package com.sihenzhang.crockpot.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public abstract class AbstractDoubleCropBlock extends AbstractCropBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    private final Function<BlockState, VoxelShape> shapes = this.makeShapes();

    @Override
    public abstract MapCodec<? extends AbstractDoubleCropBlock> codec();

    protected AbstractDoubleCropBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    private Function<BlockState, VoxelShape> makeShapes() {
        return this.getShapeForEachState(state -> {
            var height = 4 + this.getAge(state) * 4;

            return switch (state.getValue(HALF)) {
                case LOWER -> Block.column(16.0, 0.0, Math.min(16, height));
                case UPPER -> Block.column(16.0, 0.0, Math.max(0, height - 16));
            };
        });
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shapes.apply(state);
    }

    protected int getDoubleCropAgeThreshold() {
        return 4;
    }

    protected boolean isLower(BlockState state) {
        return state.is(this) && state.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (this.getAge(state) >= this.getDoubleCropAgeThreshold()) {
            var half = state.getValue(HALF);
            if (directionToNeighbour.getAxis() != Direction.Axis.Y
                    || half == DoubleBlockHalf.LOWER != (directionToNeighbour == Direction.UP)
                    || neighbourState.is(this) && neighbourState.getValue(HALF) != half) {
                return half == DoubleBlockHalf.LOWER && directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)
                        ? Blocks.AIR.defaultBlockState()
                        : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
            } else {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (this.isLower(state)) {
            return super.canSurvive(state, level, pos);
        }
        return this.isLower(level.getBlockState(pos.below()));
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            if (player.preventsBlockDrops()) {
                DoublePlantBlock.preventDropFromBottomPart(level, pos, state, player);
            } else {
                dropResources(state, level, pos, null, player, player.getMainHandItem());
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack destroyedWith) {
        super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, destroyedWith);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return this.isLower(state) && super.isRandomlyTicking(state);
    }

    protected boolean canGrowInto(LevelReader level, BlockPos pos) {
        if (!level.isInsideBuildHeight(pos)) {
            return false;
        }
        var state = level.getBlockState(pos);
        return state.isAir() || state.is(this);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        if (level.getRawBrightness(pos, 0) >= 9 && !this.isMaxAge(state)) {
            var growthSpeed = getGrowthSpeed(state, level, pos);
            if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int) (25.0F / growthSpeed) + 1) == 0)) {
                var upperPos = pos.above();
                var updatedAge = this.getAge(state) + 1;
                if (updatedAge < this.getDoubleCropAgeThreshold() || this.canGrowInto(level, upperPos)) {
                    var newLowerState = state.setValue(AGE, updatedAge);
                    level.setBlock(pos, newLowerState, Block.UPDATE_CLIENTS);
                    if (updatedAge >= this.getDoubleCropAgeThreshold()) {
                        level.setBlockAndUpdate(upperPos, newLowerState.setValue(HALF, DoubleBlockHalf.UPPER));
                    }
                }
                CommonHooks.fireCropGrowPost(level, pos, state);
            }
        }
        super.randomTick(state, level, pos, random);
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        var lowerHalf = this.getLowerHalf(level, pos, state);
        if (lowerHalf == null) {
            return;
        }
        var lowerState = lowerHalf.state;
        var lowerPos = lowerHalf.pos;
        var upperPos = lowerPos.above();
        var updatedAge = Math.min(this.getMaxAge(), this.getAge(lowerState) + this.getBonemealAgeIncrease(level));
        if (updatedAge >= this.getDoubleCropAgeThreshold() && this.canGrowInto(level, upperPos)) {
            var newLowerState = lowerState.setValue(AGE, updatedAge);
            level.setBlock(lowerPos, newLowerState, Block.UPDATE_CLIENTS);
            level.setBlockAndUpdate(upperPos, newLowerState.setValue(HALF, DoubleBlockHalf.UPPER));
        } else {
            level.setBlock(lowerPos, lowerState.setValue(AGE, Math.min(this.getDoubleCropAgeThreshold() - 1, updatedAge)), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        var lowerHalf = this.getLowerHalf(level, pos, state);
        if (lowerHalf == null || this.isMaxAge(lowerHalf.state)) {
            return false;
        }
        return this.getAge(lowerHalf.state) < this.getDoubleCropAgeThreshold() - 1 || this.canGrowInto(level, lowerHalf.pos.above());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    private PosAndState getLowerHalf(LevelReader level, BlockPos pos, BlockState state) {
        if (this.isLower(state)) {
            return new PosAndState(pos, state);
        }
        var lowerPos = pos.below();
        var lowerState = level.getBlockState(lowerPos);
        return this.isLower(lowerState) ? new PosAndState(lowerPos, lowerState) : null;
    }

    private record PosAndState(BlockPos pos, BlockState state) {
    }
}
