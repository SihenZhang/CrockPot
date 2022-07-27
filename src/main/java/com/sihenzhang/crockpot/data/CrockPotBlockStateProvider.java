package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import com.sihenzhang.crockpot.block.CornBlock;
import com.sihenzhang.crockpot.block.CrockPotUnknownCropsBlock;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;

public class CrockPotBlockStateProvider extends BlockStateProvider {
    public CrockPotBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(CrockPotRegistry.unknownCropsBlock.get(), this.models().crop("unknown_crops", RLUtils.createRL("block/unknown_crops")));
        this.customStageCropBlock(CrockPotRegistry.asparagusBlock.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotRegistry.cornBlock.get(), CornBlock.AGE, List.of());
        this.customStageCropBlock(CrockPotRegistry.eggplantBlock.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotRegistry.garlicBlock.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotRegistry.onionBlock.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotRegistry.pepperBlock.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotRegistry.tomatoBlock.get(), AbstractCrockPotCropBlock.AGE, List.of(0, 0, 1, 1, 2, 2, 2, 3));
    }

    public void customStageCropBlock(Block block, IntegerProperty ageProperty, List<Integer> ageSuffixes, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            var age = state.getValue(ageProperty);
            var stageName = getBlockName(block) + "_stage" + (ageSuffixes.isEmpty() ? age : ageSuffixes.get(Math.min(ageSuffixes.size(), age)));
            return ConfiguredModel.builder().modelFile(this.models().crop(stageName, RLUtils.createRL("block/" + stageName))).build();
        }, ignored);
    }

    protected static String getBlockName(Block block) {
        return block.getRegistryName().getPath();
    }
}
