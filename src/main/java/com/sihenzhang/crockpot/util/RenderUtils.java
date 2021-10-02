package com.sihenzhang.crockpot.util;

import com.google.common.base.Preconditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

public final class RenderUtils {
    @SuppressWarnings("unchecked")
    public static <T extends LivingEntity, M extends EntityModel<T>> void copyPropertiesFromLivingEntityModelTo(T livingEntity, Object model) {
        Preconditions.checkArgument(model instanceof EntityModel, "model should be an instance of EntityModel");
        M entityModel = (M) model;
        EntityRenderer<? super T> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
        if (entityRenderer instanceof LivingRenderer) {
            LivingRenderer<T, M> livingRenderer = (LivingRenderer<T, M>) entityRenderer;
            M livingEntityModel = livingRenderer.getModel();
            livingEntityModel.copyPropertiesTo(entityModel);
        }
    }
}
