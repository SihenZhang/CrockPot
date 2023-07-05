package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public class GnawsGiftHungerOverlay {
    private static final ResourceLocation GNAWS_GIFT_ICONS = RLUtils.createRL("textures/gui/gnaws_gift.png");
    private static final Random RAND = new Random();
    private static int hungerBarOffset;

    @SubscribeEvent
    public static void onClientSetupEvent(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != GuiOverlayManager.findOverlay(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            return;
        }
        if (shouldRender()) {
            hungerBarOffset = ((ForgeGui) Minecraft.getInstance().gui).rightHeight;
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != GuiOverlayManager.findOverlay(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            return;
        }
        if (shouldRender()) {
            var mc = Minecraft.getInstance();
            var guiGraphics = event.getGuiGraphics();

            var left = mc.getWindow().getGuiScaledWidth() / 2 + 91;
            var top = mc.getWindow().getGuiScaledHeight() - hungerBarOffset;

            var tickCount = mc.gui.getGuiTicks();
            RAND.setSeed(tickCount * 312871L);

            var foodData = mc.player.getFoodData();
            var foodLevel = foodData.getFoodLevel();

            for (var i = 0; i < 10; i++) {
                var idx = i * 2 + 1;
                var x = left - i * 8 - 9;
                var y = top;

                if (foodData.getSaturationLevel() <= 0.0F && tickCount % (foodLevel * 3 + 1) == 0) {
                    y = top + (RAND.nextInt(3) - 1);
                }

                guiGraphics.blit(GNAWS_GIFT_ICONS, x, y, 0, 0, 9, 9);

                if (idx < foodLevel) {
                    guiGraphics.blit(GNAWS_GIFT_ICONS, x, y, 9, 0, 9, 9);
                } else if (idx == foodLevel) {
                    guiGraphics.blit(GNAWS_GIFT_ICONS, x, y, 18, 0, 9, 9);
                }
            }
        }
    }

    private static boolean shouldRender() {
        if (!CrockPotConfigs.GNAWS_GIFT_HUNGER_OVERLAY.get()) {
            return false;
        }
        var mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null) {
            return false;
        }
        if (!player.hasEffect(CrockPotEffects.GNAWS_GIFT.get())) {
            return false;
        }
        var gui = (ForgeGui) mc.gui;
        var isMounted = player.getVehicle() instanceof LivingEntity;
        return !isMounted && !mc.options.hideGui && gui.shouldDrawSurvivalElements();
    }
}
