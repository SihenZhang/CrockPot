package com.sihenzhang.crockpot.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrockPotScreen extends ContainerScreen<CrockPotContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CrockPot.MOD_ID, "textures/gui/crock_pot.png");

    public CrockPotScreen(CrockPotContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.ySize = 184;
    }

    @Override
    public ITextComponent getTitle() {
        Item item = this.getContainer().getTileEntity().getBlockState().getBlock().asItem();
        return item.getDisplayName(item.getDefaultInstance());
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        Minecraft minecraft = getMinecraft();
        ITextComponent title = getTitle();
        minecraft.fontRenderer.drawText(matrixStack, title, this.xSize / 2.0F - minecraft.fontRenderer.getStringPropertyWidth(title) / 2.0F, 6.0F, 0x404040);
        minecraft.fontRenderer.drawString(matrixStack, I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bindTexture(TEXTURE);

        CrockPotTileEntity tileEntity = container.getTileEntity();

        // Draw Background
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        // Draw Input Slots
        blit(matrixStack, guiLeft + 38, guiTop + 16, 176, 97, 36, 36);

        // Draw Fuel Slots
        blit(matrixStack, guiLeft + 47, guiTop + 55, 176, 30, 18, 33);

        // Draw Fuel Bar
        if (tileEntity.isBurning()) {
            int burnTime = (int) (13 * tileEntity.getBurnTimeProgress());
            blit(matrixStack, guiLeft + 48, guiTop + 54 + 12 - burnTime, 176, 12 - burnTime, 14, burnTime + 1);
        }

        // Draw Process Arrow
        blit(matrixStack, guiLeft + 80, guiTop + 44, 176, 63, 24, 17);

        // Draw Process Bar
        if (tileEntity.isProcessing()) {
            int processTime = (int) (24 * tileEntity.getProcessTimeProgress());
            blit(matrixStack, guiLeft + 80, guiTop + 43, 176, 80, processTime + 1, 16);
        }

        // Draw Output Slots
        blit(matrixStack, guiLeft + 112, guiTop + 39, 176, 133, 26, 26);
    }
}
