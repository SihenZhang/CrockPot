package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class MilkWithBottleEvent {
    @SubscribeEvent
    public static void onCowInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Cow cow) {
            Player player = event.getPlayer();
            ItemStack stack = event.getItemStack();
            if (stack.is(Items.GLASS_BOTTLE) && !cow.isBaby()) {
                player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                if (event.getSide().isServer()) {
                    ItemStack filledResult = ItemUtils.createFilledResult(stack, player, CrockPotRegistry.milkBottle.get().getDefaultInstance(), false);
                    player.setItemInHand(event.getHand(), filledResult);
                }
            }
        }
    }
}
