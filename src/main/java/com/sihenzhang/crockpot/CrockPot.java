package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(CrockPot.MOD_ID)
public class CrockPot {
    public static final String MOD_ID = "crockpot";

    public CrockPot() {
        CrockPotRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CrockPotRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CrockPotRegistry.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CrockPotRegistry.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void onClineSetupEvent(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(CrockPotRegistry.crockPotContainer.get(), CrockPotScreen::new);
    }
}
