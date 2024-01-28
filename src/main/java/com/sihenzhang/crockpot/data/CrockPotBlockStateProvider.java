package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import com.sihenzhang.crockpot.block.CornBlock;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.block.food.AbstractStackableFoodBlock;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CrockPotBlockStateProvider extends BlockStateProvider {
    public CrockPotBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.crockPotBlock(CrockPotBlocks.CROCK_POT.get());
        this.crockPotBlock(CrockPotBlocks.PORTABLE_CROCK_POT.get());

        this.simpleBlock(CrockPotBlocks.UNKNOWN_CROPS.get(), this.models().crop("unknown_crops", RLUtils.createRL("block/unknown_crops")).renderType(RLUtils.createVanillaRL("cutout")));
        this.customStageCropBlock(CrockPotBlocks.ASPARAGUS.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotBlocks.CORN.get(), CornBlock.AGE, List.of());
        this.customStageCropBlock(CrockPotBlocks.EGGPLANT.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotBlocks.GARLIC.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotBlocks.ONION.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotBlocks.PEPPER.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCrossBlock(CrockPotBlocks.TOMATO.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));

        CrockPotBlocks.FOODS.get().forEach(this::foodBlock);
    }

    public void crockPotBlock(Block block) {
        var blockName = getBlockName(block);
        this.getVariantBuilder(block).forAllStates(state -> {
            var sb = new StringBuilder(blockName);
            if (state.getValue(CrockPotBlock.OPEN)) {
                sb.append("_open");
            }
            if (state.getValue(CrockPotBlock.LIT)) {
                sb.append("_lit");
            }
            return ConfiguredModel.builder()
                    .modelFile(this.models().getExistingFile(RLUtils.createRL(sb.toString())))
                    .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                    .build();
        });
    }

    public void customStageCropBlock(Block block, IntegerProperty ageProperty, List<Integer> ageSuffixes, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            var age = state.getValue(ageProperty);
            var stageName = getBlockName(block) + "_stage" + (ageSuffixes.isEmpty() ? age : ageSuffixes.get(Math.min(ageSuffixes.size(), age)));
            return ConfiguredModel.builder().modelFile(this.models().crop(stageName, RLUtils.createRL("block/" + stageName)).renderType(RLUtils.createVanillaRL("cutout"))).build();
        }, ignored);
    }

    public void customStageCrossBlock(Block block, IntegerProperty ageProperty, List<Integer> ageSuffixes, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            var age = state.getValue(ageProperty);
            var stageName = getBlockName(block) + "_stage" + (ageSuffixes.isEmpty() ? age : ageSuffixes.get(Math.min(ageSuffixes.size(), age)));
            return ConfiguredModel.builder().modelFile(this.models().cross(stageName, RLUtils.createRL("block/" + stageName)).renderType(RLUtils.createVanillaRL("cutout"))).build();
        }, ignored);
    }

    public void foodBlock(Block block) {
        var blockName = getBlockName(block);
        this.getVariantBuilder(block).forAllStates(state -> {
            var sb = new StringBuilder(blockName);
            if (state.getBlock() instanceof AbstractStackableFoodBlock stackableBlock) {
                int stackCount = state.getValue(stackableBlock.getStacksProperty());
                if (stackCount != 1) {
                    sb.append("_").append(stackCount - 1);
                }
            }
            return ConfiguredModel.builder()
                    .modelFile(this.models().getExistingFile(RLUtils.createRL(sb.toString())))
                    .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                    .build();
        });
    }

    protected static String getBlockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }
}
