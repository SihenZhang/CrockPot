package com.sihenzhang.crockpot.capability;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.advancement.CrockPotCriteriaTriggers;
import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.network.PacketFoodCounter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class FoodCounterCapabilityHandler {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CrockPot.MOD_ID);
    public static final Supplier<AttachmentType<IFoodCounter>> FOOD_COUNTER = ATTACHMENT_TYPES.register("food_counter",
            () -> AttachmentType.<IFoodCounter>builder(FoodCounter::new).serialize(new FoodCounterProvider()).copyOnDeath().build());

    public static IFoodCounter getFoodCounter(Player player) {
        return player.getData(FOOD_COUNTER);
    }

    @SubscribeEvent
    public static void onPlayerAppear(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncFoodCounter(player);
        }
    }

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !event.getItem().has(DataComponents.FOOD)) {
            return;
        }
        var foodCounter = getFoodCounter(player);
        var stack = event.getItem();
        foodCounter.addFood(stack.getItem());
        CrockPotCriteriaTriggers.EAT_FOOD_TRIGGER.get().trigger(player, stack, foodCounter.getCount(stack.getItem()));
        syncFoodCounter(player);
    }

    public static void syncFoodCounter(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkManager.sendToPlayer(serverPlayer, new PacketFoodCounter(getFoodCounter(player).serializeNBT()));
        }
    }
}
