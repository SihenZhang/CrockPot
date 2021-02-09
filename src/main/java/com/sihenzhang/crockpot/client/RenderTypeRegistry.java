package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderTypeRegistry {
    @SubscribeEvent
    public static void onRenderTypeSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.unknownCrops.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.asparagusBlock.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.cornBlock.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.eggplantBlock.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.onionBlock.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.pepperBlock.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(CrockPotRegistry.tomatoBlock.get(), RenderType.getCutout());
    }
}
