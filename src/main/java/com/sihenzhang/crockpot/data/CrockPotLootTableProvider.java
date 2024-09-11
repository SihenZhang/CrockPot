package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import com.sihenzhang.crockpot.block.AbstractCrockPotDoubleCropBlock;
import com.sihenzhang.crockpot.block.ModBlocks;
import com.sihenzhang.crockpot.block.food.AbstractStackableFoodBlock;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.data.loot.packs.VanillaEntityLoot;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class CrockPotLootTableProvider extends LootTableProvider {
    public CrockPotLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Set.of(), List.of(new SubProviderEntry(CrockPotBlockLoot::new, LootContextParamSets.BLOCK), new SubProviderEntry(CrockPotEntityLoot::new, LootContextParamSets.ENTITY)), provider);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
        // Do not validate against all registered loot tables
    }

    public static class CrockPotBlockLoot extends VanillaBlockLoot {
        public CrockPotBlockLoot(HolderLookup.Provider registries) {
            super(registries);
        }

        @Override
        protected void generate() {
            HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

            this.add(ModBlocks.CROCK_POT.get(), this.createSingleItemTableWithSilkTouch(ModBlocks.CROCK_POT.get(), Blocks.STONE, ConstantValue.exactly(6.0F)));
            this.dropSelf(ModBlocks.PORTABLE_CROCK_POT.get());
            this.add(ModBlocks.BIRDCAGE.get(), createDoorTable(ModBlocks.BIRDCAGE.get()));
            this.dropSelf(ModBlocks.UNKNOWN_CROPS.get());
            this.add(ModBlocks.ASPARAGUS.get(), createCropDropsWithSeed(registrylookup, ModBlocks.ASPARAGUS.get(), ModItems.ASPARAGUS.get(), ModItems.ASPARAGUS_SEEDS.get(), blockStatePropertyCondition(ModBlocks.ASPARAGUS.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(ModBlocks.EGGPLANT.get(), createCropDropsWithSeed(registrylookup, ModBlocks.EGGPLANT.get(), ModItems.EGGPLANT.get(), ModItems.EGGPLANT_SEEDS.get(), blockStatePropertyCondition(ModBlocks.EGGPLANT.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(ModBlocks.GARLIC.get(), createCropDropsWithSeed(registrylookup, ModBlocks.GARLIC.get(), ModItems.GARLIC.get(), ModItems.GARLIC_SEEDS.get(), blockStatePropertyCondition(ModBlocks.GARLIC.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(ModBlocks.ONION.get(), createCropDropsWithSeed(registrylookup, ModBlocks.ONION.get(), ModItems.ONION.get(), ModItems.ONION_SEEDS.get(), blockStatePropertyCondition(ModBlocks.ONION.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(ModBlocks.PEPPER.get(), createCropDropsWithSeed(registrylookup, ModBlocks.PEPPER.get(), ModItems.PEPPER.get(), ModItems.PEPPER_SEEDS.get(), blockStatePropertyCondition(ModBlocks.PEPPER.get(), AbstractCrockPotCropBlock.AGE, 7)));
            this.add(ModBlocks.TOMATO.get(), createCropDropsWithSeed(registrylookup, ModBlocks.TOMATO.get(), ModItems.TOMATO.get(), ModItems.TOMATO_SEEDS.get(), blockStatePropertyCondition(ModBlocks.TOMATO.get(), AbstractCrockPotCropBlock.AGE, 7)));

            ModBlocks.FOODS.get().forEach(this::dropFood);
        }

        /**
         * If {@code dropGrownCropCondition} fails (i.e. crop is not ready), drops 1 {@code seedsItem}.
         * If {@code dropGrownCropCondition} succeeds (i.e. crop is ready), drops 1 {@code seedsItem}, and 1-4 {@code
         * grownCropItem} with fortune applied.
         */
        protected LootTable.Builder createCropDropsWithSeed(HolderLookup.RegistryLookup<Enchantment> registryLookup, Block pCropBlock, Item pGrownCropItem, Item pSeedsItem, LootItemCondition.Builder pDropGrownCropCondition) {
            return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(pSeedsItem))).withPool(LootPool.lootPool().when(pDropGrownCropCondition).add(this.applyExplosionDecay(pGrownCropItem, LootItem.lootTableItem(pGrownCropItem).apply(ApplyBonusCount.addBonusBinomialDistributionCount(registryLookup.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));
        }

        protected static LootItemCondition.Builder blockStatePropertyCondition(Block pBlock, Property<Integer> pProperty, int pValue) {
            return LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(pProperty, pValue));
        }

        protected void dropFood(Block block) {
            if (block instanceof AbstractStackableFoodBlock stackableFoodBlock) {
                var lootTable = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(stackableFoodBlock, LootItem.lootTableItem(stackableFoodBlock).apply(List.of(1, 2, 3, 4, 5, 6), (p_249985_) -> SetItemCountFunction.setCount(ConstantValue.exactly((float) p_249985_)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(stackableFoodBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(stackableFoodBlock.getStacksProperty(), p_249985_)))))));
                this.add(block, lootTable);
            } else {
                this.dropSelf(block);
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> !(block instanceof AbstractCrockPotDoubleCropBlock)).map(Block.class::cast).toList();
        }
    }

    public static class CrockPotEntityLoot extends VanillaEntityLoot {
        public CrockPotEntityLoot(HolderLookup.Provider registries) {
            super(registries);
        }

        @Override
        public void generate() {
            this.add(ModEntities.VOLT_GOAT.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModItems.VOLT_GOAT_HORN.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(-2.0F, 1.0F))).apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0F, 1.0F)).setLimit(2)))));
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return Stream.of(ModEntities.VOLT_GOAT.get());
        }
    }
}
