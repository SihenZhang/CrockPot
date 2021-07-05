package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Random;

public class CrockPotSeedsItem extends BlockNamedItem {
    public CrockPotSeedsItem(Block blockIn) {
        super(blockIn, new Properties().tab(CrockPot.ITEM_GROUP));
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target instanceof ChickenEntity) {
            ChickenEntity chicken = (ChickenEntity) target;
            int age = chicken.getAge();
            if (age == 0 && chicken.canBreed()) {
                if (!playerIn.abilities.instabuild) {
                    stack.shrink(1);
                }
                if (!chicken.level.isClientSide) {
                    chicken.setInLove(playerIn);
                }
                return ActionResultType.sidedSuccess(chicken.level.isClientSide);
            }
            if (chicken.isBaby()) {
                if (!playerIn.abilities.instabuild) {
                    stack.shrink(1);
                }
                chicken.ageUp((int) ((float) (-age / 20) * 0.1F), true);
                return ActionResultType.sidedSuccess(chicken.level.isClientSide);
            }
        }
        if (target instanceof ParrotEntity) {
            ParrotEntity parrot = (ParrotEntity) target;
            if (!parrot.isTame()) {
                if (!playerIn.abilities.instabuild) {
                    stack.shrink(1);
                }
                Random rand = parrot.getRandom();
                if (!parrot.isSilent()) {
                    parrot.level.playSound(null, parrot.getX(), parrot.getY(), parrot.getZ(), SoundEvents.PARROT_EAT, parrot.getSoundSource(), 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
                }
                if (!parrot.level.isClientSide) {
                    if (rand.nextInt(10) == 0 && !ForgeEventFactory.onAnimalTame(parrot, playerIn)) {
                        parrot.tame(playerIn);
                        parrot.level.broadcastEntityEvent(parrot, (byte) 7);
                    } else {
                        parrot.level.broadcastEntityEvent(parrot, (byte) 6);
                    }
                }
                return ActionResultType.sidedSuccess(parrot.level.isClientSide);
            }
        }
        return super.interactLivingEntity(stack, playerIn, target, hand);
    }
}
