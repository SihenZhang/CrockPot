package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.Config;
import com.sihenzhang.crockpot.effect.ModEffects;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public final class GnawsGiftHungerOverlay {
    public static final Identifier LAYER_ID = IdUtil.mod("gnaws_gift_hunger");
    private static final Identifier GNAWS_GIFT_ICONS = IdUtil.mod("textures/gui/gnaws_gift.png");
    private static final Random RAND = new Random();

    private GnawsGiftHungerOverlay() {
    }

    public static void render(GuiGraphicsExtractor graphics) {
        if (!shouldRender()) {
            return;
        }

        var mc = Minecraft.getInstance();
        var left = graphics.guiWidth() / 2 + 91;
        var top = graphics.guiHeight() - (mc.gui.rightHeight - 10);
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

            blit(graphics, x, y, 0);
            if (idx < foodLevel) {
                blit(graphics, x, y, 9);
            } else if (idx == foodLevel) {
                blit(graphics, x, y, 18);
            }
        }
    }

    private static void blit(GuiGraphicsExtractor graphics, int x, int y, int u) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, GNAWS_GIFT_ICONS, x, y, (float) u, 0.0F, 9, 9, 256, 256);
    }

    private static boolean shouldRender() {
        if (!Config.GNAWS_GIFT_HUNGER_OVERLAY.get()) {
            return false;
        }
        var mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null || mc.gameMode == null) {
            return false;
        }
        if (!player.hasEffect(ModEffects.GNAWS_GIFT)) {
            return false;
        }
        var isMounted = player.getVehicle() instanceof LivingEntity;
        return !isMounted && !mc.options.hideGui && mc.gameMode.canHurtPlayer();
    }
}
