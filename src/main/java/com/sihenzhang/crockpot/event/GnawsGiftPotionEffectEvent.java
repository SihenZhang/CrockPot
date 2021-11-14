package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class GnawsGiftPotionEffectEvent {
    @SubscribeEvent
    public static void onFoodRightClick(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        if (player.hasEffect(CrockPotRegistry.gnawsGift) && event.getItemStack().isEdible()) {
            player.startUsingItem(event.getHand());
            event.setCancellationResult(ActionResultType.CONSUME);
            event.setCanceled(true);
        }
    }
}
