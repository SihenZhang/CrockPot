package com.sihenzhang.crockpot.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CrockPotScreen extends AbstractContainerScreen<CrockPotMenu> {
    private static final ResourceLocation TEXTURE = RLUtils.createRL("textures/gui/crock_pot.png");

    public CrockPotScreen(CrockPotMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 184;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public Component getTitle() {
        return menu.getBlockEntity().getBlockState().getBlock().getName();
    }

//    @Override
//    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
//        this.renderBackground(poseStack);
//        super.render(poseStack, mouseX, mouseY, partialTick);
//        this.renderTooltip(poseStack, mouseX, mouseY);
//    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    //    @Override
//    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
//        Component title = getTitle();
//        font.draw(poseStack, title, imageWidth / 2.0F - font.width(title) / 2.0F, (float) titleLabelY, 0x404040);
//        font.draw(poseStack, playerInventoryTitle, (float) inventoryLabelX, (float) inventoryLabelY, 0x404040);
//    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Component title = getTitle();
        guiGraphics.drawString(font, title, (int) (imageWidth / 2.0F - font.width(title) / 2.0F), titleLabelY, 0x404040);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // Draw Background
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Draw Input Slots
        guiGraphics.blit(TEXTURE, leftPos + 38, topPos + 16, 176, 97, 36, 36);

        // Draw Fuel Slots
        guiGraphics.blit(TEXTURE, leftPos + 47, topPos + 55, 176, 30, 18, 33);

        // Draw Fuel Bar
        int burningProgress = menu.getBurningProgress();
        guiGraphics.blit(TEXTURE, leftPos + 48, topPos + 54 + 13 - burningProgress, 176, 13 - burningProgress, 14, burningProgress + 1);

        // Draw Process Arrow
        guiGraphics.blit(TEXTURE, leftPos + 80, topPos + 44, 176, 63, 24, 17);

        // Draw Process Bar
        int cookingProgress = menu.getCookingProgress();
        guiGraphics.blit(TEXTURE, leftPos + 80, topPos + 43, 176, 80, cookingProgress + 1, 16);

        // Draw Output Slots
        guiGraphics.blit(TEXTURE, leftPos + 112, topPos + 39, 176, 133, 26, 26);
    }

//    @Override
//    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, TEXTURE);
//
//        // Draw Background
//        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
//
//        // Draw Input Slots
//        blit(poseStack, leftPos + 38, topPos + 16, 176, 97, 36, 36);
//
//        // Draw Fuel Slots
//        blit(poseStack, leftPos + 47, topPos + 55, 176, 30, 18, 33);
//
//        // Draw Fuel Bar
//        int burningProgress = menu.getBurningProgress();
//        blit(poseStack, leftPos + 48, topPos + 54 + 13 - burningProgress, 176, 13 - burningProgress, 14, burningProgress + 1);
//
//        // Draw Process Arrow
//        blit(poseStack, leftPos + 80, topPos + 44, 176, 63, 24, 17);
//
//        // Draw Process Bar
//        int cookingProgress = menu.getCookingProgress();
//        blit(poseStack, leftPos + 80, topPos + 43, 176, 80, cookingProgress + 1, 16);
//
//        // Draw Output Slots
//        blit(poseStack, leftPos + 112, topPos + 39, 176, 133, 26, 26);
//    }
}
