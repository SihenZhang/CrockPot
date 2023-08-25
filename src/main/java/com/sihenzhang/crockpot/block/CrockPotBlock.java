package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntities;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CrockPotBlock extends BaseEntityBlock {
    private static final Random RAND = new Random();
    private static final String[] SUFFIXES = {"Pro", "Plus", "Max", "Ultra", "Premium", "Super"};
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    private long lastSysTime;
    private final Set<Integer> toPick = new HashSet<>();
    private final int potLevel;

    public CrockPotBlock(int potLevel) {
        super(Properties.of().requiresCorrectToolForDrops().strength(1.5F, 6.0F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0).noOcclusion());
        this.potLevel = potLevel;
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    public int getPotLevel() {
        return potLevel;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrockPotBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, CrockPotBlockEntities.CROCK_POT_BLOCK_ENTITY.get(), CrockPotBlockEntity::serverTick);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof CrockPotBlockEntity crockPotBlockEntity) {
                crockPotBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .ifPresent(itemHandler -> {
                            for (var i = 0; i < itemHandler.getSlots(); i++) {
                                var stack = itemHandler.getStackInSlot(i);
                                if (!stack.isEmpty()) {
                                    Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY() + 0.5, pPos.getZ(), stack);
                                }
                            }
                        });
                if (crockPotBlockEntity.isCooking()) {
                    Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY() + 0.5, pPos.getZ(), CrockPotItems.WET_GOOP.get().getDefaultInstance());
                }
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof CrockPotBlockEntity crockPotBlockEntity) {
            NetworkHooks.openScreen((ServerPlayer) pPlayer, crockPotBlockEntity, pPos);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            var xPos = pos.getX() + 0.5;
            var yPos = pos.getY() + 0.2;
            var zPos = pos.getZ() + 0.5;
            if (random.nextInt(10) == 0) {
                level.playLocalSound(xPos, yPos, zPos, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, random.nextFloat() + 0.5F, Mth.nextFloat(random, 0.6F, 1.3F), false);
            }
            var xOffset = Mth.nextDouble(random, -0.15, 0.15);
            var zOffset = Mth.nextDouble(random, -0.15, 0.15);
            level.addParticle(ParticleTypes.SMOKE, xPos + xOffset, yPos, zPos + zOffset, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.FLAME, xPos + xOffset, yPos, zPos + zOffset, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public MutableComponent getName() {
        if (potLevel > 0) {
            var sysTime = System.currentTimeMillis();
            if (lastSysTime + 5000 < sysTime) {
                lastSysTime = sysTime;
                toPick.clear();
                while (toPick.size() < potLevel) {
                    toPick.add(RAND.nextInt(SUFFIXES.length));
                }
            }
            var toPickSuffixes = toPick.stream().map(i -> Component.literal(SUFFIXES[i])).toArray();
            return Component.translatable(this.getDescriptionId(), toPickSuffixes);
        } else {
            return super.getName();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 0.8F;
    }
}
