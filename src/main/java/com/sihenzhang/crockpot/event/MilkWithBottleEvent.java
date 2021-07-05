package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class MilkWithBottleEvent {
    @SubscribeEvent
    public static void onCowInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof CowEntity) {
            CowEntity cow = (CowEntity) event.getTarget();
            PlayerEntity player = event.getPlayer();
            ItemStack stack = event.getItemStack();
            if (stack.getItem() == Items.GLASS_BOTTLE && !cow.isBaby()) {
                player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                if (event.getSide().isServer()) {
                    ItemStack filledResult = DrinkHelper.createFilledResult(stack, player, CrockPotRegistry.milkBottle.getDefaultInstance(), false);
                    player.setItemInHand(event.getHand(), filledResult);
                }
            }
        }
    }
}
