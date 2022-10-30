package com.sihenzhang.crockpot.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import com.sihenzhang.crockpot.block.AbstractCrockPotDoubleCropBlock;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CrockPotLootTableProvider extends LootTableProvider {
    public CrockPotLootTableProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(Pair.of(CrockPotBlockLoot::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {
        map.forEach((name, table) -> LootTables.validate(validationTracker, name, table));
    }

    @Override
    public String getName() {
        return "CrockPot LootTables";
    }

    public static class CrockPotBlockLoot extends BlockLoot {
        @Override
        protected void addTables() {
            this.dropSelf(CrockPotRegistry.BASIC_CROCK_POT_BLOCK.get());
            this.dropSelf(CrockPotRegistry.ADVANCED_CROCK_POT_BLOCK.get());
            this.dropSelf(CrockPotRegistry.ULTIMATE_CROCK_POT_BLOCK.get());
            this.add(CrockPotRegistry.BIRDCAGE_BLOCK.get(), createDoorTable(CrockPotRegistry.BIRDCAGE_BLOCK.get()));
            this.dropSelf(CrockPotRegistry.UNKNOWN_CROPS_BLOCK.get());
            this.add(CrockPotRegistry.ASPARAGUS_BLOCK.get(), createCropDropsWithSeed(CrockPotRegistry.ASPARAGUS_BLOCK.get(), CrockPotRegistry.ASPARAGUS.get(), CrockPotRegistry.ASPARAGUS_SEEDS.get(), blockStatePropertyCondition(CrockPotRegistry.ASPARAGUS_BLOCK.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(CrockPotRegistry.EGGPLANT_BLOCK.get(), createCropDropsWithSeed(CrockPotRegistry.EGGPLANT_BLOCK.get(), CrockPotRegistry.EGGPLANT.get(), CrockPotRegistry.EGGPLANT_SEEDS.get(), blockStatePropertyCondition(CrockPotRegistry.EGGPLANT_BLOCK.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(CrockPotRegistry.GARLIC_BLOCK.get(), createCropDropsWithSeed(CrockPotRegistry.GARLIC_BLOCK.get(), CrockPotRegistry.GARLIC.get(), CrockPotRegistry.GARLIC_SEEDS.get(), blockStatePropertyCondition(CrockPotRegistry.GARLIC_BLOCK.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(CrockPotRegistry.ONION_BLOCK.get(), createCropDropsWithSeed(CrockPotRegistry.ONION_BLOCK.get(), CrockPotRegistry.ONION.get(), CrockPotRegistry.ONION_SEEDS.get(), blockStatePropertyCondition(CrockPotRegistry.ONION_BLOCK.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(CrockPotRegistry.PEPPER_BLOCK.get(), createCropDropsWithSeed(CrockPotRegistry.PEPPER_BLOCK.get(), CrockPotRegistry.PEPPER.get(), CrockPotRegistry.PEPPER_SEEDS.get(), blockStatePropertyCondition(CrockPotRegistry.PEPPER_BLOCK.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(CrockPotRegistry.TOMATO_BLOCK.get(), createCropDropsWithSeed(CrockPotRegistry.TOMATO_BLOCK.get(), CrockPotRegistry.TOMATO.get(), CrockPotRegistry.TOMATO_SEEDS.get(), blockStatePropertyCondition(CrockPotRegistry.TOMATO_BLOCK.get(), AbstractCrockPotCropBlock.AGE, 7)));
        }

        /**
         * If {@code dropGrownCropCondition} fails (i.e. crop is not ready), drops 1 {@code seedsItem}.
         * If {@code dropGrownCropCondition} succeeds (i.e. crop is ready), drops 1 {@code seedsItem}, and 1-4 {@code
         * grownCropItem} with fortune applied.
         */
        protected static LootTable.Builder createCropDropsWithSeed(Block pCropBlock, Item pGrownCropItem, Item pSeedsItem, LootItemCondition.Builder pDropGrownCropCondition) {
            return applyExplosionDecay(pCropBlock, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(pSeedsItem))).withPool(LootPool.lootPool().when(pDropGrownCropCondition).add(LootItem.lootTableItem(pGrownCropItem).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))));
        }

        protected static LootItemCondition.Builder blockStatePropertyCondition(Block pBlock, Property<Integer> pProperty, int pValue) {
            return LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(pProperty, pValue));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return CrockPotRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> !(block instanceof AbstractCrockPotDoubleCropBlock)).toList();
        }
    }
}
