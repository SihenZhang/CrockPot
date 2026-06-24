package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.block.*;
import com.sihenzhang.crockpot.block.food.CrockPotStackableFoodBlock;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(CrockPotBlockLoot::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(CrockPotEntityLoot::new, LootContextParamSets.ENTITY)
        ), registries);
    }

    public static class CrockPotBlockLoot extends BlockLootSubProvider {
        protected CrockPotBlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected void generate() {
            this.add(ModBlocks.CROCK_POT.get(), this.createSingleItemTableWithSilkTouch(ModBlocks.CROCK_POT.get(), Blocks.STONE, ConstantValue.exactly(6.0F)));
            this.dropSelf(ModBlocks.PORTABLE_CROCK_POT.get());
            this.add(ModBlocks.BIRDCAGE.get(), this.createDoorTable(ModBlocks.BIRDCAGE.get()));
            this.add(ModBlocks.DRYING_RACK.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(ModBlocks.DRYING_RACK.get(), LootItem.lootTableItem(ModBlocks.DRYING_RACK.get()).apply(List.of(1, 2), stackCount -> SetItemCountFunction.setCount(ConstantValue.exactly(stackCount.floatValue())).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.DRYING_RACK.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DryingRackBlock.STACKS, stackCount))))))));
            this.dropSelf(ModBlocks.UNKNOWN_CROPS.get());
            this.add(ModBlocks.ASPARAGUS.get(), createCropDropsWithSeed(ModBlocks.ASPARAGUS.get(), ModItems.ASPARAGUS.get(), ModItems.ASPARAGUS_SEEDS.get(), blockStatePropertyCondition(ModBlocks.ASPARAGUS.get(), AbstractCropBlock.AGE, 7)));
            this.add(ModBlocks.EGGPLANT.get(), createCropDropsWithSeed(ModBlocks.EGGPLANT.get(), ModItems.EGGPLANT.get(), ModItems.EGGPLANT_SEEDS.get(), blockStatePropertyCondition(ModBlocks.EGGPLANT.get(), AbstractCropBlock.AGE, 7)));
            this.add(ModBlocks.GARLIC.get(), createCropDropsWithSeed(ModBlocks.GARLIC.get(), ModItems.GARLIC.get(), ModItems.GARLIC_SEEDS.get(), blockStatePropertyCondition(ModBlocks.GARLIC.get(), AbstractCropBlock.AGE, 7)));
            this.add(ModBlocks.ONION.get(), createCropDropsWithSeed(ModBlocks.ONION.get(), ModItems.ONION.get(), ModItems.ONION_SEEDS.get(), blockStatePropertyCondition(ModBlocks.ONION.get(), AbstractCropBlock.AGE, 7)));
            this.add(ModBlocks.PEPPER.get(), createCropDropsWithSeed(ModBlocks.PEPPER.get(), ModItems.PEPPER.get(), ModItems.PEPPER_SEEDS.get(), blockStatePropertyCondition(ModBlocks.PEPPER.get(), AbstractCropBlock.AGE, 7)));
            this.add(ModBlocks.TOMATO.get(), createCropDropsWithSeed(ModBlocks.TOMATO.get(), ModItems.TOMATO.get(), ModItems.TOMATO_SEEDS.get(), blockStatePropertyCondition(ModBlocks.TOMATO.get(), AbstractCropBlock.AGE, 7)));

            ModBlocks.FOODS.get().forEach(this::dropFood);
            ModBlocks.NON_FOODS.get().forEach(this::dropFood);
        }

        protected LootTable.Builder createCropDropsWithSeed(Block cropBlock, Item grownCropItem, Item seedsItem, LootItemCondition.Builder dropGrownCropCondition) {
            var enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
            return LootTable.lootTable()
                    .withPool(LootPool.lootPool().add(LootItem.lootTableItem(seedsItem)))
                    .withPool(LootPool.lootPool().when(dropGrownCropCondition).add(this.applyExplosionDecay(cropBlock, LootItem.lootTableItem(grownCropItem)
                            .apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));
        }

        protected static LootItemCondition.Builder blockStatePropertyCondition(Block block, Property<Integer> property, int value) {
            return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, value));
        }

        protected void dropFood(Block block) {
            if (block instanceof CrockPotStackableFoodBlock stackableFoodBlock) {
                var stackValues = IntStream.rangeClosed(1, stackableFoodBlock.getMaxStacks()).boxed().toList();
                var lootTable = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(stackableFoodBlock, LootItem.lootTableItem(stackableFoodBlock).apply(stackValues, stackCount -> SetItemCountFunction.setCount(ConstantValue.exactly(stackCount.floatValue())).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(stackableFoodBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(stackableFoodBlock.getStacksProperty(), stackCount)))))));
                this.add(block, lootTable);
            } else {
                this.dropSelf(block);
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get()).filter(block -> !(block instanceof AbstractDoubleCropBlock)).toList();
        }
    }

    public static class CrockPotEntityLoot extends EntityLootSubProvider {
        protected CrockPotEntityLoot(HolderLookup.Provider registries) {
            super(FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        public void generate() {
            this.add(ModEntities.VOLT_GOAT.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModItems.VOLT_GOAT_HORN.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(-2.0F, 1.0F))).apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)).setLimit(2)))));
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return Stream.of(ModEntities.VOLT_GOAT.get());
        }
    }
}
