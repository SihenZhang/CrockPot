package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.util.ItemUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

import java.util.Iterator;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class GnawsCoinSoulboundEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerDrops(LivingDropsEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player player) {
            if (player instanceof FakePlayer || player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                return;
            }
            Iterator<ItemEntity> iter = event.getDrops().iterator();
            while (iter.hasNext()) {
                ItemStack stack = iter.next().getItem();
                if (stack.is(ModItems.GNAWS_COIN.get())) {
                    if (ItemUtils.giveItemToPlayer(player, stack)) {
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
        if (player instanceof FakePlayer || player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            return;
        }
        IItemHandler oldInventory = new PlayerMainInvWrapper(oldPlayer.getInventory());
        for (int i = 0; i < oldInventory.getSlots(); i++) {
            ItemStack stack = oldInventory.getStackInSlot(i);
            if (stack.is(ModItems.GNAWS_COIN.get())) {
                if (ItemUtils.giveItemToPlayer(player, stack.copy())) {
                    stack.setCount(0);
                }
            }
        }
    }
}
