package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public abstract class CrockPotBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public CrockPotBlock() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F).lightValue(13).notSolid());
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(LIT, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrockPotTileEntity();
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof CrockPotTileEntity && state.getBlock() != newState.getBlock()) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    .ifPresent(itemHandler -> {
                        for (int i = 0; i < itemHandler.getSlots(); i++) {
                            ItemStack stack = itemHandler.getStackInSlot(i);
                            if (!stack.isEmpty()) {
                                spawnAsEntity(worldIn, pos, stack);
                            }
                        }
                    });
            CrockPotTileEntity cast = (CrockPotTileEntity) tileEntity;
            if (cast.isProcessing()) {
                spawnAsEntity(worldIn, pos, new ItemStack(CrockPotRegistry.wetGoop.get()));
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && handIn == Hand.MAIN_HAND) {
            CrockPotTileEntity tileEntity = (CrockPotTileEntity) worldIn.getTileEntity(pos);
            NetworkHooks.openGui((ServerPlayerEntity) player, tileEntity, (packetBuffer -> {
                assert tileEntity != null;
                packetBuffer.writeBlockPos(tileEntity.getPos());
            }));
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(LIT) ? super.getLightValue(state) : 0;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double xPos = (double) pos.getX() + 0.5;
            double yPos = pos.getY() + 0.2;
            double zPos = (double) pos.getZ() + 0.5;
            if (rand.nextInt(10) == 0) {
                worldIn.playSound(xPos, yPos, zPos, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
            }
            if (this.getPotLevel() == 2) {
                Direction direction = stateIn.get(FACING);
                Direction.Axis directionAxis = direction.getAxis();
                double axisOffset = rand.nextDouble() * 0.3 - 0.15;
                double xOffset = directionAxis == Direction.Axis.X ? (double)direction.getXOffset() * 0.45 : axisOffset;
                double yOffset = rand.nextDouble() * 0.3 - 0.15;
                double zOffset = directionAxis == Direction.Axis.Z ? (double)direction.getZOffset() * 0.45 : axisOffset;
                worldIn.addParticle(ParticleTypes.ENCHANTED_HIT, xPos + xOffset, yPos + yOffset, zPos + zOffset, 0.0, 0.0, 0.0);
                worldIn.addParticle(ParticleTypes.ENCHANTED_HIT, xPos - xOffset, yPos + yOffset, zPos - zOffset, 0.0, 0.0, 0.0);
            } else {
                double xOffset = rand.nextDouble() * 0.3 - 0.15;
                double zOffset = rand.nextDouble() * 0.3 - 0.15;
                worldIn.addParticle(ParticleTypes.SMOKE, xPos + xOffset, yPos, zPos + zOffset, 0.0, 0.0, 0.0);
                worldIn.addParticle(ParticleTypes.FLAME, xPos + xOffset, yPos, zPos + zOffset, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0.8F;
    }

    public abstract int getPotLevel();
}
