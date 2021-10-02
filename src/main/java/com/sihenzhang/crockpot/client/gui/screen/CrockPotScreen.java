package com.sihenzhang.crockpot.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrockPotScreen extends ContainerScreen<CrockPotContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CrockPot.MOD_ID, "textures/gui/crock_pot.png");

    public CrockPotScreen(CrockPotContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 184;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public ITextComponent getTitle() {
        return this.getMenu().getTileEntity().getBlockState().getBlock().getName();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        ITextComponent title = getTitle();
        this.font.draw(matrixStack, title, this.imageWidth / 2.0F - this.font.width(title) / 2.0F, (float) this.titleLabelY, 0x404040);
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float) this.inventoryLabelX, (float) this.inventoryLabelY, 0x404040);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bind(TEXTURE);

        CrockPotTileEntity tileEntity = menu.getTileEntity();

        // Draw Background
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Draw Input Slots
        blit(matrixStack, leftPos + 38, topPos + 16, 176, 97, 36, 36);

        // Draw Fuel Slots
        blit(matrixStack, leftPos + 47, topPos + 55, 176, 30, 18, 33);

        // Draw Fuel Bar
        if (tileEntity.isBurning()) {
            int burnTime = (int) (13 * tileEntity.getBurnTimeProgress());
            blit(matrixStack, leftPos + 48, topPos + 54 + 12 - burnTime, 176, 12 - burnTime, 14, burnTime + 1);
        }

        // Draw Process Arrow
        blit(matrixStack, leftPos + 80, topPos + 44, 176, 63, 24, 17);

        // Draw Process Bar
        if (tileEntity.isProcessing()) {
            int processTime = (int) (24 * tileEntity.getProcessTimeProgress());
            blit(matrixStack, leftPos + 80, topPos + 43, 176, 80, processTime + 1, 16);
        }

        // Draw Output Slots
        blit(matrixStack, leftPos + 112, topPos + 39, 176, 133, 26, 26);
    }
}
