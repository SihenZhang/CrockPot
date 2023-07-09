package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.loot.AddItemModifier;
import com.sihenzhang.crockpot.loot.AddItemWithLootingEnchantModifier;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class CrockPotGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public CrockPotGlobalLootModifierProvider(PackOutput output) {
        super(output, CrockPot.MOD_ID);
    }

    @Override
    protected void start() {
        this.add("unknown_seeds_from_grass", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))).build(),
                ExplosionCondition.survivesExplosion().build(),
                LootItemRandomChanceCondition.randomChance(0.1F).build()
        }, CrockPotItems.UNKNOWN_SEEDS.get(), 1));
        this.add("unknown_seeds_from_tall_grass", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS).build(),
                InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))).build(),
                ExplosionCondition.survivesExplosion().build(),
                LootItemRandomChanceCondition.randomChance(0.1F).build()
        }, CrockPotItems.UNKNOWN_SEEDS.get(), 1));
        this.add("hoglin_nose_from_hoglin", new AddItemModifier(new LootItemCondition[]{
                LootItemKilledByPlayerCondition.killedByPlayer().build(),
                LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.3F, 0.03F).build(),
                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.HOGLIN)).flags(EntityFlagsPredicate.Builder.flags().setOnFire(false).build())).build()
        }, CrockPotItems.HOGLIN_NOSE.get(), 1));
        this.add("cooked_hoglin_nose_from_hoglin", new AddItemModifier(new LootItemCondition[]{
                LootItemKilledByPlayerCondition.killedByPlayer().build(),
                LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.3F, 0.03F).build(),
                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.HOGLIN)).flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build())).build()
        }, CrockPotItems.COOKED_HOGLIN_NOSE.get(), 1));
        this.add("frog_legs_from_frog", new AddItemWithLootingEnchantModifier(new LootItemCondition[]{
                LootItemKilledByPlayerCondition.killedByPlayer().build(),
                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.FROG)).flags(EntityFlagsPredicate.Builder.flags().setOnFire(false).build())).build()
        }, CrockPotItems.FROG_LEGS.get(), 1, 4));
        this.add("cooked_frog_legs_from_frog", new AddItemWithLootingEnchantModifier(new LootItemCondition[]{
                LootItemKilledByPlayerCondition.killedByPlayer().build(),
                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.FROG)).flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build())).build()
        }, CrockPotItems.COOKED_FROG_LEGS.get(), 1, 4));
    }
}
