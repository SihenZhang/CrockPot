package com.sihenzhang.crockpot.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class BirdcageBlock extends BaseEntityBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    public BirdcageBlock() {
        super(Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.LANTERN).noOcclusion());
        this.registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(HANGING, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF, HANGING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return null;
    }
}
