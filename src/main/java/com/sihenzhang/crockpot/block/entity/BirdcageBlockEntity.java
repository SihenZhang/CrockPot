package com.sihenzhang.crockpot.block.entity;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.entity.Birdcage;
import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
        if (pBlockEntity.isOnCooldown()) {
            pBlockEntity.fedCooldown--;
        }
        while (!pBlockEntity.outputBuffer.isEmpty() && pBlockEntity.outputBuffer.peek().getSecond() < pLevel.getGameTime()) {
            var output = pBlockEntity.outputBuffer.poll().getFirst();
            Containers.dropContents(pLevel, pPos, new SimpleContainer(output));
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
        var isMonsterFood = foodValues.has(FoodCategory.MONSTER);
        if (!isMonsterFood || level.random.nextBoolean()) {
            var parrotEgg = CrockPotRegistry.PARROT_EGGS.get(parrot.getVariant()).get().getDefaultInstance();
            outputBuffer.offer(Pair.of(parrotEgg, level.getGameTime() + OUTPUT_COOLDOWN));
        }
        meat.shrink(1);
        fedCooldown = FED_COOLDOWN;
        if (!parrot.isSilent()) {
            level.playSound(null, parrot.getX(), parrot.getY(), parrot.getZ(), SoundEvents.GENERIC_EAT, parrot.getSoundSource(), 1.0F, isMonsterFood ? 0.75F : 1.25F);
        }
        // Spawn Smoke Particles
        level.broadcastEntityEvent(parrot, EntityEvent.TAMING_FAILED);
        return true;
    }

    public boolean fedByRecipe(ItemStack input, ParrotFeedingRecipe recipe, Parrot parrot) {
        if (this.isOnCooldown()) {
            return false;
        }
        if (input.isEmpty()) {
            return false;
        }
        var result = recipe.assemble(new SimpleContainer(input));
        if (result.isEmpty()) {
            return false;
        }
        outputBuffer.offer(Pair.of(result, level.getGameTime() + OUTPUT_COOLDOWN));
        input.shrink(1);
        fedCooldown = FED_COOLDOWN;
        if (!parrot.isSilent()) {
            level.playSound(null, parrot.getX(), parrot.getY(), parrot.getZ(), SoundEvents.PARROT_EAT, parrot.getSoundSource(), 1.0F, 1.0F);
        }
        // Spawn Smoke Particles
        level.broadcastEntityEvent(parrot, EntityEvent.TAMING_SUCCEEDED);
        return true;
    }
}
