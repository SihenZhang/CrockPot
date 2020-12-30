package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;

import java.util.Random;

public class CrockPotCropsBlockItem extends BlockNamedItem {
    private final boolean isSeed;

    public CrockPotCropsBlockItem(Block blockIn) {
        super(blockIn, new Properties().group(CrockPot.ITEM_GROUP));
        isSeed = true;
    }

    public CrockPotCropsBlockItem(Block blockIn, int hunger, float saturation) {
        super(blockIn, new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).build()));
        isSeed = false;
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (isSeed) {
            if (target instanceof ChickenEntity) {
                ChickenEntity chicken = (ChickenEntity) target;
                int age = chicken.getGrowingAge();
                if (age == 0 && chicken.canBreed()) {
                    if (!playerIn.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    if (!chicken.world.isRemote) {
                        chicken.setInLove(playerIn);
                    }
                    return ActionResultType.func_233537_a_(chicken.world.isRemote);
                }
                if (chicken.isChild()) {
                    if (!playerIn.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    chicken.ageUp((int) ((float) (-age / 20) * 0.1F), true);
                    return ActionResultType.func_233537_a_(chicken.world.isRemote);
                }
            }
            if (target instanceof ParrotEntity) {
                ParrotEntity parrot = (ParrotEntity) target;
                if (!parrot.isTamed()) {
                    if (!playerIn.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    Random rand = new Random();
                    if (!parrot.isSilent()) {
                        parrot.world.playSound(null, parrot.getPosX(), parrot.getPosY(), parrot.getPosZ(), SoundEvents.ENTITY_PARROT_EAT, parrot.getSoundCategory(), 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
                    }
                    if (!parrot.world.isRemote) {
                        if (rand.nextInt(10) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(parrot, playerIn)) {
                            parrot.setTamedBy(playerIn);
                            parrot.world.setEntityState(parrot, (byte) 7);
                        } else {
                            parrot.world.setEntityState(parrot, (byte) 6);
                        }
                    }
                    return ActionResultType.func_233537_a_(parrot.world.isRemote);
                }
            }
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
