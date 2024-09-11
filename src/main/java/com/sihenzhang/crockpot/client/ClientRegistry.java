package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.client.model.MilkmadeHatModel;
import com.sihenzhang.crockpot.client.model.VoltGoatModel;
import com.sihenzhang.crockpot.client.model.geom.CrockPotModelLayers;
import com.sihenzhang.crockpot.client.renderer.entity.EmptyRenderer;
import com.sihenzhang.crockpot.client.renderer.entity.VoltGoatRenderer;
import com.sihenzhang.crockpot.client.renderer.entity.layers.MilkmadeHatLayer;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.inventory.ModMenuTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistry {
    @SubscribeEvent
    public static void onClientSetupEvent(final RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.CROCK_POT_MENU_TYPE.get(), CrockPotScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BIRDCAGE.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntities.PARROT_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.VOLT_GOAT.get(), VoltGoatRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CrockPotModelLayers.MILKMADE_HAT, MilkmadeHatModel::createLayer);
        event.registerLayerDefinition(CrockPotModelLayers.VOLT_GOAT, VoltGoatModel::createBodyLayer);
        event.registerLayerDefinition(CrockPotModelLayers.VOLT_GOAT_ARMOR, VoltGoatModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(name -> {
            LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> renderer = event.getSkin(name);
            if (renderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new MilkmadeHatLayer<>(playerRenderer, event.getEntityModels()));
            }
        });
        // TODO: Super Hacky Way! Maybe use ArmorItem and getArmorModel instead.
        Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(renderer -> {
            if (renderer instanceof ArmorStandRenderer armorStandRenderer) {
                armorStandRenderer.addLayer(new MilkmadeHatLayer<>(armorStandRenderer, event.getEntityModels()));
            }
            if (renderer instanceof HumanoidMobRenderer) {
                @SuppressWarnings("unchecked") HumanoidMobRenderer<Mob, HumanoidModel<Mob>> humanoidMobRenderer = (HumanoidMobRenderer<Mob, HumanoidModel<Mob>>) renderer;
                humanoidMobRenderer.addLayer(new MilkmadeHatLayer<>(humanoidMobRenderer, event.getEntityModels()));
            }
        });
    }
}
