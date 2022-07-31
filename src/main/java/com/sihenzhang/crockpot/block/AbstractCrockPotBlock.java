package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class AbstractCrockPotBlock extends BaseEntityBlock {
    private final Random rand = new Random();
    private long lastSysTime;
    private final Set<Integer> toPick = new HashSet<>();
    private final String[] suffixes = {"Pro", "Plus", "Max", "Ultra", "Premium", "Super"};

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public AbstractCrockPotBlock() {
        super(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0).noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrockPotBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, CrockPotRegistry.CROCK_POT_BLOCK_ENTITY.get(), CrockPotBlockEntity::serverTick);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CrockPotBlockEntity crockPotBlockEntity) {
                if (!level.isClientSide) {
                    blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                            .ifPresent(itemHandler -> {
                                for (int i = 0; i < itemHandler.getSlots(); i++) {
                                    ItemStack stack = itemHandler.getStackInSlot(i);
                                    if (!stack.isEmpty()) {
                                        popResource(level, pos, stack);
                                    }
                                }
                            });
                    if (crockPotBlockEntity.isCooking()) {
                        popResource(level, pos, CrockPotRegistry.WET_GOOP.get().getDefaultInstance());
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CrockPotBlockEntity) {
                NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) blockEntity, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (state.getValue(LIT)) {
            double xPos = (double) pos.getX() + 0.5;
            double yPos = (double) pos.getY() + 0.2;
            double zPos = (double) pos.getZ() + 0.5;
            if (random.nextInt(10) == 0) {
                level.playLocalSound(xPos, yPos, zPos, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, random.nextFloat() + 0.5F, Mth.nextFloat(random, 0.6F, 1.3F), false);
            }
            if (this.getPotLevel() == 2) {
                Direction direction = state.getValue(FACING);
                Direction.Axis directionAxis = direction.getAxis();
                double axisOffset = Mth.nextDouble(random, -0.15, 0.15);
                double xOffset = directionAxis == Direction.Axis.X ? (double) direction.getStepX() * 0.45 : axisOffset;
                double yOffset = Mth.nextDouble(random, -0.15, 0.15);
                double zOffset = directionAxis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.45 : axisOffset;
                level.addParticle(ParticleTypes.ENCHANTED_HIT, xPos + xOffset, yPos + yOffset, zPos + zOffset, 0.0, 0.0, 0.0);
                level.addParticle(ParticleTypes.ENCHANTED_HIT, xPos - xOffset, yPos + yOffset, zPos - zOffset, 0.0, 0.0, 0.0);
            } else {
                double xOffset = Mth.nextDouble(random, -0.15, 0.15);
                double zOffset = Mth.nextDouble(random, -0.15, 0.15);
                level.addParticle(ParticleTypes.SMOKE, xPos + xOffset, yPos, zPos + zOffset, 0.0, 0.0, 0.0);
                level.addParticle(ParticleTypes.FLAME, xPos + xOffset, yPos, zPos + zOffset, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 0.8F;
    }

    @Override
    public MutableComponent getName() {
        int potLevel = this.getPotLevel();
        if (potLevel > 0) {
            long sysTime = System.currentTimeMillis();
            if (this.lastSysTime + 5000 < sysTime) {
                this.lastSysTime = sysTime;
                this.toPick.clear();
                while (this.toPick.size() < potLevel) {
                    this.toPick.add(this.rand.nextInt(this.suffixes.length));
                }
            }
            Component[] toPickSuffixes = this.toPick.stream().map(i -> new TextComponent(suffixes[i])).toArray(Component[]::new);
            return new TranslatableComponent(this.getDescriptionId(), (Object[]) toPickSuffixes);
        } else {
            return super.getName();
        }
    }

    public abstract int getPotLevel();
}
