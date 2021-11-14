package com.sihenzhang.crockpot.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public class GnawsGiftHungerOverlay {
    private static final ResourceLocation GNAWS_GIFT_ICONS = new ResourceLocation(CrockPot.MOD_ID, "textures/gui/gnaws_gift.png");
    private static int foodIconsOffset;

    @SubscribeEvent
    public static void onPreRender(RenderGameOverlayEvent.Pre event) {
        if (!CrockPotConfig.GNAWS_GIFT_HUNGER_OVERLAY.get()) {
            return;
        }
        if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD) {
            return;
        }
        if (event.isCanceled()) {
            return;
        }
        foodIconsOffset = ForgeIngameGui.right_height;
    }

    @SubscribeEvent
    public static void onRender(RenderGameOverlayEvent.Post event) {
        if (!CrockPotConfig.GNAWS_GIFT_HUNGER_OVERLAY.get()) {
            return;
        }
        if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD) {
            return;
        }
        if (event.isCanceled()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player.hasEffect(CrockPotRegistry.gnawsGift)) {
            MatrixStack matrixStack = event.getMatrixStack();
            matrixStack.pushPose();
            matrixStack.translate(0, 0, 0.01);
            mc.getTextureManager().bind(GNAWS_GIFT_ICONS);
            RenderSystem.enableBlend();
            int left = mc.getWindow().getGuiScaledWidth() / 2 + 91;
            int top = mc.getWindow().getGuiScaledHeight() - foodIconsOffset;

            int tickCount = mc.gui.getGuiTicks();
            Random rand = new Random();
            rand.setSeed(tickCount * 312871L);

            FoodStats stats = player.getFoodData();
            int level = stats.getFoodLevel();

            for (int i = 0; i < 10; i++) {
                int idx = i * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;

                if (stats.getSaturationLevel() <= 0.0F && tickCount % (level * 3 + 1) == 0) {
                    y = top + (rand.nextInt(3) - 1);
                }

                mc.gui.blit(matrixStack, x, y, 0, 0, 9, 9);

                if (idx < level) {
                    mc.gui.blit(matrixStack, x, y, 9, 0, 9, 9);
                } else if (idx == level) {
                    mc.gui.blit(matrixStack, x, y, 18, 0, 9, 9);
                }
            }

            RenderSystem.disableBlend();
            mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
            matrixStack.popPose();
        }
    }
}
