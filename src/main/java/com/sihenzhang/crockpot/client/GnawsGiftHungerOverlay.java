package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.effect.ModEffects;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class GnawsGiftHungerOverlay {
    private static final ResourceLocation GNAWS_GIFT_ICONS = RLUtils.mod("textures/gui/gnaws_gift.png");
    public static final RandomSource RANDOM = RandomSource.create();
    private static int foodLevelOffset;

    @SubscribeEvent
    public static void init(final RegisterGuiLayersEvent event) {
        event.registerBelow(VanillaGuiLayers.FOOD_LEVEL, RLUtils.mod("food_level_offset"), (guiGraphics, deltaTracker) -> foodLevelOffset = Minecraft.getInstance().gui.rightHeight);
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, RLUtils.mod("gnaws_gift"), new GnawsGiftHungerGuiLayer());
    }

    public static class GnawsGiftHungerGuiLayer implements LayeredDraw.Layer {
        @Override
        public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
            if (!CrockPotConfigs.GNAWS_GIFT_HUNGER_OVERLAY.get()) {
                return;
            }
            var mc = Minecraft.getInstance();
            if (mc.options.hideGui || !mc.gameMode.canHurtPlayer()) {
                return;
            }
            var player = mc.getCameraEntity() instanceof Player ? (Player) mc.getCameraEntity() : null;
            if (player == null) {
                return;
            }
            if (!player.hasEffect(ModEffects.GNAWS_GIFT)) {
                return;
            }
            var guiTicks = mc.gui.getGuiTicks();
            var top = guiGraphics.guiHeight() - foodLevelOffset;
            var right = guiGraphics.guiWidth() / 2 + 91; // right of food bar

            var foodData = player.getFoodData();
            var foodLevel = foodData.getFoodLevel();
            for (var i = 0; i < 10; i++) {
                var idx = i * 2 + 1;
                var x = right - i * 8 - 9;
                var y = top;

                if (foodData.getSaturationLevel() <= 0.0F && guiTicks % (foodLevel * 3 + 1) == 0) {
                    y = top + (RANDOM.nextInt(3) - 1);
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
}
