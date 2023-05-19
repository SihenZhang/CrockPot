package com.sihenzhang.crockpot.capability;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.advancement.CrockPotCriteriaTriggers;
import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.network.PacketFoodCounter;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class FoodCounterCapabilityHandler {
    public static final Capability<IFoodCounter> FOOD_COUNTER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final ResourceLocation FOOD_COUNTER = RLUtils.createRL("food_counter");

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(IFoodCounter.class);
    }

    @SubscribeEvent
    public static void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(FOOD_COUNTER, new FoodCounterProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerAppear(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncFoodCounter(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        var player = event.getEntity();
        var oldPlayer = event.getOriginal();
        oldPlayer.revive();
        oldPlayer.getCapability(FOOD_COUNTER_CAPABILITY).ifPresent(oldFoodCounter -> player.getCapability(FOOD_COUNTER_CAPABILITY)
                .ifPresent(newFoodCounter -> newFoodCounter.deserializeNBT(oldFoodCounter.serializeNBT()))
        );
    }

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !event.getItem().isEdible()) {
            return;
        }
        player.getCapability(FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> {
            var stack = event.getItem();
            foodCounter.addFood(stack.getItem());
            CrockPotCriteriaTriggers.EAT_FOOD_TRIGGER.trigger(player, stack, foodCounter.getCount(stack.getItem()));
        });
        syncFoodCounter(player);
    }

    public static void syncFoodCounter(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> NetworkManager.sendToPlayer(serverPlayer, new PacketFoodCounter(foodCounter.serializeNBT())));
        }
    }
}
