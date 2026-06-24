package com.sihenzhang.crockpot.client.renderer.entity;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

public class EmptyRenderer<T extends Entity> extends EntityRenderer<T, EntityRenderState> {
    public EmptyRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(T entity, Frustum culler, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
