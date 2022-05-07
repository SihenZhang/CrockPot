package com.sihenzhang.crockpot.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GnawsGiftHungerOverlay {
    private static final ResourceLocation GNAWS_GIFT_ICONS = RLUtils.createRL("textures/gui/gnaws_gift.png");
    private static final Random RAND = new Random();
    private static int hungerBarOffset;

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            OverlayRegistry.registerOverlayBelow(ForgeIngameGui.FOOD_LEVEL_ELEMENT, "CrockPot Get Hunger Bar Offset", (gui, mStack, partialTicks, width, height) -> {
                if (!CrockPotConfig.GNAWS_GIFT_HUNGER_OVERLAY.get()) {
                    return;
                }
                Player player = Minecraft.getInstance().player;
                if (player != null && player.hasEffect(CrockPotRegistry.gnawsGift.get())) {
                    hungerBarOffset = gui.right_height;
                }
            });

            OverlayRegistry.registerOverlayAbove(ForgeIngameGui.FOOD_LEVEL_ELEMENT, "CrockPot Gnaw's Gift Hunger Overlay", (gui, mStack, partialTicks, width, height) -> {
                if (!CrockPotConfig.GNAWS_GIFT_HUNGER_OVERLAY.get()) {
                    return;
                }
                Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;
                if (player != null && player.hasEffect(CrockPotRegistry.gnawsGift.get())) {
                    boolean isMounted = player.getVehicle() instanceof LivingEntity;
                    if (!isMounted && !mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
                        mStack.pushPose();
                        mStack.translate(0, 0, 0.01);
                        RenderSystem.setShaderTexture(0, GNAWS_GIFT_ICONS);
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

                            mc.gui.blit(mStack, x, y, 0, 0, 9, 9);

                            if (idx < level) {
                                mc.gui.blit(mStack, x, y, 9, 0, 9, 9);
                            } else if (idx == level) {
                                mc.gui.blit(mStack, x, y, 18, 0, 9, 9);
                            }
                        }

                        RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
                        mStack.popPose();
                    }
                }
            });
        });
    }
}
