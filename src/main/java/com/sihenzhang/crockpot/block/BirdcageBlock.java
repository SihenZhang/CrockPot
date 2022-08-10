package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import com.sihenzhang.crockpot.entity.Birdcage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BirdcageBlock extends BaseEntityBlock {
    public static final VoxelShape LOWER_SHAPE = Shapes.or(
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(4.0D, 2.0D, 4.0D, 12.0D, 7.0D, 12.0D),
            Shapes.join(
                    Block.box(0.0D, 7.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                    Block.box(1.0D, 8.0D, 1.0D, 15.0D, 15.0D, 15.0D),
                    BooleanOp.ONLY_FIRST
            )
    );
    public static final VoxelShape UPPER_SHAPE = Shapes.join(
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D),
            BooleanOp.ONLY_FIRST
    );
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    public BirdcageBlock() {
        super(Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.LANTERN).noOcclusion());
        this.registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(HANGING, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var lowerPos = pState.getValue(HALF) == DoubleBlockHalf.LOWER ? pPos : pPos.below();
        var birdcageEntities = pLevel.getEntitiesOfClass(Birdcage.class, new AABB(lowerPos.getX(), lowerPos.getY(), lowerPos.getZ(), lowerPos.getX() + 1.0D, lowerPos.getY() + 2.0D, lowerPos.getZ() + 1.0D));
        if (birdcageEntities.isEmpty()) {
            // no BirdcageEntity in the cage block, so create one to capture the parrot
            var leftShoulderEntity = pPlayer.getShoulderEntityLeft();
            var rightShoulderEntity = pPlayer.getShoulderEntityRight();
            if (!leftShoulderEntity.isEmpty() || !rightShoulderEntity.isEmpty()) {
                var isLeftShoulder = !leftShoulderEntity.isEmpty();
                var optionalParrot = EntityType.create(isLeftShoulder ? leftShoulderEntity : rightShoulderEntity, pLevel).filter(entity -> entity instanceof Parrot).map(Parrot.class::cast);
                var optionalBirdcage = Optional.ofNullable(CrockPotRegistry.BIRDCAGE_ENTITY.get().create(pLevel));
                if (optionalParrot.isPresent() && optionalBirdcage.isPresent()) {
                    var parrot = optionalParrot.get();
                    var birdcage = optionalBirdcage.get();
                    if (!pLevel.isClientSide() && captureParrotIntoBirdcage(pLevel, lowerPos, pPlayer, parrot, birdcage, isLeftShoulder)) {
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.CONSUME;
                }
            }
        } else {
            var parrotEntities = pLevel.getEntitiesOfClass(Parrot.class, new AABB(lowerPos.getX(), lowerPos.getY(), lowerPos.getZ(), lowerPos.getX() + 1.0D, lowerPos.getY() + 2.0D, lowerPos.getZ() + 1.0D));
            if (!parrotEntities.isEmpty()) {
                for (var parrotEntity : parrotEntities) {
                    if (pPlayer.getUUID().equals(parrotEntity.getOwnerUUID())) {

//                        parrotEntity.func_213439_d((ServerPlayerEntity) player);
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static boolean captureParrotIntoBirdcage(Level pLevel, BlockPos pPos, Player pPlayer, Parrot pParrot, Birdcage pBirdcage, boolean isLeftShoulder) {
        pParrot.setOwnerUUID(pPlayer.getUUID());
        pParrot.setPos(pPlayer.getX(), pPlayer.getY() + 0.7D, pPlayer.getZ());
        pLevel.addFreshEntity(pParrot);
        pBirdcage.setPos(pPos.getX() + 0.5D, pPos.getY() + 1.0D, pPos.getZ() + 0.5D);
        pLevel.addFreshEntity(pBirdcage);
        if (!pParrot.startRiding(pBirdcage, true)) {
            pParrot.discard();
            pBirdcage.discard();
            return false;
        }
        if (isLeftShoulder) {
            pPlayer.setShoulderEntityLeft(new CompoundTag());
        } else {
            pPlayer.setShoulderEntityRight(new CompoundTag());
        }
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pDirection == getNeighborDirection(pState)) {
            return pNeighborState.is(this) && pNeighborState.getValue(HALF) != pState.getValue(HALF) ? pState : Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    private static Direction getNeighborDirection(BlockState pState) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        boolean isHanging = pContext.getClickedFace() == Direction.DOWN;
        var state = this.defaultBlockState().setValue(HANGING, isHanging);
        var level = pContext.getLevel();
        var clickedPos = pContext.getClickedPos();
        if (isHanging) {
            if (clickedPos.getY() > level.getMinBuildHeight() + 1 && level.getBlockState(clickedPos.below()).canBeReplaced(pContext)) {
                return state.setValue(HALF, DoubleBlockHalf.UPPER);
            }
        } else {
            if (clickedPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(clickedPos.above()).canBeReplaced(pContext)) {
                return state.setValue(HALF, DoubleBlockHalf.LOWER);
            }
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        var isHanging = pState.hasProperty(HANGING) && pState.getValue(HANGING);
        pLevel.setBlockAndUpdate(isHanging ? pPos.below() : pPos.above(), pState.setValue(HALF, isHanging ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER));
    }

//    @Override
//    @SuppressWarnings("deprecation")
//    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
//        return pState.getValue(HALF) != DoubleBlockHalf.UPPER || pLevel.getBlockState(pPos.below()).is(this);
//    }

//    @Override
//    @SuppressWarnings("deprecation")
//    public PushReaction getPistonPushReaction(BlockState pState) {
//        return PushReaction.BLOCK;
//    }
//
//    @Override
//    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
//        if (captureParrot(state, worldIn, pos, player)) {
//            return ActionResultType.CONSUME;
//        } else if (releaseParrot(worldIn, pos, player)) {
//            return ActionResultType.CONSUME;
//        }
//        if (!worldIn.isRemote) {
//            ItemStack itemStack = player.getHeldItem(handIn);
//            if (CrockPot.FOOD_CATEGORY_MANAGER.valuesOf(itemStack.getItem()).getOrDefault(FoodCategory.MEAT, 0.0F) > 0) {
//                if (!player.abilities.isCreativeMode) {
//                    itemStack.shrink(1);
//                }
//                spawnAsEntity(worldIn, pos, CrockPotRegistry.birdEgg.getDefaultInstance());
//            }
//        }
//        return ActionResultType.func_233537_a_(worldIn.isRemote);
//    }
//
//    public static boolean captureParrot(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
//        BlockPos lowerPos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();
//        List<BirdcageEntity> birdcageEntities = worldIn.getEntitiesWithinAABB(BirdcageEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 2.0, pos.getZ() + 1.0));
//        if (birdcageEntities.isEmpty()) {
//            return captureParrotOnShoulder(worldIn, lowerPos, player, true) || captureParrotOnShoulder(worldIn, lowerPos, player, false);
//        }
//        return false;
//    }
//
//    public static boolean captureParrotOnShoulder(World worldIn, BlockPos pos, PlayerEntity player, boolean isLeftShoulder) {
//        CompoundNBT shoulderEntity = isLeftShoulder ? player.getLeftShoulderEntity() : player.getRightShoulderEntity();
//        if (!worldIn.isRemote && !shoulderEntity.isEmpty()) {
//            Optional<Entity> entity = EntityType.loadEntityUnchecked(shoulderEntity, worldIn);
//            if (entity.isPresent() && entity.get() instanceof ParrotEntity) {
//                ParrotEntity parrotEntity = (ParrotEntity) entity.get();
//                parrotEntity.setOwnerId(player.getUniqueID());
//                parrotEntity.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
//                BirdcageEntity birdcageEntity = new BirdcageEntity(worldIn, pos);
//                worldIn.addEntity(parrotEntity);
//                if (isLeftShoulder) {
//                    player.setLeftShoulderEntity(new CompoundNBT());
//                } else {
//                    player.setRightShoulderEntity(new CompoundNBT());
//                }
//                worldIn.addEntity(birdcageEntity);
//                if (!parrotEntity.startRiding(birdcageEntity, true)) {
//                    birdcageEntity.remove();
//                    // func_213439_d: setEntityOnShoulder
//                    parrotEntity.func_213439_d((ServerPlayerEntity) player);
//                    return false;
//                }
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean releaseParrot(World worldIn, BlockPos pos, PlayerEntity player) {
//        if (!worldIn.isRemote && player.isSneaking()) {
//            List<ParrotEntity> parrotEntities = worldIn.getEntitiesWithinAABB(ParrotEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 2.0, pos.getZ() + 1.0));
//            if (!parrotEntities.isEmpty()) {
//                for (ParrotEntity parrotEntity : parrotEntities) {
//                    if (player.getUniqueID().equals(parrotEntity.getOwnerId())) {
//                        // func_213439_d: setEntityOnShoulder
//                        parrotEntity.func_213439_d((ServerPlayerEntity) player);
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }


    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
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

    public static BlockEntity getBlockEntity(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return pLevel.getBlockEntity(pState.getValue(HALF) == DoubleBlockHalf.LOWER ? pPos : pPos.below());
    }
}
