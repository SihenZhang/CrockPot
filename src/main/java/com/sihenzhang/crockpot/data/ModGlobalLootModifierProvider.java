package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.loot.AddItemModifier;
import com.sihenzhang.crockpot.loot.AddItemWithLootingEnchantModifier;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CrockPot.MOD_ID);
    }

    @Override
    protected void start() {
        var items = this.registries.lookupOrThrow(Registries.ITEM);
        var entityTypes = this.registries.lookupOrThrow(Registries.ENTITY_TYPE);
        this.add("unknown_seeds_from_grass", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SHORT_GRASS).build(),
                InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, Items.SHEARS))).build(),
                ExplosionCondition.survivesExplosion().build(),
                LootItemRandomChanceCondition.randomChance(0.1F).build()
        }, 0, ModItems.UNKNOWN_SEEDS.get(), 1));
        this.add("unknown_seeds_from_tall_grass", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS).build(),
                InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, Items.SHEARS))).build(),
                ExplosionCondition.survivesExplosion().build(),
                LootItemRandomChanceCondition.randomChance(0.1F).build()
        }, 0, ModItems.UNKNOWN_SEEDS.get(), 1));
        this.add("hoglin_nose_from_hoglin", new AddItemModifier(new LootItemCondition[]{
                LootItemKilledByPlayerCondition.killedByPlayer().build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.3F, 0.03F).build(),
                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityType.HOGLIN)).flags(EntityFlagsPredicate.Builder.flags().setOnFire(false))).build()
        }, 0, ModItems.HOGLIN_NOSE.get(), 1));
        this.add("cooked_hoglin_nose_from_hoglin", new AddItemModifier(new LootItemCondition[]{
                LootItemKilledByPlayerCondition.killedByPlayer().build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.3F, 0.03F).build(),
                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityType.HOGLIN)).flags(EntityFlagsPredicate.Builder.flags().setOnFire(true))).build()
        }, 0, ModItems.COOKED_HOGLIN_NOSE.get(), 1));
        this.add("frog_legs_from_frog", new AddItemWithLootingEnchantModifier(new LootItemCondition[]{
                LootItemKilledByPlayerCondition.killedByPlayer().build(),
                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityType.FROG)).flags(EntityFlagsPredicate.Builder.flags().setOnFire(false))).build()
        }, 0, ModItems.FROG_LEGS.get(), 1, 4));
        this.add("cooked_frog_legs_from_frog", new AddItemWithLootingEnchantModifier(new LootItemCondition[]{
                LootItemKilledByPlayerCondition.killedByPlayer().build(),
                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityType.FROG)).flags(EntityFlagsPredicate.Builder.flags().setOnFire(true))).build()
        }, 0, ModItems.COOKED_FROG_LEGS.get(), 1, 4));
        this.add("crock_pot_upgrade_smithing_template_from_nether_bridge", new AddItemModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(IdUtil.mc("chests/nether_bridge")).build(),
                LootItemRandomChanceCondition.randomChance(0.1F).build()
        }, 0, ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get(), 1));
    }
}
