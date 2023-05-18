package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;

public class CrockPotSeedsItem extends ItemNameBlockItem {
    public CrockPotSeedsItem(Block block) {
        super(block, new Properties().tab(CrockPot.ITEM_GROUP));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (pInteractionTarget instanceof Chicken chicken) {
            var age = chicken.getAge();
            if (!chicken.level.isClientSide && age == 0 && chicken.canFallInLove()) {
                if (!pPlayer.getAbilities().instabuild) {
                    pStack.shrink(1);
                }
                chicken.setInLove(pPlayer);
                chicken.gameEvent(GameEvent.ENTITY_INTERACT); // See Mob::interact(Player, InteractionHand)
                return InteractionResult.SUCCESS;
            }
            if (chicken.isBaby()) {
                if (!pPlayer.getAbilities().instabuild) {
                    pStack.shrink(1);
                }
                chicken.ageUp((int) ((float) (-age / 20) * 0.1F), true);
                chicken.gameEvent(GameEvent.ENTITY_INTERACT);
                return InteractionResult.sidedSuccess(chicken.level.isClientSide);
            }
            if (chicken.level.isClientSide) {
                return InteractionResult.CONSUME;
            }
        }
        if (pInteractionTarget instanceof Parrot parrot) {
            if (!parrot.isTame()) {
                var rand = parrot.getRandom();
                if (!pPlayer.getAbilities().instabuild) {
                    pStack.shrink(1);
                }
                if (!parrot.isSilent()) {
                    parrot.level.playSound(null, parrot.getX(), parrot.getY(), parrot.getZ(), SoundEvents.PARROT_EAT, parrot.getSoundSource(), 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
                }
                if (!parrot.level.isClientSide) {
                    if (rand.nextInt(10) == 0 && !ForgeEventFactory.onAnimalTame(parrot, pPlayer)) {
                        parrot.tame(pPlayer);
                        parrot.level.broadcastEntityEvent(parrot, EntityEvent.TAMING_SUCCEEDED);
                    } else {
                        parrot.level.broadcastEntityEvent(parrot, EntityEvent.TAMING_FAILED);
                    }
                }
                return InteractionResult.sidedSuccess(parrot.level.isClientSide);
            }
        }
        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }
}
