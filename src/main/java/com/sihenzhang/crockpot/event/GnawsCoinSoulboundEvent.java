package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.util.ItemUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class GnawsCoinSoulboundEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerDrops(LivingDropsEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            if (player instanceof FakePlayer || player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                return;
            }
            Iterator<ItemEntity> iter = event.getDrops().iterator();
            while (iter.hasNext()) {
                ItemStack stack = iter.next().getItem();
                if (stack.getItem() == CrockPotRegistry.gnawsCoin) {
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
        PlayerEntity player = event.getPlayer();
        PlayerEntity oldPlayer = event.getOriginal();
        if (player instanceof FakePlayer || player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            return;
        }
        IItemHandler oldInventory = new PlayerMainInvWrapper(oldPlayer.inventory);
        for (int i = 0; i < oldInventory.getSlots(); i++) {
            ItemStack stack = oldInventory.getStackInSlot(i);
            if (stack.getItem() == CrockPotRegistry.gnawsCoin) {
                if (ItemUtils.giveItemToPlayer(player, stack.copy())) {
                    stack.setCount(0);
                }
            }
        }
    }
}
