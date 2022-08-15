package com.sihenzhang.crockpot.block.entity;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.entity.Birdcage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.Queue;

public class BirdcageBlockEntity extends BlockEntity {
    private static final int FED_COOLDOWN = 10;
    private static final int OUTPUT_COOLDOWN = 40;

    private int fedCooldown;
    private final Queue<Pair<ItemStack, Long>> outputBuffer = new ArrayDeque<>(4);

    public BirdcageBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(CrockPotRegistry.BIRDCAGE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, BirdcageBlockEntity pBlockEntity) {
        if (pBlockEntity.fedCooldown > 0) {
            pBlockEntity.fedCooldown--;
        }
        if (!pBlockEntity.outputBuffer.isEmpty() && pLevel.getGameTime() == pBlockEntity.outputBuffer.peek().getSecond()) {
            var output = pBlockEntity.outputBuffer.poll();
        }
    }

    public boolean isOnCooldown() {
        return fedCooldown > 0;
    }

    public boolean captureParrot(Level pLevel, BlockPos pPos, Player pPlayer, Parrot pParrot, Birdcage pBirdcage, boolean isLeftShoulder) {
        pParrot.setOwnerUUID(pPlayer.getUUID());
        pParrot.setPos(pPlayer.getX(), pPlayer.getY() + 0.7D, pPlayer.getZ());
        pLevel.addFreshEntity(pParrot);
        pBirdcage.setPos(pPos.getX() + 0.5D, pPos.getY() + 0.475D, pPos.getZ() + 0.5D);
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

    public boolean fedByMeat(ItemStack meat, FoodValues foodValues, Parrot parrot) {
        if (this.isOnCooldown()) {
            return false;
        }
        if (meat.isEmpty()) {
            return false;
        }
        if (!foodValues.has(FoodCategory.MONSTER) || level.random.nextBoolean()) {
            outputBuffer.offer(Pair.of(Items.EGG.getDefaultInstance(), level.getGameTime() + OUTPUT_COOLDOWN));
        }
        meat.shrink(1);
        level.playSound(null, parrot.getX(), parrot.getY(), parrot.getZ(), SoundEvents.GENERIC_EAT, parrot.getSoundSource(), 1.0F, 1.0F);
        for (var i = 0; i < 7; i++) {
            var xSpeed = parrot.getRandom().nextGaussian() * 0.02D;
            var ySpeed = parrot.getRandom().nextGaussian() * 0.02D;
            var zSpeed = parrot.getRandom().nextGaussian() * 0.02D;
            level.addParticle(ParticleTypes.SMOKE, parrot.getRandomX(1.0D), parrot.getRandomY() + 0.5D, parrot.getRandomZ(1.0D), xSpeed, ySpeed, zSpeed);
        }
        return true;
    }
}
