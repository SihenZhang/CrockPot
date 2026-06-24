package com.sihenzhang.crockpot.block;

import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntities;
import com.sihenzhang.crockpot.block.entity.DryingRackBlockEntity;
import com.sihenzhang.crockpot.recipe.DryingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class DryingRackBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WALL = BooleanProperty.create("wall");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty STACKS = IntegerProperty.create("stacks", 1, 2);

    // Each north-facing shape is one cuboid spanning the model's overall bounds.
    // The slight overhangs are intentional and keep the thin rack easy to target.
    private static final Map<Direction, VoxelShape> FLOOR_SHAPES = makeRotatedShapes(
            Block.box(-0.25D, -0.25D, 5.0D, 16.25D, 16.25D, 11.0D));
    private static final Map<Direction, VoxelShape> FLOOR_SHAPES_2 = makeRotatedShapes(
            Block.box(-0.25D, -0.25D, 1.0D, 16.25D, 16.25D, 15.0D));
    private static final Map<Direction, VoxelShape> WALL_SHAPES = makeRotatedShapes(
            Block.box(0.0D, 6.75D, 14.5D, 16.0D, 16.25D, 17.5D));
    private static final Map<Direction, VoxelShape> WALL_SHAPES_2 = makeRotatedShapes(
            Block.box(0.0D, -1.25D, 14.5D, 16.0D, 16.25D, 17.5D));

    private static Map<Direction, VoxelShape> makeRotatedShapes(VoxelShape north) {
        var map = new EnumMap<Direction, VoxelShape>(Direction.class);
        map.put(Direction.NORTH, north);
        var east = rotateYClockwise(north);
        map.put(Direction.EAST, east);
        var south = rotateYClockwise(east);
        map.put(Direction.SOUTH, south);
        map.put(Direction.WEST, rotateYClockwise(south));
        return map;
    }

    private static VoxelShape rotateYClockwise(VoxelShape shape) {
        var holder = new VoxelShape[]{Shapes.empty()};
        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) ->
                holder[0] = Shapes.or(holder[0], Shapes.box(1.0D - z2, y1, x1, 1.0D - z1, y2, x2)));
        return holder[0];
    }

    public DryingRackBlock(BlockBehaviour.Properties properties) {
        super(properties.mapColor(MapColor.WOOD).strength(2.0F, 2.0F).sound(SoundType.WOOD).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WALL, false)
                .setValue(WATERLOGGED, false)
                .setValue(STACKS, 1));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return MapCodec.unit(this);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return interact(state, level, pos, player, InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return interact(state, level, pos, player, hand, stack);
    }

    private InteractionResult interact(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stackInHand) {
        if (!(level.getBlockEntity(pos) instanceof DryingRackBlockEntity dryingRack)) {
            return InteractionResult.PASS;
        }

        var recipe = stackInHand.isEmpty() ? java.util.Optional.<net.minecraft.world.item.crafting.RecipeHolder<DryingRecipe>>empty() : DryingRecipe.getRecipeFor(stackInHand, level);
        if (recipe.isPresent()) {
            if (!dryingRack.hasEmptySlot()) {
                return InteractionResult.CONSUME;
            }
            if (!level.isClientSide() && dryingRack.addItem(player.getAbilities().instabuild ? stackInHand.copy() : stackInHand, recipe.get().value())) {
                if (!player.getAbilities().instabuild) {
                    stackInHand.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.8F, 1.0F);
            }
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        if ((stackInHand.isEmpty() || recipe.isEmpty()) && dryingRack.hasReadyItems()) {
            if (!level.isClientSide() && dryingRack.collectReadyItems(player, hand)) {
                level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.8F, 1.0F);
            }
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (level instanceof Level realLevel && realLevel.getBlockEntity(pos) instanceof DryingRackBlockEntity dryingRack) {
            dryingRack.dropContents(realLevel, pos);
        }
        super.destroy(level, pos, state);
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (context.getItemInHand().is(this.asItem()) && state.is(this)) {
            return state.getValue(STACKS) < 2;
        }
        return super.canBeReplaced(state, context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var clickedState = level.getBlockState(pos);
        if (clickedState.is(this)) {
            return clickedState.cycle(STACKS);
        }
        var clickedFace = context.getClickedFace();
        var wall = clickedFace.getAxis().isHorizontal();
        var facing = wall ? clickedFace : context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WALL, wall)
                .setValue(WATERLOGGED, level.getFluidState(pos).is(Fluids.WATER));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var facing = state.getValue(FACING);
        var stacked = state.getValue(STACKS) == 2;
        if (state.getValue(WALL)) {
            return (stacked ? WALL_SHAPES_2 : WALL_SHAPES).get(facing);
        }
        return (stacked ? FLOOR_SHAPES_2 : FLOOR_SHAPES).get(facing);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WALL, WATERLOGGED, STACKS);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DryingRackBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, CrockPotBlockEntities.DRYING_RACK_BLOCK_ENTITY.get(), DryingRackBlockEntity::serverTick);
    }

    public static int getSlotCount(BlockState state) {
        return state.getValue(STACKS) * DryingRackBlockEntity.SLOTS_PER_STACK;
    }

    public static boolean canDry(Level level, BlockPos pos, BlockState state) {
        return !state.getValue(WATERLOGGED) && !(level instanceof ServerLevel serverLevel && serverLevel.isRainingAt(pos.above()));
    }
}
