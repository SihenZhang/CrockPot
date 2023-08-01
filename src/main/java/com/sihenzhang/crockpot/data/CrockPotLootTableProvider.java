package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import com.sihenzhang.crockpot.block.AbstractCrockPotDoubleCropBlock;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrockPotLootTableProvider extends LootTableProvider {
    public CrockPotLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(new SubProviderEntry(CrockPotBlockLoot::new, LootContextParamSets.BLOCK)));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {
        map.forEach((name, table) -> table.validate(validationTracker));
    }

    public static class CrockPotBlockLoot extends VanillaBlockLoot {
        @Override
        protected void generate() {
            this.dropSelf(CrockPotBlocks.BASIC_CROCK_POT.get());
            this.dropSelf(CrockPotBlocks.ADVANCED_CROCK_POT.get());
            this.dropSelf(CrockPotBlocks.ULTIMATE_CROCK_POT.get());
            this.add(CrockPotBlocks.BIRDCAGE.get(), createDoorTable(CrockPotBlocks.BIRDCAGE.get()));
            this.dropSelf(CrockPotBlocks.UNKNOWN_CROPS.get());
            this.add(CrockPotBlocks.ASPARAGUS.get(), createCropDropsWithSeed(CrockPotBlocks.ASPARAGUS.get(), CrockPotItems.ASPARAGUS.get(), CrockPotItems.ASPARAGUS_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.ASPARAGUS.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(CrockPotBlocks.EGGPLANT.get(), createCropDropsWithSeed(CrockPotBlocks.EGGPLANT.get(), CrockPotItems.EGGPLANT.get(), CrockPotItems.EGGPLANT_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.EGGPLANT.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(CrockPotBlocks.GARLIC.get(), createCropDropsWithSeed(CrockPotBlocks.GARLIC.get(), CrockPotItems.GARLIC.get(), CrockPotItems.GARLIC_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.GARLIC.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(CrockPotBlocks.ONION.get(), createCropDropsWithSeed(CrockPotBlocks.ONION.get(), CrockPotItems.ONION.get(), CrockPotItems.ONION_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.ONION.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(CrockPotBlocks.PEPPER.get(), createCropDropsWithSeed(CrockPotBlocks.PEPPER.get(), CrockPotItems.PEPPER.get(), CrockPotItems.PEPPER_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.PEPPER.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(CrockPotBlocks.TOMATO.get(), createCropDropsWithSeed(CrockPotBlocks.TOMATO.get(), CrockPotItems.TOMATO.get(), CrockPotItems.TOMATO_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.TOMATO.get(), AbstractCrockPotCropBlock.AGE, 7)));
        }

        /**
         * If {@code dropGrownCropCondition} fails (i.e. crop is not ready), drops 1 {@code seedsItem}.
         * If {@code dropGrownCropCondition} succeeds (i.e. crop is ready), drops 1 {@code seedsItem}, and 1-4 {@code
         * grownCropItem} with fortune applied.
         */
        protected LootTable.Builder createCropDropsWithSeed(Block pCropBlock, Item pGrownCropItem, Item pSeedsItem, LootItemCondition.Builder pDropGrownCropCondition) {
            return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(pSeedsItem))).withPool(LootPool.lootPool().when(pDropGrownCropCondition).add(this.applyExplosionDecay(pGrownCropItem, LootItem.lootTableItem(pGrownCropItem).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))));
        }

        protected static LootItemCondition.Builder blockStatePropertyCondition(Block pBlock, Property<Integer> pProperty, int pValue) {
            return LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(pProperty, pValue));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return CrockPotBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> !(block instanceof AbstractCrockPotDoubleCropBlock)).toList();
        }
    }
}
