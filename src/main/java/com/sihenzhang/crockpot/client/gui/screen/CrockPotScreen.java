package com.sihenzhang.crockpot.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

@MethodsReturnNonnullByDefault
public class CrockPotScreen extends ContainerScreen<CrockPotContainer> {
    private static final ResourceLocation texture = new ResourceLocation(CrockPot.MOD_ID, "textures/gui/crock_pot.png");

    public CrockPotScreen(CrockPotContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.ySize = 184;
    }

    @Override
    public ITextComponent getTitle() {
        Item item = this.getContainer().getTileEntity().getBlockState().getBlock().asItem();
        return item.getDisplayName(new ItemStack(item));
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        renderHoveredToolTip(p_render_1_, p_render_2_);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        Minecraft minecraft = getMinecraft();
        String crockPotTitle = getTitle().getFormattedText();
        minecraft.fontRenderer.drawString(crockPotTitle, this.xSize / 2f - minecraft.fontRenderer.getStringWidth(crockPotTitle) / 2f, 6, 0x404040);
        minecraft.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        getMinecraft().getTextureManager().bindTexture(texture);

        CrockPotTileEntity tileEntity = container.getTileEntity();

        // Draw Background
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);

        // Draw Input Slots
        blit(guiLeft + 38, guiTop + 16, 176, 97, 36, 36);

        // Draw Fuel Slots
        blit(guiLeft + 47, guiTop + 55, 176, 30, 18, 33);

        // Draw Fuel Bar
        if (tileEntity.isBurning()) {
            int burnTime = (int) (13 * tileEntity.getBurnTimeProgress());
            blit(guiLeft + 48, guiTop + 54 + 12 - burnTime, 176, 12 - burnTime, 14, burnTime + 1);
        }

        // Draw Process Arrow
        blit(guiLeft + 80, guiTop + 44, 176, 63, 24, 17);

        // Draw Process Bar
        if (tileEntity.isProcessing()) {
            int processTime = (int) (24 * tileEntity.getProcessTimeProgress());
            blit(guiLeft + 80, guiTop + 43, 176, 80, processTime + 1, 16);
        }

        // Draw Output Slots
        blit(guiLeft + 112, guiTop + 39, 176, 133, 26, 26);
    }
}
