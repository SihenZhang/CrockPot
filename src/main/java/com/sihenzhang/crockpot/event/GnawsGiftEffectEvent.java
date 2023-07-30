package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class GnawsGiftEffectEvent {
    @SubscribeEvent
    public static void onFoodRightClick(final PlayerInteractEvent.RightClickItem event) {
        var player = event.getEntity();
        if (player.hasEffect(CrockPotEffects.GNAWS_GIFT.get()) && event.getItemStack().isEdible()) {
            player.startUsingItem(event.getHand());
            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
        }
    }
}
