package com.sihenzhang.crockpot.block.entity;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.registry.FoodCategories;
import com.sihenzhang.crockpot.core.FoodValues;
import com.sihenzhang.crockpot.entity.Birdcage;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class BirdcageBlockEntity extends BlockEntity {
    private static final int FED_COOLDOWN = 10;
    public static final int OUTPUT_COOLDOWN = 40;

    private int fedCooldown;
    private final Queue<Pair<ItemStack, Long>> outputBuffer = new ArrayDeque<>(4);

    public BirdcageBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(CrockPotBlockEntities.BIRDCAGE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
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

    public Queue<Pair<ItemStack, Long>> getOutputBuffer() {
        return outputBuffer;
    }

    public boolean captureParrot(Level pLevel, BlockPos pPos, Player pPlayer, Parrot pParrot, Birdcage pBirdcage, boolean isLeftShoulder) {
        if (!(pPlayer instanceof ServerPlayer serverPlayer)) {
            return false;
        }
        pParrot.setOwner(serverPlayer);
        pParrot.setPos(pPlayer.getX(), pPlayer.getY() + 0.7D, pPlayer.getZ());
        pLevel.addFreshEntity(pParrot);
        pBirdcage.setPos(pPos.getX() + 0.5D, pPos.getY() + 0.475D, pPos.getZ() + 0.5D);
        pLevel.addFreshEntity(pBirdcage);
        if (!pParrot.startRiding(pBirdcage, true, true)) {
            pParrot.discard();
            pBirdcage.discard();
            return false;
        }
        clearShoulderParrot(serverPlayer, isLeftShoulder);
        return true;
    }

    private static void clearShoulderParrot(ServerPlayer player, boolean isLeftShoulder) {
        var shoulderTag = isLeftShoulder ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
        shoulderTag.keySet().stream().toList().forEach(shoulderTag::remove);
        if (isLeftShoulder) {
            player.setShoulderParrotLeft(Optional.empty());
        } else {
            player.setShoulderParrotRight(Optional.empty());
        }
    }

    public boolean fedByMeat(ItemStack meat, FoodValues foodValues, Parrot parrot) {
        if (this.isOnCooldown()) {
            return false;
        }
        if (meat.isEmpty()) {
            return false;
        }
        var isMonsterFood = foodValues.has(level.registryAccess().getOrThrow(FoodCategories.MONSTER));
        if (!isMonsterFood || level.getRandom().nextBoolean()) {
            var parrotEgg = ModItems.PARROT_EGGS.get(parrot.getVariant()).get().getDefaultInstance();
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
        var result = recipe.assemble(new SingleRecipeInput(input));
        if (!result.isEmpty()) {
            outputBuffer.offer(Pair.of(result, level.getGameTime() + OUTPUT_COOLDOWN));
        }
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
