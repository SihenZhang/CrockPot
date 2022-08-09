package com.sihenzhang.crockpot.client.renderer.entity;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class EmptyRenderer<T extends Entity> extends EntityRenderer<T> {
    public EmptyRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public boolean shouldRender(T pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return null;
    }
}
