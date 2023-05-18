package com.sihenzhang.crockpot.integration.jei.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class DrawableNineSliceResource implements IDrawable {
    private final ResourceLocation resourceLocation;
    private final int textureWidth;
    private final int textureHeight;

    private final int u;
    private final int v;
    private final int actualWidth;
    private final int actualHeight;

    private final int width;
    private final int height;
    private final int sliceLeft;
    private final int sliceRight;
    private final int sliceTop;
    private final int sliceBottom;

    public DrawableNineSliceResource(ResourceLocation resourceLocation, int u, int v, int actualWidth, int actualHeight, int width, int height, int sliceLeft, int sliceRight, int sliceTop, int sliceBottom, int textureWidth, int textureHeight) {
        this.resourceLocation = resourceLocation;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.u = u;
        this.v = v;
        this.actualWidth = actualWidth;
        this.actualHeight = actualHeight;
        this.width = width;
        this.height = height;
        this.sliceLeft = sliceLeft;
        this.sliceRight = sliceRight;
        this.sliceTop = sliceTop;
        this.sliceBottom = sliceBottom;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(PoseStack stack, int xOffset, int yOffset) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, resourceLocation);

        float scaledWidth = 1.0F / textureWidth;
        float scaledHeight = 1.0F / textureHeight;

        float uMin = u * scaledWidth;
        float uMax = (u + actualWidth) * scaledWidth;
        float vMin = v * scaledHeight;
        float vMax = (v + actualHeight) * scaledHeight;
        float uSize = uMax - uMin;
        float vSize = vMax - vMin;

        float uLeft = uMin + uSize * (sliceLeft / (float) actualWidth);
        float uRight = uMax - uSize * (sliceRight / (float) actualWidth);
        float vTop = vMin + vSize * (sliceTop / (float) actualHeight);
        float vBottom = vMax - vSize * (sliceBottom / (float) actualHeight);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix = stack.last().pose();

        // left top
        draw(bufferBuilder, matrix, uMin, vMin, uLeft, vTop, xOffset, yOffset, sliceLeft, sliceTop);
        // left bottom
        draw(bufferBuilder, matrix, uMin, vBottom, uLeft, vMax, xOffset, yOffset + height - sliceBottom, sliceLeft, sliceBottom);
        // right top
        draw(bufferBuilder, matrix, uRight, vMin, uMax, vTop, xOffset + width - sliceRight, yOffset, sliceRight, sliceTop);
        // right bottom
        draw(bufferBuilder, matrix, uRight, vBottom, uMax, vMax, xOffset + width - sliceRight, yOffset + height - sliceBottom, sliceRight, sliceBottom);

        int middleWidth = actualWidth - sliceLeft - sliceRight;
        int middleHeight = actualHeight - sliceTop - sliceBottom;
        int tiledMiddleWidth = width - sliceLeft - sliceRight;
        int tiledMiddleHeight = height - sliceTop - sliceBottom;
        if (tiledMiddleWidth > 0) {
            // top edge
            drawTiled(bufferBuilder, matrix, uLeft, vMin, uRight, vTop, xOffset + sliceLeft, yOffset, tiledMiddleWidth, sliceTop, middleWidth, sliceTop);
            // bottom edge
            drawTiled(bufferBuilder, matrix, uLeft, vBottom, uRight, vMax, xOffset + sliceLeft, yOffset + height - sliceBottom, tiledMiddleWidth, sliceBottom, middleWidth, sliceBottom);
        }
        if (tiledMiddleHeight > 0) {
            // left side
            drawTiled(bufferBuilder, matrix, uMin, vTop, uLeft, vBottom, xOffset, yOffset + sliceTop, sliceLeft, tiledMiddleHeight, sliceLeft, middleHeight);
            // right side
            drawTiled(bufferBuilder, matrix, uRight, vTop, uMax, vBottom, xOffset + width - sliceRight, yOffset + sliceTop, sliceRight, tiledMiddleHeight, sliceRight, middleHeight);
        }
        if (tiledMiddleHeight > 0 && tiledMiddleWidth > 0) {
            // middle area
            drawTiled(bufferBuilder, matrix, uLeft, vTop, uRight, vBottom, xOffset + sliceLeft, yOffset + sliceTop, tiledMiddleWidth, tiledMiddleHeight, middleWidth, middleHeight);
        }

        tesselator.end();
    }

    private void drawTiled(BufferBuilder bufferBuilder, Matrix4f matrix, float uMin, float vMin, float uMax, float vMax, int xOffset, int yOffset, int tiledWidth, int tiledHeight, int width, int height) {
        int xTileCount = tiledWidth / width;
        int xRemainder = tiledWidth - (xTileCount * width);
        int yTileCount = tiledHeight / height;
        int yRemainder = tiledHeight - (yTileCount * height);

        int yStart = yOffset + tiledHeight;

        float uSize = uMax - uMin;
        float vSize = vMax - vMin;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int tileWidth = (xTile == xTileCount) ? xRemainder : width;
                int tileHeight = (yTile == yTileCount) ? yRemainder : height;
                int x = xOffset + (xTile * width);
                int y = yStart - ((yTile + 1) * height);
                if (tileWidth > 0 && tileHeight > 0) {
                    int maskRight = width - tileWidth;
                    int maskTop = height - tileHeight;
                    float uOffset = (maskRight / (float) width) * uSize;
                    float vOffset = (maskTop / (float) height) * vSize;

                    draw(bufferBuilder, matrix, uMin, vMin + vOffset, uMax - uOffset, vMax, x, y + maskTop, tileWidth, tileHeight);
                }
            }
        }
    }

    private static void draw(BufferBuilder bufferBuilder, Matrix4f matrix, float uMin, float vMin, float uMax, float vMax, int xOffset, int yOffset, int width, int height) {
        bufferBuilder.vertex(matrix, xOffset, yOffset + height, 0).uv(uMin, vMax).endVertex();
        bufferBuilder.vertex(matrix, xOffset + width, yOffset + height, 0).uv(uMax, vMax).endVertex();
        bufferBuilder.vertex(matrix, xOffset + width, yOffset, 0).uv(uMax, vMin).endVertex();
        bufferBuilder.vertex(matrix, xOffset, yOffset, 0).uv(uMin, vMin).endVertex();
    }
}
