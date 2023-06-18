package com.sihenzhang.crockpot.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GnawsGiftHungerOverlay {
    private static final ResourceLocation GNAWS_GIFT_ICONS = RLUtils.createRL("textures/gui/gnaws_gift.png");
    public static final ResourceLocation FOOD_LEVEL_ELEMENT = RLUtils.createVanillaRL("food_level");
    public static final ResourceLocation MC_ICONS = new ResourceLocation("textures/gui/icons.png");
    private static final Random RAND = new Random();
    private static int hungerBarOffset;

//    @SubscribeEvent
    public static void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (!CrockPotConfigs.GNAWS_GIFT_HUNGER_OVERLAY.get()) {
            return;
        }
        if (event.getOverlay() != GuiOverlayManager.findOverlay(FOOD_LEVEL_ELEMENT)) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        ForgeGui gui = (ForgeGui) mc.gui;
        Player player = mc.player;
        if (player != null && player.hasEffect(CrockPotEffects.GNAWS_GIFT.get())) {
            boolean isMounted = player.getVehicle() instanceof LivingEntity;
            if (!isMounted && !mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
//                mStack.pose().pushPose();
//                mStack.pose().translate(0, 0, 0.01);
//                RenderSystem.setShaderTexture(0, GNAWS_GIFT_ICONS);
                GuiGraphics guiGraphics = event.getGuiGraphics();
                int left = mc.getWindow().getGuiScaledWidth() / 2 + 91;
                int top = mc.getWindow().getGuiScaledHeight() - hungerBarOffset;

                int tickCount = mc.gui.getGuiTicks();
                RAND.setSeed(tickCount * 312871L);

                FoodData stats = player.getFoodData();
                int level = stats.getFoodLevel();

                for (int i = 0; i < 10; i++) {
                    int idx = i * 2 + 1;
                    int x = left - i * 8 - 9;
                    int y = top;

                    if (stats.getSaturationLevel() <= 0.0F && tickCount % (level * 3 + 1) == 0) {
                        y = top + (RAND.nextInt(3) - 1);
                    }

                    guiGraphics.blit(GNAWS_GIFT_ICONS, x, y, 0, 0, 9, 9);
//                    mc.gui.blit(mStack, );

                    if (idx < level) {
                        guiGraphics.blit(GNAWS_GIFT_ICONS, x, y, 9, 0, 9, 9);
//                        mc.gui.blit(mStack, x, y, 9, 0, 9, 9);
                    } else if (idx == level) {
                        guiGraphics.blit(GNAWS_GIFT_ICONS, x, y, 18, 0, 9, 9);
//                        mc.gui.blit(mStack, x, y, 18, 0, 9, 9);
                    }
                }

                RenderSystem.setShaderTexture(0, MC_ICONS);
//                mStack.popPose();
            }
        }
    }

//    @SubscribeEvent
    public static void onClientSetupEvent(RegisterGuiOverlaysEvent event) {
        event.registerBelow(VanillaGuiOverlay.FOOD_LEVEL.id(), "get_hunger_bar_offset", (gui, mStack, partialTicks, width, height) -> {
            if (!CrockPotConfigs.GNAWS_GIFT_HUNGER_OVERLAY.get()) {
                return;
            }
            Player player = Minecraft.getInstance().player;
            if (player != null && player.hasEffect(CrockPotEffects.GNAWS_GIFT.get())) {
                hungerBarOffset = gui.rightHeight;
            }
        });
    }
}
