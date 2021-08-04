package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.network.PacketSyncExplosionCraftingRecipe;
import com.sihenzhang.crockpot.network.PacketSyncFoodValues;
import com.sihenzhang.crockpot.network.PacketSyncPiglinBarteringRecipe;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class SyncDataPackEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(
                () -> (ServerPlayerEntity) event.getPlayer()),
                new PacketSyncFoodValues(CrockPot.FOOD_VALUES_MANAGER.serialize())
        );
        NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(
                () -> (ServerPlayerEntity) event.getPlayer()),
                new PacketSyncPiglinBarteringRecipe(CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.serialize())
        );
        NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(
                () -> (ServerPlayerEntity) event.getPlayer()),
                new PacketSyncExplosionCraftingRecipe(CrockPot.EXPLOSION_CRAFTING_RECIPE_MANAGER.serialize())
        );
    }

    @SubscribeEvent
    public static void onReloading(AddReloadListenerEvent event) {
        event.addListener(new ReloadListener<Void>() {
            @Override
            protected Void prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
                return null;
            }

            @Override
            protected void apply(Void objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
                if (EffectiveSide.get().isServer()) {
                    NetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncFoodValues(CrockPot.FOOD_VALUES_MANAGER.serialize()));
                    NetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncPiglinBarteringRecipe(CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.serialize()));
                    NetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncExplosionCraftingRecipe(CrockPot.EXPLOSION_CRAFTING_RECIPE_MANAGER.serialize()));
                }
            }
        });
    }
}
