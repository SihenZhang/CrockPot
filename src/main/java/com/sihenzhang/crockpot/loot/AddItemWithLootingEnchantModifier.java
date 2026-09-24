package com.sihenzhang.crockpot.loot;

import com.google.common.base.Suppliers;
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
import java.util.function.Supplier;

public class AddItemWithLootingEnchantModifier extends LootModifier {
    public static final Supplier<MapCodec<AddItemWithLootingEnchantModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(
            inst.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(m -> m.item),
                    Codec.INT.fieldOf("count").forGetter(m -> m.count),
                    Codec.INT.fieldOf("limit").forGetter(m -> m.limit)
            )).apply(inst, AddItemWithLootingEnchantModifier::new)));

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
        var attacker = context.getParamOrNull(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ATTACKING_ENTITY);
        var looting = context.getLevel().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(net.minecraft.world.item.enchantment.Enchantments.LOOTING);
        var lootingModifier = attacker instanceof net.minecraft.world.entity.LivingEntity living ? net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentLevel(looting, living) : 0;
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
