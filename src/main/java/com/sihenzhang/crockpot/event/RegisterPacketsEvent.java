package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.patchouli.ModIntegrationPatchouli;
import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.network.PacketFoodCounter;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterPacketsEvent {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        NetworkManager.registerPacket(
                PacketFoodCounter.class,
                PacketFoodCounter::serialize,
                PacketFoodCounter::deserialize,
                PacketFoodCounter::handle,
                NetworkDirection.PLAY_TO_CLIENT
        );
        if (ModList.get().isLoaded(ModIntegrationPatchouli.MOD_ID)) {
            ModIntegrationPatchouli.registerPacket();
        }
    }
}
