package com.sihenzhang.crockpot.block;

import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import com.sihenzhang.crockpot.block.entity.ModBlockEntities;
import com.sihenzhang.crockpot.entity.Birdcage;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;

public class BirdcageBlock extends BaseEntityBlock {
    public static final MapCodec<BirdcageBlock> CODEC = simpleCodec(BirdcageBlock::new);
    public static final VoxelShape LOWER_SHAPE_WITHOUT_BASE = Block.box(1.0D, 5.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    public static final VoxelShape LOWER_SHAPE = Shapes.or(
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D),
            Block.box(6.5D, 2.0D, 6.5D, 9.5D, 5.0D, 9.5D),
            LOWER_SHAPE_WITHOUT_BASE
    );
    public static final VoxelShape UPPER_SHAPE_WITHOUT_CHAIN = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 9.0D, 15.0D);
    public static final VoxelShape UPPER_SHAPE = Shapes.or(
            UPPER_SHAPE_WITHOUT_CHAIN,
            Block.box(6.5D, 9.0D, 6.5D, 9.5D, 13.0D, 9.5D)
    );
    public static final VoxelShape HANGING_UPPER_SHAPE = Shapes.or(
            UPPER_SHAPE_WITHOUT_CHAIN,
            Block.box(6.5D, 9.0D, 6.5D, 9.5D, 16.0D, 9.5D)
    );
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    public BirdcageBlock() {
        this(Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.LANTERN).noOcclusion());
    }

    public BirdcageBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(HANGING, false));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (this.getBlockEntity(level, pos, state) instanceof BirdcageBlockEntity birdcageBlockEntity) {
            var parrot = this.findNearbyParrot(state, level, pos);
            if (parrot != null && !birdcageBlockEntity.isOnCooldown()) {
                var foodValues = FoodValuesDefinition.getFoodValues(stack, level);
                // if item in hand is Meat, Parrot will lay eggs
                if (foodValues.has(FoodCategory.MEAT)) {
                    if (!level.isClientSide() && birdcageBlockEntity.fedByMeat(player.getAbilities().instabuild ? stack.copy() : stack, foodValues, parrot)) {
                        return ItemInteractionResult.SUCCESS;
                    }
                    return ItemInteractionResult.CONSUME;
                }

                // if item in hand can be fed to Parrot, Parrot will eat it
                var optionalParrotFeedingRecipe = level.getRecipeManager().getRecipeFor(ModRecipes.PARROT_FEEDING_RECIPE_TYPE.get(), new SingleRecipeInput(stack), level);
                if (optionalParrotFeedingRecipe.isPresent()) {
                    if (!level.isClientSide() && birdcageBlockEntity.fedByRecipe(player.getAbilities().instabuild ? stack.copy() : stack, optionalParrotFeedingRecipe.get().value(), level.registryAccess(), parrot)) {
                        return ItemInteractionResult.SUCCESS;
                    }
                    return ItemInteractionResult.CONSUME;
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (this.getBlockEntity(level, pos, state) instanceof BirdcageBlockEntity birdcageBlockEntity) {
            var lowerPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            var nearbyParrot = this.findNearbyParrot(state, level, pos);
            if (nearbyParrot == null) {
                // no Parrot in the Birdcage, so put the Parrot into the Birdcage
                var leftShoulderEntity = player.getShoulderEntityLeft();
                var rightShoulderEntity = player.getShoulderEntityRight();
                if (!leftShoulderEntity.isEmpty() || !rightShoulderEntity.isEmpty()) {
                    var isLeftShoulder = !leftShoulderEntity.isEmpty();
                    var optionalParrot = EntityType.create(isLeftShoulder ? leftShoulderEntity : rightShoulderEntity, level).filter(Parrot.class::isInstance).map(Parrot.class::cast);
                    var optionalBirdcage = Optional.ofNullable(ModEntities.BIRDCAGE.get().create(level));
                    if (optionalParrot.isPresent() && optionalBirdcage.isPresent()) {
                        var parrot = optionalParrot.get();
                        var birdcage = optionalBirdcage.get();
                        if (!level.isClientSide() && birdcageBlockEntity.captureParrot(level, lowerPos, player, parrot, birdcage, isLeftShoulder)) {
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.CONSUME;
                    }
                }
            } else {
                // if player is sneaking and its main hand is empty, release the Parrot
                if (player.isShiftKeyDown()) {
                    if (player.getUUID().equals(nearbyParrot.getOwnerUUID())) {
                        if (!level.isClientSide() && nearbyParrot.setEntityOnShoulder((ServerPlayer) player)) {
                            return InteractionResult.SUCCESS;
                        }
                    } else {
                        player.displayClientMessage(I18nUtils.createTooltipComponent("birdcage.not_owner"), true);
                    }
                    return InteractionResult.CONSUME;
                }
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    private Parrot findNearbyParrot(BlockState state, Level level, BlockPos pos) {
        var lowerPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
        var parrots = level.getEntitiesOfClass(Parrot.class, new AABB(lowerPos.getX(), lowerPos.getY(), lowerPos.getZ(), lowerPos.getX() + 1.0D, lowerPos.getY() + 2.0D, lowerPos.getZ() + 1.0D));
        return parrots.isEmpty() ? null : parrots.getFirst();
    }

    @Override
    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide() && pPlayer.isCreative()) {
            if (pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                var lowerPos = pPos.below();
                var lowerState = pLevel.getBlockState(lowerPos);
                if (lowerState.is(pState.getBlock()) && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    pLevel.setBlock(lowerPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                    pLevel.levelEvent(pPlayer, LevelEvent.PARTICLES_DESTROY_BLOCK, lowerPos, Block.getId(lowerState));
                }
            }
        }
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pState.hasProperty(HALF) && pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                pLevel.getEntitiesOfClass(Birdcage.class, new AABB(pPos.getX(), pPos.getY(), pPos.getZ(), pPos.getX() + 1.0D, pPos.getY() + 2.0D, pPos.getZ() + 1.0D)).forEach(Birdcage::discard);
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pDirection == getConnectedDirection(pState)) {
            return pNeighborState.is(this) && pNeighborState.getValue(HALF) != pState.getValue(HALF) ? pState : Blocks.AIR.defaultBlockState();
        }
        if (pDirection.getAxis() == Direction.Axis.Y) {
            // if the block on the direction can support the birdcage, try to place it with connection
            var canSupport = Block.canSupportCenter(pLevel, pNeighborPos, pDirection.getOpposite());
            // the hanging value of the upper block with the connection is true
            var upperBlockHangingValueWithSupport = canSupport;
            // the hanging value of the lower block with the base is false
            var lowerBlockHangingValueWithSupport = !canSupport;
            if (pState.getValue(HALF) == DoubleBlockHalf.LOWER && pState.getValue(HANGING) != lowerBlockHangingValueWithSupport) {
                return pState.setValue(HANGING, lowerBlockHangingValueWithSupport);
            }
            if (pState.getValue(HALF) == DoubleBlockHalf.UPPER && pState.getValue(HANGING) != upperBlockHangingValueWithSupport) {
                return pState.setValue(HANGING, upperBlockHangingValueWithSupport);
            }
        }
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        var level = pContext.getLevel();
        var clickedPos = pContext.getClickedPos();
        for (var direction : pContext.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                // if the block on the direction can support the birdcage, try to place it with connection
                var canSupport = Block.canSupportCenter(level, clickedPos.relative(direction), direction.getOpposite());
                // the hanging value of the upper block with the connection is true
                var upperBlockHangingValueWithSupport = canSupport;
                // the hanging value of the lower block with the base is false
                var lowerBlockHangingValueWithSupport = !canSupport;
                if (direction == Direction.UP) {
                    if (clickedPos.getY() > level.getMinBuildHeight() && level.getBlockState(clickedPos.below()).canBeReplaced(pContext)) {
                        return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER).setValue(HANGING, upperBlockHangingValueWithSupport);
                    }
                } else {
                    if (clickedPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(clickedPos.above()).canBeReplaced(pContext)) {
                        return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER).setValue(HANGING, lowerBlockHangingValueWithSupport);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        var neighbourDirection = getConnectedDirection(pState);
        var neighbourPos = pPos.relative(neighbourDirection);
        var neighbourCanSupport = Block.canSupportCenter(pLevel, neighbourPos.relative(neighbourDirection), neighbourDirection.getOpposite());
        pLevel.setBlockAndUpdate(neighbourPos, pState.setValue(HALF, pState.getValue(HALF) == DoubleBlockHalf.LOWER ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER).setValue(HANGING, (neighbourDirection == Direction.UP) == neighbourCanSupport));
    }

    public static Direction getConnectedDirection(BlockState pState) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(HANGING)) {
            return pState.getValue(HALF) == DoubleBlockHalf.UPPER ? HANGING_UPPER_SHAPE : LOWER_SHAPE_WITHOUT_BASE;
        }
        return pState.getValue(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF, HANGING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? new BirdcageBlockEntity(pPos, pState) : null;
    }

    public BlockEntity getBlockEntity(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return pLevel.getBlockEntity(pState.getValue(HALF) == DoubleBlockHalf.LOWER ? pPos : pPos.below());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : createTickerHelper(pBlockEntityType, ModBlockEntities.BIRDCAGE_BLOCK_ENTITY.get(), BirdcageBlockEntity::serverTick);
    }
}
