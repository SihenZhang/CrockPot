package com.sihenzhang.crockpot.data;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.AbstractCropBlock;
import com.sihenzhang.crockpot.block.BirdcageBlock;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.block.DryingRackBlock;
import com.sihenzhang.crockpot.block.ModBlocks;
import com.sihenzhang.crockpot.block.food.CrockPotStackableFoodBlock;
import com.sihenzhang.crockpot.item.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, CrockPot.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // ========== Blocks ==========

        this.createCrockPotBlock(blockModels, ModBlocks.CROCK_POT.get());
        this.createCrockPotBlock(blockModels, ModBlocks.PORTABLE_CROCK_POT.get());

        this.createBirdcage(blockModels);
        this.createDryingRack(blockModels);

        this.createNonTemplateModelBlockWithDefaultItem(blockModels, ModBlocks.UNKNOWN_CROPS.get());
        blockModels.createCropBlock(ModBlocks.ASPARAGUS.get(), AbstractCropBlock.AGE, 0, 0, 1, 1, 2, 2, 2, 3);
        this.createDoubleCropBlock(blockModels, ModBlocks.CORN.get(), AbstractCropBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7);
        this.createCrossBlock(blockModels, ModBlocks.EGGPLANT.get(), BlockModelGenerators.PlantType.NOT_TINTED, AbstractCropBlock.AGE, 0, 0, 1, 1, 2, 2, 2, 3);
        blockModels.createCropBlock(ModBlocks.GARLIC.get(), AbstractCropBlock.AGE, 0, 0, 1, 1, 2, 2, 2, 3);
        blockModels.createCropBlock(ModBlocks.ONION.get(), AbstractCropBlock.AGE, 0, 0, 1, 1, 2, 2, 2, 3);
        blockModels.createCropBlock(ModBlocks.PEPPER.get(), AbstractCropBlock.AGE, 0, 0, 1, 1, 2, 2, 2, 3);
        this.createCrossBlock(blockModels, ModBlocks.TOMATO.get(), BlockModelGenerators.PlantType.NOT_TINTED, AbstractCropBlock.AGE, 0, 0, 1, 1, 2, 2, 2, 3);

        ModBlocks.FOODS.get().forEach(block -> this.createFoodBlock(blockModels, block));
        ModBlocks.NON_FOODS.get().forEach(block -> this.createFoodBlock(blockModels, block));

        // ========== Items ==========

        ModItems.CROPS.get().forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        ModItems.COOKED_CROPS.get().forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        ModItems.DRIED_FOODS.get().forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));

        ModItems.PARROT_EGGS.values().forEach(item -> itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));

        itemModels.generateFlatItem(ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.BLACKSTONE_DUST.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.COLLECTED_DUST.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.COOKED_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FROG_LEGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.COOKED_FROG_LEGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.HOGLIN_NOSE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.COOKED_HOGLIN_NOSE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.MILK_BOTTLE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SYRUP.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.VOLT_GOAT_HORN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.MILKMADE_HAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.CREATIVE_MILKMADE_HAT.get(), ModItems.MILKMADE_HAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GNAWS_COIN.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.VOLT_GOAT_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
    }

    private void createCrockPotBlock(BlockModelGenerators blockModels, Block block) {
        blockModels.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block, "_gui"));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(CrockPotBlock.OPEN, CrockPotBlock.LIT).generate((open, lit) -> {
                    var suffix = (open ? "_open" : "") + (lit ? "_lit" : "");
                    return plainVariant(ModelLocationUtils.getModelLocation(block, suffix));
                }))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
        );
    }

    private void createBirdcage(BlockModelGenerators blockModels) {
        var block = ModBlocks.BIRDCAGE.get();
        blockModels.registerSimpleFlatItemModel(block.asItem());
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
                PropertyDispatch.initial(BirdcageBlock.HALF, BirdcageBlock.HANGING).generate((half, hanging) -> {
                    var suffix = (hanging ? "_hanging" : "") + "_" + half.getSerializedName();
                    return plainVariant(ModelLocationUtils.getModelLocation(block, suffix));
                })
        ));
    }

    private void createDryingRack(BlockModelGenerators blockModels) {
        var block = ModBlocks.DRYING_RACK.get();
        blockModels.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
                PropertyDispatch.initial(DryingRackBlock.WALL, DryingRackBlock.STACKS).generate((wall, stacks) -> {
                    var suffix = (wall ? "_wall" : "") + (stacks == 1 ? "" : "_2");
                    return plainVariant(ModelLocationUtils.getModelLocation(block, suffix));
                })
        ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }

    private void createNonTemplateModelBlockWithDefaultItem(BlockModelGenerators blockModels, Block block) {
        blockModels.registerSimpleFlatItemModel(block.asItem());
        blockModels.createNonTemplateModelBlock(block);
    }

    private void createCrossBlock(BlockModelGenerators blockModels, Block block, BlockModelGenerators.PlantType plantType, Property<Integer> property, int... stages) {
        blockModels.registerSimpleFlatItemModel(block.asItem());
        if (property.getPossibleValues().size() != stages.length) {
            throw new IllegalArgumentException();
        }
        Int2ObjectMap<Identifier> models = new Int2ObjectOpenHashMap<>();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
                PropertyDispatch.initial(property).generate(age -> plainVariant(
                        models.computeIfAbsent(stages[age], s -> {
                            String suffix = "_stage" + s;
                            TextureMapping texture = TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix));
                            return plantType.getCross().createWithSuffix(block, suffix, texture, blockModels.modelOutput);
                        })
                ))
        ));
    }

    private void createDoubleCropBlock(BlockModelGenerators blockModels, Block block, Property<Integer> property, int... stages) {
        blockModels.registerSimpleFlatItemModel(block.asItem());
        if (property.getPossibleValues().size() != stages.length) {
            throw new IllegalArgumentException();
        }
        Table<Integer, DoubleBlockHalf, Identifier> models = HashBasedTable.create();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
                PropertyDispatch.initial(property, BlockStateProperties.DOUBLE_BLOCK_HALF).generate(
                        (age, half) -> plainVariant(models.column(half).computeIfAbsent(stages[age], s -> switch (half) {
                            case UPPER ->
                                    blockModels.createSuffixedVariant(block, "_top_stage_" + s, ModelTemplates.CROP, TextureMapping::crop);
                            case LOWER ->
                                    blockModels.createSuffixedVariant(block, "_bottom_stage_" + s, ModelTemplates.CROP, TextureMapping::crop);
                        }))
                )
        ));
    }

    private void createFoodBlock(BlockModelGenerators blockModels, Block block) {
        blockModels.registerSimpleFlatItemModel(block.asItem());

        if (block instanceof CrockPotStackableFoodBlock stackableBlock) {
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
                    PropertyDispatch.initial(stackableBlock.getStacksProperty()).generate(stackCount -> plainVariant(
                            ModelLocationUtils.getModelLocation(block, stackCount == 1 ? "" : "_" + (stackCount - 1))
                    ))
            ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        } else {
            blockModels.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, plainVariant(ModelLocationUtils.getModelLocation(block)))
                            .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
            );
        }
    }

    private static MultiVariant plainVariant(Identifier model) {
        return BlockModelGenerators.plainVariant(model);
    }
}
