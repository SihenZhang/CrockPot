package com.sihenzhang.crockpot.client;

import com.google.common.reflect.TypeToken;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.entity.ModBlockEntities;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.client.model.MilkmadeHatModel;
import com.sihenzhang.crockpot.client.model.geom.CrockPotModelLayers;
import com.sihenzhang.crockpot.client.renderer.blockentity.DryingRackRenderer;
import com.sihenzhang.crockpot.client.renderer.entity.EmptyRenderer;
import com.sihenzhang.crockpot.client.renderer.entity.VoltGoatRenderer;
import com.sihenzhang.crockpot.client.renderer.entity.layers.MilkmadeHatLayer;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.inventory.ModMenuTypes;
import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public final class ClientRegistry {
    private ClientRegistry() {
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.CROCK_POT_MENU_TYPE.get(), CrockPotScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BIRDCAGE.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntities.PARROT_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.VOLT_GOAT.get(), VoltGoatRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DRYING_RACK_BLOCK_ENTITY.get(), DryingRackRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CrockPotModelLayers.MILKMADE_HAT, MilkmadeHatModel::createLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {}, (entity, state) -> {
            if (state instanceof HumanoidRenderState) {
                // Vanilla headEquipment only includes items with an equippable asset ID.
                boolean wearingHat = entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof MilkmadeHatItem;
                state.setRenderData(MilkmadeHatLayer.WEARING_MILKMADE_HAT, wearingHat);
                if (wearingHat) {
                    // The custom layer replaces the ordinary item model on the head.
                    state.headItem.clear();
                }
            }
        });
    }

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, GnawsGiftHungerOverlay.LAYER_ID, (graphics, deltaTracker) -> GnawsGiftHungerOverlay.render(graphics));
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(skin -> {
            var renderer = event.getPlayerRenderer(skin);
            if (renderer != null) {
                addMilkmadeHatLayer(renderer, event);
            }
            var mannequinRenderer = event.getMannequinRenderer(skin);
            if (mannequinRenderer != null) {
                addMilkmadeHatLayer(mannequinRenderer, event);
            }
        });
        event.getEntityTypes().forEach(entityType -> {
            var renderer = event.getRenderer(entityType);
            if (renderer instanceof ArmorStandRenderer armorStandRenderer) {
                addMilkmadeHatLayer(armorStandRenderer, event);
            } else if (renderer instanceof HumanoidMobRenderer<?, ?, ?> humanoidMobRenderer) {
                addMilkmadeHatLayer(humanoidMobRenderer, event);
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addMilkmadeHatLayer(LivingEntityRenderer renderer, EntityRenderersEvent.AddLayers event) {
        renderer.addLayer(new MilkmadeHatLayer(renderer, event.getEntityModels()));
    }
}
