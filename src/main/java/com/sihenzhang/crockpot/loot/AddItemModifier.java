package com.sihenzhang.crockpot.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    public static final Supplier<MapCodec<AddItemModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(
            inst.group(
                    Item.CODEC.xmap(Holder::value, Item::builtInRegistryHolder).fieldOf("item").forGetter(m -> m.item),
                    com.mojang.serialization.Codec.INT.fieldOf("count").forGetter(m -> m.count)
            )).apply(inst, AddItemModifier::new)));

    private final Item item;
    private final int count;

    public AddItemModifier(LootItemCondition[] conditionsIn, int priority, Item item, int count) {
        super(conditionsIn, priority);
        this.item = item;
        this.count = count;
    }

    @Override
    protected @Nonnull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(item, count));
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
