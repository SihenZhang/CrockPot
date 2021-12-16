package com.sihenzhang.crockpot.capability;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.CrockPotCriteriaTriggers;
import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.network.PacketFoodCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class FoodCounterCapabilityHandler {
    @CapabilityInject(IFoodCounter.class)
    public static Capability<IFoodCounter> FOOD_COUNTER_CAPABILITY = null;
    public static final ResourceLocation FOOD_COUNTER = new ResourceLocation(CrockPot.MOD_ID, "food_counter");

    @SubscribeEvent
    public static void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(FOOD_COUNTER, new FoodCounterProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerAppear(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayerEntity) {
            syncFoodCounter((PlayerEntity) entity);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity player = event.getPlayer();
        PlayerEntity oldPlayer = event.getOriginal();
        oldPlayer.revive();
        oldPlayer.getCapability(FOOD_COUNTER_CAPABILITY).ifPresent(oldFoodCounter -> player.getCapability(FOOD_COUNTER_CAPABILITY)
                .ifPresent(newFoodCounter -> newFoodCounter.deserializeNBT(oldFoodCounter.serializeNBT()))
        );
    }

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntityLiving() instanceof ServerPlayerEntity) || !event.getItem().isEdible()) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
        player.getCapability(FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> {
            ItemStack stack = event.getItem();
            foodCounter.addFood(stack.getItem());
            CrockPotCriteriaTriggers.EAT_FOOD_TRIGGER.trigger(player, stack, foodCounter.getCount(stack.getItem()));
        });
        syncFoodCounter(player);
    }

    public static void syncFoodCounter(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            serverPlayer.getCapability(FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> NetworkManager.sendToPlayer(serverPlayer, new PacketFoodCounter(foodCounter.serializeNBT())));
        }
    }

    @Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Setup {
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            CapabilityManager.INSTANCE.register(IFoodCounter.class, new Capability.IStorage<IFoodCounter>() {
                @Nullable
                @Override
                public INBT writeNBT(Capability<IFoodCounter> capability, IFoodCounter instance, Direction side) {
                    return instance.serializeNBT();
                }

                @Override
                public void readNBT(Capability<IFoodCounter> capability, IFoodCounter instance, Direction side, INBT nbt) {
                    instance.deserializeNBT((CompoundNBT) nbt);
                }
            }, FoodCounter::new);
        }
    }
}
