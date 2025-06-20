package com.sihenzhang.crockpot.client.gui.screen;

import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CrockPotScreen extends AbstractContainerScreen<CrockPotMenu> {
    private static final ResourceLocation TEXTURE = RLUtils.createRL("textures/gui/crock_pot.png");

    public CrockPotScreen(CrockPotMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 175;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Draw Background
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Draw Input Slots
        guiGraphics.blit(TEXTURE, leftPos + 32, topPos + 16, imageWidth, 85, 52, 46);
        var inputStacks = menu.getInputStacks();
        for (var i = 0; i < 2; i++) {
            for (var j = 0; j < 2; j++) {
                if (!inputStacks.get(j + i * 2).isEmpty()) {
                    guiGraphics.blit(TEXTURE, leftPos + 32 + j * 26, topPos + 16 + i * 23, imageWidth + j * 26, 131 + i * 24, 26, 24);
                }
            }
        }

        // Draw Fuel Slots
        guiGraphics.blit(TEXTURE, leftPos + 91, topPos + 45, imageWidth, 30, 18, 33);
        if (menu.hasFuel()) {
            guiGraphics.blit(TEXTURE, leftPos + 91, topPos + 60, imageWidth + 26, 179, 18, 18);
        }
        // Draw Burning Bar
        if (menu.isBurning()) {
            var burningProgress = menu.getBurningProgress();
            guiGraphics.blit(TEXTURE, leftPos + 92, topPos + 44 + 12 - burningProgress, 176, 12 - burningProgress, 14, burningProgress + 1);
        }

        // Draw Process Arrow
        guiGraphics.blit(TEXTURE, leftPos + 88, topPos + 29, imageWidth, 63, 24, 11);
        var cookingProgress = menu.getCookingProgress();
        guiGraphics.blit(TEXTURE, leftPos + 88, topPos + 29, imageWidth, 73, cookingProgress + 1, 11);

        // Draw Output Slots
        guiGraphics.blit(TEXTURE, leftPos + 118, topPos + 26, imageWidth, 179, 26, 26);
    }
}
