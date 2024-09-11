package com.sihenzhang.crockpot.recipe;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RangedItem {
    public static final Codec<RangedItem> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.ITEM_NON_AIR_CODEC.fieldOf("item").forGetter(RangedItem::getItemHolder),
                    Codec.withAlternative(
                            Codec.pair(Codec.INT.fieldOf("min").codec(), Codec.INT.fieldOf("max").codec()),
                            Codec.INT.xmap(count -> Pair.of(count, count), Pair::getFirst)
                    ).fieldOf("count").forGetter(r -> Pair.of(r.min, r.max))
            ).apply(instance, (item, count) -> new RangedItem(item.value(), count.getFirst(), count.getSecond()))
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedItem> STREAM_CODEC = StreamCodec.of(
            RangedItem::toNetwork, RangedItem::fromNetwork
    );

    public final Item item;
    public final int min;
    public final int max;

    public RangedItem(Item item, int min, int max) {
        Preconditions.checkArgument(min >= 0 || max >= 0, "The count of RangedItem should not be less than 0");
        if (min == 0 && max == 0) {
            CrockPot.LOGGER.warn("The count of RangedItem is 0, make sure this is intentional!");
        }
        if (min > max) {
            CrockPot.LOGGER.warn("The minimum count of RangedItem is greater than the maximum count, make sure this is intentional!");
        }
        this.item = item;
        this.min = min;
        this.max = max;
    }

    public RangedItem(Item item, int count) {
        this(item, count, count);
    }

    @SuppressWarnings("deprecation")
    public Holder<Item> getItemHolder() {
        return this.item.builtInRegistryHolder();
    }

    public boolean isRanged() {
        return min != max;
    }

    public ItemStack getInstance(RandomSource random) {
        if (this.isRanged()) {
            return new ItemStack(item, Mth.nextInt(random, min, max));
        }
        return new ItemStack(item, min);
    }

    private static RangedItem fromNetwork(RegistryFriendlyByteBuf buffer) {
        var item = ByteBufCodecs.registry(Registries.ITEM).decode(buffer);
        var min = buffer.readByte();
        var max = buffer.readByte();
        return new RangedItem(item, min, max);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, RangedItem value) {
        ByteBufCodecs.registry(Registries.ITEM).encode(buffer, value.item);
        buffer.writeByte(value.min);
        buffer.writeByte(value.max);
    }
}
