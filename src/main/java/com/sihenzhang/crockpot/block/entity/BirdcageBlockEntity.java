package com.sihenzhang.crockpot.block.entity;

import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.entity.Birdcage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BirdcageBlockEntity extends BlockEntity {
    private static final int FED_COOLDOWN = 10;
    private static final int LAYING_EGGS_COOLDOWN = 40;
    private static final int DROPPING_SEEDS_COOLDOWN = 40;

    private int fedCooldown;

    public BirdcageBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(CrockPotRegistry.BIRDCAGE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, BirdcageBlockEntity pBlockEntity) {
        if (pBlockEntity.fedCooldown > 0) {
            pBlockEntity.fedCooldown--;
        }
    }

    public boolean hasCooldown() {
        return fedCooldown <= 0;
    }

    public boolean captureParrot(Level pLevel, BlockPos pPos, Player pPlayer, Parrot pParrot, Birdcage pBirdcage, boolean isLeftShoulder) {
        pParrot.setOwnerUUID(pPlayer.getUUID());
        pParrot.setPos(pPlayer.getX(), pPlayer.getY() + 0.7D, pPlayer.getZ());
        pLevel.addFreshEntity(pParrot);
        pBirdcage.setPos(pPos.getX() + 0.5D, pPos.getY() + 0.8D, pPos.getZ() + 0.5D);
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

    public boolean fedByMeat(ItemStack stack) {
        return false;
    }
}
