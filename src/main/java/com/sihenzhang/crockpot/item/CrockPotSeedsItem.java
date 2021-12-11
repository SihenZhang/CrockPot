package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Random;

public class CrockPotSeedsItem extends ItemNameBlockItem {
    public CrockPotSeedsItem(Block block) {
        super(block, new Properties().tab(CrockPot.ITEM_GROUP));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (interactionTarget instanceof Chicken chicken) {
            int age = chicken.getAge();
            if (age == 0 && chicken.canBreed()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (!chicken.level.isClientSide) {
                    chicken.setInLove(player);
                }
                return InteractionResult.sidedSuccess(chicken.level.isClientSide);
            }
            if (chicken.isBaby()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                chicken.ageUp((int) ((float) (-age / 20) * 0.1F), true);
                return InteractionResult.sidedSuccess(chicken.level.isClientSide);
            }
        }
        if (interactionTarget instanceof Parrot parrot) {
            if (!parrot.isTame()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                Random rand = parrot.getRandom();
                if (!parrot.isSilent()) {
                    parrot.level.playSound(null, parrot.getX(), parrot.getY(), parrot.getZ(), SoundEvents.PARROT_EAT, parrot.getSoundSource(), 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
                }
                if (!parrot.level.isClientSide) {
                    if (rand.nextInt(10) == 0 && !ForgeEventFactory.onAnimalTame(parrot, player)) {
                        parrot.tame(player);
                        parrot.level.broadcastEntityEvent(parrot, (byte) 7);
                    } else {
                        parrot.level.broadcastEntityEvent(parrot, (byte) 6);
                    }
                }
                return InteractionResult.sidedSuccess(parrot.level.isClientSide);
            }
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }
}
