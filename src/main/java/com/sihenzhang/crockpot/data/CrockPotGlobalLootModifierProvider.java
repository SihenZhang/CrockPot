package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.loot.CrockPotLootModifiers;
import com.sihenzhang.crockpot.loot.FishingLootModifier;
import com.sihenzhang.crockpot.loot.UnknownSeedsDropModifier;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class CrockPotGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public CrockPotGlobalLootModifierProvider(DataGenerator generator) {
        super(generator, CrockPot.MOD_ID);
    }

    @Override
    protected void start() {
        this.add("unknown_seeds_from_grass", CrockPotLootModifiers.UNKNOWN_SEEDS_DROP.get(), new UnknownSeedsDropModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))).build(),
                ExplosionCondition.survivesExplosion().build(),
                LootItemRandomChanceCondition.randomChance(0.1F).build()
        }));
        this.add("unknown_seeds_from_tall_grass", CrockPotLootModifiers.UNKNOWN_SEEDS_DROP.get(), new UnknownSeedsDropModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS).build(),
                InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))).build(),
                ExplosionCondition.survivesExplosion().build(),
                LootItemRandomChanceCondition.randomChance(0.1F).build()
        }));

        this.add("fishing_frog_legs", CrockPotLootModifiers.FISHING.get(), new FishingLootModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(BuiltInLootTables.FISHING_FISH).build(),
                AlternativeLootItemCondition.alternative(
                        LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.PLAINS)),
                        LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.SUNFLOWER_PLAINS)),
                        LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.SWAMP)),
                        LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.FOREST)),
                        LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.FLOWER_FOREST))
                ).build()
        }, CrockPotItems.FROG_LEGS.get(), 1));
    }
}
