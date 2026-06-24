package com.sihenzhang.crockpot.block;

import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntities;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CrockPotBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 16, 15);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    private final int potLevel;

    public CrockPotBlock(BlockBehaviour.Properties properties, int potLevel) {
        super(properties.requiresCorrectToolForDrops().strength(1.5F, 6.0F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0).noOcclusion());
        this.potLevel = potLevel;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false).setValue(OPEN, false));
    }

    public int getPotLevel() {
        return potLevel;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return MapCodec.unit(this);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return openCrockPot(pLevel, pPos, pPlayer);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        return openCrockPot(pLevel, pPos, pPlayer);
    }

    private InteractionResult openCrockPot(Level pLevel, BlockPos pPos, Player pPlayer) {
        if (!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof CrockPotBlockEntity crockPotBlockEntity) {
            ((ServerPlayer) pPlayer).openMenu(crockPotBlockEntity, buf -> buf.writeBlockPos(pPos));
        }
        return pLevel.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel instanceof Level level && level.getBlockEntity(pPos) instanceof CrockPotBlockEntity crockPotBlockEntity) {
            var itemHandler = crockPotBlockEntity.getItemHandler();
            for (var i = 0; i < itemHandler.size(); i++) {
                Containers.dropItemStack(level, pPos.getX(), pPos.getY(), pPos.getZ(), crockPotBlockEntity.getStackInSlot(i));
            }
            if (crockPotBlockEntity.isCooking()) {
                Containers.dropContents(level, pPos, new SimpleContainer(ModItems.WET_GOOP.get().getDefaultInstance()));
            }
        }
        super.destroy(pLevel, pPos, pState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof CrockPotBlockEntity crockPotBlockEntity) {
            crockPotBlockEntity.recheckOpen();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT)) {
            var xPos = pPos.getX() + 0.5;
            var yPos = pPos.getY() + 0.2;
            var zPos = pPos.getZ() + 0.5;
            if (pRandom.nextInt(10) == 0) {
                pLevel.playLocalSound(xPos, yPos, zPos, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, pRandom.nextFloat() + 0.5F, Mth.nextFloat(pRandom, 0.6F, 1.3F), false);
            }
            var xOffset = Mth.nextDouble(pRandom, -0.15, 0.15);
            var zOffset = Mth.nextDouble(pRandom, -0.15, 0.15);
            pLevel.addParticle(ParticleTypes.SMOKE, xPos + xOffset, yPos, zPos + zOffset, 0.0, 0.0, 0.0);
            pLevel.addParticle(ParticleTypes.FLAME, xPos + xOffset, yPos, zPos + zOffset, 0.0, 0.0, 0.0);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, LIT, OPEN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CrockPotBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : createTickerHelper(pBlockEntityType, CrockPotBlockEntities.CROCK_POT_BLOCK_ENTITY.get(), CrockPotBlockEntity::serverTick);
    }
}
