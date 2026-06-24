package com.sihenzhang.crockpot.recipe;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RangedItem {
    private static final Codec<CountRange> COUNT_CODEC = Codec.either(Codec.INT, CountRange.CODEC)
            .xmap(either -> either.map(CountRange::single, range -> range), range -> range.min() == range.max() ? com.mojang.datafixers.util.Either.left(range.min()) : com.mojang.datafixers.util.Either.right(range));
    public static final MapCodec<RangedItem> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Item.CODEC.xmap(Holder::value, Item::builtInRegistryHolder).fieldOf("item").forGetter(RangedItem::getItem),
            COUNT_CODEC.optionalFieldOf("count", CountRange.single(1)).forGetter(item -> new CountRange(item.min, item.max))
    ).apply(instance, (item, count) -> new RangedItem(item, count.min(), count.max())));
    public static final Codec<RangedItem> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedItem> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC.map(Holder::value, Item::builtInRegistryHolder),
            RangedItem::getItem,
            ByteBufCodecs.VAR_INT,
            RangedItem::getMin,
            ByteBufCodecs.VAR_INT,
            RangedItem::getMax,
            RangedItem::new
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

    public Item getItem() {
        return item;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
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

    private record CountRange(int min, int max) {
        static final Codec<CountRange> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.optionalFieldOf("min", 1).forGetter(CountRange::min),
                Codec.INT.optionalFieldOf("max", 1).forGetter(CountRange::max)
        ).apply(instance, CountRange::new));

        static CountRange single(int count) {
            return new CountRange(count, count);
        }
    }
}
