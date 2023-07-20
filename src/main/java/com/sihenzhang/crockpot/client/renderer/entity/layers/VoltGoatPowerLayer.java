package com.sihenzhang.crockpot.client.renderer.entity.layers;

import com.sihenzhang.crockpot.client.model.VoltGoatModel;
import com.sihenzhang.crockpot.client.model.geom.CrockPotModelLayers;
import com.sihenzhang.crockpot.entity.VoltGoat;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class VoltGoatPowerLayer extends EnergySwirlLayer<VoltGoat, VoltGoatModel<VoltGoat>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    private final VoltGoatModel<VoltGoat> model;

    public VoltGoatPowerLayer(RenderLayerParent<VoltGoat, VoltGoatModel<VoltGoat>> pRenderer, EntityModelSet pModelSet) {
        super(pRenderer);
        this.model = new VoltGoatModel<>(pModelSet.bakeLayer(CrockPotModelLayers.VOLT_GOAT_ARMOR));
    }

    @Override
    protected float xOffset(float pTickCount) {
        return pTickCount * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    @Override
    protected EntityModel<VoltGoat> model() {
        return model;
    }
}
