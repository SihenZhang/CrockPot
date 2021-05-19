package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.network.PacketSyncCrockPotFoodCategory;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class SyncFoodCategoryEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(
                () -> (ServerPlayerEntity) event.getEntity()),
                new PacketSyncCrockPotFoodCategory(CrockPot.FOOD_CATEGORY_MANAGER.serialize())
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
                if (ServerLifecycleHooks.getCurrentServer() != null) {
                    NetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncCrockPotFoodCategory(CrockPot.FOOD_CATEGORY_MANAGER.serialize()));
                }
            }
        });
    }
}
