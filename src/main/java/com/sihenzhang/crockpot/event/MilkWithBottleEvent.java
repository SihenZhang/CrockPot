package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class MilkWithBottleEvent {
    @SubscribeEvent
    public static void onCowAndGoatInteract(PlayerInteractEvent.EntityInteract event) {
        var targetEntity = event.getTarget();
        if (targetEntity instanceof Cow || targetEntity instanceof Goat) {
            var stack = event.getItemStack();
            if (stack.is(Items.GLASS_BOTTLE) && !((Animal) targetEntity).isBaby()) {
                var player = event.getEntity();
                if (targetEntity instanceof Goat goat) {
                    player.playSound(goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_MILK : SoundEvents.GOAT_MILK, 1.0F, 1.0F);
                } else {
                    player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                }
                var filledResult = ItemUtils.createFilledResult(stack, player, ModItems.MILK_BOTTLE.get().getDefaultInstance(), false);
                player.setItemInHand(event.getHand(), filledResult);
                event.setCancellationResult(event.getSide().isClient() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
                event.setCanceled(true);
            }
        }
    }
}
