package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Iterator;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class GnawsCoinSoulboundEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerDrops(LivingDropsEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player player) {
            if (player instanceof FakePlayer || shouldKeepInventory(player)) {
                return;
            }
            Iterator<ItemEntity> iter = event.getDrops().iterator();
            while (iter.hasNext()) {
                ItemStack stack = iter.next().getItem();
                if (stack.is(ModItems.GNAWS_COIN.get())) {
                    if (player.getInventory().add(stack)) {
                        iter.remove();
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        Player player = event.getEntity();
        Player oldPlayer = event.getOriginal();
        if (player instanceof FakePlayer || shouldKeepInventory(player)) {
            return;
        }
        for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
            ItemStack stack = oldPlayer.getInventory().getItem(i);
            if (stack.is(ModItems.GNAWS_COIN.get())) {
                ItemStack remaining = stack.copy();
                if (player.getInventory().add(remaining)) {
                    stack.setCount(0);
                } else {
                    stack.setCount(remaining.getCount());
                }
            }
        }
    }

    private static boolean shouldKeepInventory(Player player) {
        return player.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(GameRules.KEEP_INVENTORY);
    }
}
