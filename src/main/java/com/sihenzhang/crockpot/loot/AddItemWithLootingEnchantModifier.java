package com.sihenzhang.crockpot.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.Nonnull;

public class AddItemWithLootingEnchantModifier extends LootModifier {
    public static final MapCodec<AddItemWithLootingEnchantModifier> CODEC = RecordCodecBuilder.mapCodec(
            instance -> LootModifier.codecStart(instance).and(instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(lm -> lm.item),
                    Codec.INT.fieldOf("count").forGetter(lm -> lm.count),
                    Codec.INT.fieldOf("limit").forGetter(lm -> lm.limit)
            )).apply(instance, AddItemWithLootingEnchantModifier::new)
    );

    private static final UniformGenerator RANDOM_NUMBER_GENERATOR = UniformGenerator.between(0.0F, 1.0F);

    private final Item item;
    private final int count;
    private final int limit;

    public AddItemWithLootingEnchantModifier(LootItemCondition[] conditionsIn, Item item, int count, int limit) {
        super(conditionsIn);
        this.item = item;
        this.count = count;
        this.limit = limit;
    }

    @Override
    protected @Nonnull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var result = new ItemStack(item, count);
//        var lootingModifier = context.getLootingModifier();
//        if (lootingModifier > 0) {
//            var bonus = (float) lootingModifier * RANDOM_NUMBER_GENERATOR.getFloat(context);
//            result.grow(Math.round(bonus));
//            if (limit > 0 && result.getCount() > limit) {
//                result.setCount(limit);
//            }
//        }
        generatedLoot.add(result);
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
