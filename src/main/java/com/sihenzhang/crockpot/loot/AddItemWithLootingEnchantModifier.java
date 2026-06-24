package com.sihenzhang.crockpot.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AddItemWithLootingEnchantModifier extends LootModifier {
    public static final Supplier<MapCodec<AddItemWithLootingEnchantModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(
            inst.group(
                    Item.CODEC.xmap(Holder::value, Item::builtInRegistryHolder).fieldOf("item").forGetter(m -> m.item),
                    Codec.INT.fieldOf("count").forGetter(m -> m.count),
                    Codec.INT.fieldOf("limit").forGetter(m -> m.limit)
            )).apply(inst, AddItemWithLootingEnchantModifier::new)));

    private static final UniformGenerator RANDOM_NUMBER_GENERATOR = UniformGenerator.between(0.0F, 1.0F);

    private final Item item;
    private final int count;
    private final int limit;

    public AddItemWithLootingEnchantModifier(LootItemCondition[] conditionsIn, int priority, Item item, int count, int limit) {
        super(conditionsIn, priority);
        this.item = item;
        this.count = count;
        this.limit = limit;
    }

    @Override
    protected @Nonnull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var result = new ItemStack(item, count);
        var lootingModifier = context.getOptionalParameter(LootContextParams.ENCHANTMENT_LEVEL);
        if (lootingModifier > 0) {
            var bonus = (float) lootingModifier * RANDOM_NUMBER_GENERATOR.getFloat(context);
            result.grow(Math.round(bonus));
            if (limit > 0 && result.getCount() > limit) {
                result.setCount(limit);
            }
        }
        generatedLoot.add(result);
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
