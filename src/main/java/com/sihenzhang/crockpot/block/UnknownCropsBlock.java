package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.mixin.CropBlockAccessor;
import com.sihenzhang.crockpot.tag.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

public class UnknownCropsBlock extends AbstractCropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    private static final VoxelShape[] SHAPES = Block.boxes(1, (ignored) -> Block.column(16.0, 0.0, 2.0));

    public UnknownCropsBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[this.getAge(state)];
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 1;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.UNKNOWN_SEEDS.get();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos, 0) < 9) {
            return;
        }
        var growthSpeed = getGrowthSpeed(state, level, pos);
        if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int) (25.0F / growthSpeed) + 1) == 0)) {
            BuiltInRegistries.BLOCK.getRandomElementOf(ModBlockTags.UNKNOWN_CROPS, random)
                    .ifPresent(block -> {
                        level.setBlock(pos, block.value().defaultBlockState(), Block.UPDATE_CLIENTS);
                        CommonHooks.fireCropGrowPost(level, pos, state);
                    });
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        BuiltInRegistries.BLOCK.getRandomElementOf(ModBlockTags.UNKNOWN_CROPS, level.getRandom())
                .map(Holder::value)
                .ifPresent(block -> {
                    switch (block) {
                        case AbstractDoubleCropBlock cropBlock -> {
                            var age = Math.max(0, ((CropBlockAccessor) cropBlock).invokeGetBonemealAgeIncrease(level) - 1);
                            var upperPos = pos.above();
                            if (age >= cropBlock.getDoubleCropAgeThreshold() && cropBlock.canGrowInto(level, upperPos)) {
                                var stateForAge = cropBlock.getStateForAge(age);
                                level.setBlock(pos, stateForAge.setValue(AbstractDoubleCropBlock.HALF, DoubleBlockHalf.LOWER), Block.UPDATE_CLIENTS);
                                level.setBlockAndUpdate(upperPos, stateForAge.setValue(AbstractDoubleCropBlock.HALF, DoubleBlockHalf.UPPER));
                            } else {
                                level.setBlock(pos, cropBlock.getStateForAge(Math.min(cropBlock.getDoubleCropAgeThreshold() - 1, age)), Block.UPDATE_CLIENTS);
                            }
                        }
                        case CropBlock cropBlock -> {
                            var age = Math.max(0, ((CropBlockAccessor) cropBlock).invokeGetBonemealAgeIncrease(level) - 1);
                            level.setBlock(pos, cropBlock.getStateForAge(Math.min(age, cropBlock.getMaxAge())), Block.UPDATE_CLIENTS);
                        }
                        default -> level.setBlock(pos, block.defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
