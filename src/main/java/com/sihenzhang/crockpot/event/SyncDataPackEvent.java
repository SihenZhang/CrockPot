package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.patchouli.ModIntegrationPatchouli;
import com.sihenzhang.crockpot.integration.patchouli.PacketCrockPotConfigFlag;
import com.sihenzhang.crockpot.network.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.EffectiveSide;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class SyncDataPackEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        if (ModList.get().isLoaded(ModIntegrationPatchouli.MOD_ID)) {
            NetworkManager.sendToPlayer(player, new PacketCrockPotConfigFlag(ModIntegrationPatchouli.getConfigFlags()));
        }
    }

    @SubscribeEvent
    public static void onReloading(AddReloadListenerEvent event) {
        event.addListener(new SimplePreparableReloadListener<Void>() {
            @Override
            protected Void prepare(ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
                return null;
            }

            @Override
            protected void apply(Void objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
                if (EffectiveSide.get().isServer()) {
                    if (ModList.get().isLoaded(ModIntegrationPatchouli.MOD_ID)) {
                        NetworkManager.sendToAll(new PacketCrockPotConfigFlag(ModIntegrationPatchouli.getConfigFlags()));
                    }
                }
            }
        });
    }
}
