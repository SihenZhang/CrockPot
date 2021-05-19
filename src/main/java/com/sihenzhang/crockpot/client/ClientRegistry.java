package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        // Register Screen
        ScreenManager.registerFactory(CrockPotRegistry.crockPotContainer, CrockPotScreen::new);
        // Register EntityRendering
//        RenderingRegistry.registerEntityRenderingHandler(CrockPotRegistry.birdcageEntity, EmptyEntityRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(CrockPotRegistry.birdEggEntity, manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
        // Register RenderType
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.unknownCropsBlock, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.asparagusBlock, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.cornBlock, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.eggplantBlock, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.onionBlock, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.pepperBlock, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.tomatoBlock, RenderType.getCutout());
    }

    @SubscribeEvent
    public static void onItemColorRegister(ColorHandlerEvent.Item event) {
//        event.getItemColors().register((IItemColor) CrockPotRegistry.birdEgg, CrockPotRegistry.birdEgg);
    }
}
