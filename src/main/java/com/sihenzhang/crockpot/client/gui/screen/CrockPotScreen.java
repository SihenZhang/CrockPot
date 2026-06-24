package com.sihenzhang.crockpot.client.gui.screen;

import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CrockPotScreen extends AbstractContainerScreen<CrockPotMenu> {
    private static final Identifier TEXTURE = IdUtil.mod("textures/gui/crock_pot.png");

    public CrockPotScreen(CrockPotMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 175);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        blit(graphics, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        blit(graphics, leftPos + 32, topPos + 16, imageWidth, 85, 52, 46);
        var inputStacks = menu.getInputStacks();
        for (var i = 0; i < 2; i++) {
            for (var j = 0; j < 2; j++) {
                if (!inputStacks.get(j + i * 2).isEmpty()) {
                    blit(graphics, leftPos + 32 + j * 26, topPos + 16 + i * 23, imageWidth + j * 26, 131 + i * 24, 26, 24);
                }
            }
        }

        blit(graphics, leftPos + 91, topPos + 45, imageWidth, 30, 18, 33);
        if (menu.hasFuel()) {
            blit(graphics, leftPos + 91, topPos + 60, imageWidth + 26, 179, 18, 18);
        }
        if (menu.isBurning()) {
            var burningProgress = menu.getBurningProgress();
            blit(graphics, leftPos + 92, topPos + 44 + 12 - burningProgress, 176, 12 - burningProgress, 14, burningProgress + 1);
        }

        blit(graphics, leftPos + 88, topPos + 29, imageWidth, 63, 24, 11);
        var cookingProgress = menu.getCookingProgress();
        blit(graphics, leftPos + 88, topPos + 29, imageWidth, 73, cookingProgress + 1, 11);

        blit(graphics, leftPos + 118, topPos + 26, imageWidth, 179, 26, 26);
    }

    private static void blit(GuiGraphicsExtractor graphics, int x, int y, int u, int v, int width, int height) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, (float) u, (float) v, width, height, 256, 256);
    }
}
