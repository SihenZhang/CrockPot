package com.sihenzhang.crockpot.block;

import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import com.sihenzhang.crockpot.block.entity.ModBlockEntities;
import com.sihenzhang.crockpot.entity.Birdcage;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.registry.FoodCategories;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;

public class BirdcageBlock extends BaseEntityBlock {
    public static final MapCodec<BirdcageBlock> CODEC = simpleCodec(BirdcageBlock::new);
    public static final VoxelShape LOWER_SHAPE_WITHOUT_BASE = Block.column(14.0D, 5.0D, 16.0D);
    public static final VoxelShape LOWER_SHAPE = Shapes.or(
            Block.column(8.0D, 0.0D, 2.0D),
            Block.column(3.0D, 2.0D, 5.0D),
            LOWER_SHAPE_WITHOUT_BASE
    );
    public static final VoxelShape UPPER_SHAPE_WITHOUT_CHAIN = Block.column(14.0D, 0.0D, 9.0D);
    public static final VoxelShape UPPER_SHAPE = Shapes.or(
            UPPER_SHAPE_WITHOUT_CHAIN,
            Block.column(3.0D, 9.0D, 13.0D)
    );
    public static final VoxelShape HANGING_UPPER_SHAPE = Shapes.or(
            UPPER_SHAPE_WITHOUT_CHAIN,
            Block.column(3.0D, 9.0D, 16.0D)
    );
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    public BirdcageBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(HANGING, false));
    }

    @Override
    protected MapCodec<BirdcageBlock> codec() {
        return CODEC;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        if (this.getBlockEntity(pLevel, pPos, pState) instanceof BirdcageBlockEntity birdcageBlockEntity) {
            var lowerPos = pState.getValue(HALF) == DoubleBlockHalf.LOWER ? pPos : pPos.below();
            var parrots = pLevel.getEntitiesOfClass(Parrot.class, new AABB(lowerPos.getX(), lowerPos.getY(), lowerPos.getZ(), lowerPos.getX() + 1.0D, lowerPos.getY() + 2.0D, lowerPos.getZ() + 1.0D));

            if (parrots.isEmpty()) {
                // no Parrot in the Birdcage, so put the Parrot into the Birdcage
                if (pPlayer instanceof ServerPlayer serverPlayer) {
                    var leftShoulderEntity = serverPlayer.getShoulderEntityLeft();
                    var rightShoulderEntity = serverPlayer.getShoulderEntityRight();
                    if (!leftShoulderEntity.isEmpty() || !rightShoulderEntity.isEmpty()) {
                        var isLeftShoulder = !leftShoulderEntity.isEmpty();
                        var optionalParrot = Optional.ofNullable(EntityType.loadEntityRecursive(isLeftShoulder ? leftShoulderEntity : rightShoulderEntity, pLevel, EntitySpawnReason.LOAD, entity -> entity))
                                .filter(Parrot.class::isInstance)
                                .map(Parrot.class::cast);
                        var optionalBirdcage = Optional.ofNullable(ModEntities.BIRDCAGE.get().create(pLevel, EntitySpawnReason.TRIGGERED));
                        if (optionalParrot.isPresent() && optionalBirdcage.isPresent()) {
                            var parrot = optionalParrot.get();
                            var birdcage = optionalBirdcage.get();
                            if (!pLevel.isClientSide() && birdcageBlockEntity.captureParrot(pLevel, lowerPos, serverPlayer, parrot, birdcage, isLeftShoulder)) {
                                return InteractionResult.SUCCESS;
                            }
                            return InteractionResult.CONSUME;
                        }
                    }
                }
            } else {
                var parrot = parrots.get(0);

                // if player is sneaking and its main hand is empty, release the Parrot
                if (pPlayer.isShiftKeyDown()) {
                    if (parrot.isOwnedBy(pPlayer)) {
                        if (!pLevel.isClientSide() && pPlayer instanceof ServerPlayer serverPlayer && parrot.setEntityOnShoulder(serverPlayer)) {
                            return InteractionResult.SUCCESS;
                        }
                    } else {
                        if (pPlayer instanceof ServerPlayer serverPlayer) {
                            serverPlayer.sendSystemMessage(I18nUtil.tooltip("birdcage.not_owner"), true);
                        } else {
                            pPlayer.sendSystemMessage(I18nUtil.tooltip("birdcage.not_owner"));
                        }
                    }
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (this.getBlockEntity(pLevel, pPos, pState) instanceof BirdcageBlockEntity birdcageBlockEntity) {
            var lowerPos = pState.getValue(HALF) == DoubleBlockHalf.LOWER ? pPos : pPos.below();
            var parrots = pLevel.getEntitiesOfClass(Parrot.class, new AABB(lowerPos.getX(), lowerPos.getY(), lowerPos.getZ(), lowerPos.getX() + 1.0D, lowerPos.getY() + 2.0D, lowerPos.getZ() + 1.0D));

            if (!parrots.isEmpty() && !birdcageBlockEntity.isOnCooldown()) {
                var parrot = parrots.get(0);
                var foodValues = FoodValuesDefinition.getFoodValues(pStack, pLevel);
                // if item in hand is Meat, Parrot will lay eggs
                var meatCategory = pLevel.registryAccess().getOrThrow(FoodCategories.MEAT);
                if (foodValues.has(meatCategory)) {
                    if (!pLevel.isClientSide() && birdcageBlockEntity.fedByMeat(pPlayer.getAbilities().instabuild ? pStack.copy() : pStack, foodValues, parrot)) {
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.CONSUME;
                }

                // if item in hand can be fed to Parrot, Parrot will eat it
                var optionalParrotFeedingRecipe = pLevel instanceof ServerLevel serverLevel
                        ? serverLevel.recipeAccess().getRecipeFor(ModRecipes.PARROT_FEEDING_RECIPE_TYPE.get(), new SingleRecipeInput(pStack), serverLevel)
                        : Optional.<net.minecraft.world.item.crafting.RecipeHolder<com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe>>empty();
                if (optionalParrotFeedingRecipe.isPresent()) {
                    if (!pLevel.isClientSide() && birdcageBlockEntity.fedByRecipe(pPlayer.getAbilities().instabuild ? pStack.copy() : pStack, optionalParrotFeedingRecipe.get().value(), parrot)) {
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
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
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel instanceof Level level && pState.hasProperty(HALF) && pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            level.getEntitiesOfClass(Birdcage.class, new AABB(pPos.getX(), pPos.getY(), pPos.getZ(), pPos.getX() + 1.0D, pPos.getY() + 2.0D, pPos.getZ() + 1.0D)).forEach(Birdcage::discard);
        }
        super.destroy(pLevel, pPos, pState);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected BlockState updateShape(BlockState pState, LevelReader pLevel, ScheduledTickAccess pTicks, BlockPos pCurrentPos, Direction pDirection, BlockPos pNeighborPos, BlockState pNeighborState, RandomSource pRandom) {
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
        return super.updateShape(pState, pLevel, pTicks, pCurrentPos, pDirection, pNeighborPos, pNeighborState, pRandom);
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
                    if (clickedPos.getY() > level.getMinY() && level.getBlockState(clickedPos.below()).canBeReplaced(pContext)) {
                        return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER).setValue(HANGING, upperBlockHangingValueWithSupport);
                    }
                } else {
                    if (clickedPos.getY() < level.getMaxY() - 1 && level.getBlockState(clickedPos.above()).canBeReplaced(pContext)) {
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
