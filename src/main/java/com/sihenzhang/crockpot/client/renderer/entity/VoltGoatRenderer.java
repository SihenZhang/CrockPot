package com.sihenzhang.crockpot.client.renderer.entity;

import com.sihenzhang.crockpot.client.model.VoltGoatModel;
import com.sihenzhang.crockpot.client.model.geom.CrockPotModelLayers;
import com.sihenzhang.crockpot.client.renderer.entity.layers.VoltGoatPowerLayer;
import com.sihenzhang.crockpot.entity.VoltGoat;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class VoltGoatRenderer extends MobRenderer<VoltGoat, VoltGoatModel<VoltGoat>> {
    private static final ResourceLocation VOLT_GOAT_LOCATION = RLUtils.createRL("textures/entity/volt_goat.png");

    public VoltGoatRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new VoltGoatModel<>(pContext.bakeLayer(CrockPotModelLayers.VOLT_GOAT)), 0.7F);
        this.addLayer(new VoltGoatPowerLayer(this, pContext.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(VoltGoat pEntity) {
        return VOLT_GOAT_LOCATION;
    }
}
