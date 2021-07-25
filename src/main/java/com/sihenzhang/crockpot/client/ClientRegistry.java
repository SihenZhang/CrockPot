package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.client.renderer.layers.MilkmadeHatLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.MobEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register Screen
            ScreenManager.register(CrockPotRegistry.crockPotContainer, CrockPotScreen::new);
            // Register EntityRendering
//            RenderingRegistry.registerEntityRenderingHandler(CrockPotRegistry.birdcageEntity, EmptyEntityRenderer::new);
//            RenderingRegistry.registerEntityRenderingHandler(CrockPotRegistry.birdEggEntity, manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
            // Add Layer to EntityRenderer
            Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().values().forEach(playerRenderer -> playerRenderer.addLayer(new MilkmadeHatLayer<>(playerRenderer)));
            Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(entityRenderer -> {
                if (entityRenderer instanceof ArmorStandRenderer) {
                    ArmorStandRenderer armorStandRenderer = (ArmorStandRenderer) entityRenderer;
                    armorStandRenderer.addLayer(new MilkmadeHatLayer<>(armorStandRenderer));
                }
                if (entityRenderer instanceof BipedRenderer) {
                    @SuppressWarnings("unchecked") BipedRenderer<MobEntity, BipedModel<MobEntity>> bipedRenderer = (BipedRenderer<MobEntity, BipedModel<MobEntity>>) entityRenderer;
                    bipedRenderer.addLayer(new MilkmadeHatLayer<>(bipedRenderer));
                }
            });
            // Register RenderType
            RenderTypeLookup.setRenderLayer(CrockPotRegistry.unknownCropsBlock, RenderType.cutout());
            RenderTypeLookup.setRenderLayer(CrockPotRegistry.asparagusBlock, RenderType.cutout());
            RenderTypeLookup.setRenderLayer(CrockPotRegistry.cornBlock, RenderType.cutout());
            RenderTypeLookup.setRenderLayer(CrockPotRegistry.eggplantBlock, RenderType.cutout());
            RenderTypeLookup.setRenderLayer(CrockPotRegistry.onionBlock, RenderType.cutout());
            RenderTypeLookup.setRenderLayer(CrockPotRegistry.pepperBlock, RenderType.cutout());
            RenderTypeLookup.setRenderLayer(CrockPotRegistry.tomatoBlock, RenderType.cutout());
        });
    }

    @SubscribeEvent
    public static void onItemColorRegister(ColorHandlerEvent.Item event) {
//        event.getItemColors().register((IItemColor) CrockPotRegistry.birdEgg, CrockPotRegistry.birdEgg);
    }
}
