package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.client.model.MilkmadeHatModel;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register Screen
            MenuScreens.register(CrockPotRegistry.crockPotMenu, CrockPotScreen::new);
            // Register RenderType
            ItemBlockRenderTypes.setRenderLayer(CrockPotRegistry.unknownCropsBlock, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CrockPotRegistry.asparagusBlock, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CrockPotRegistry.cornBlock, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CrockPotRegistry.eggplantBlock, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CrockPotRegistry.onionBlock, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CrockPotRegistry.pepperBlock, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CrockPotRegistry.tomatoBlock, RenderType.cutout());
        });
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MilkmadeHatModel.LAYER_LOCATION, MilkmadeHatModel::createBodyLayer);
    }
}
