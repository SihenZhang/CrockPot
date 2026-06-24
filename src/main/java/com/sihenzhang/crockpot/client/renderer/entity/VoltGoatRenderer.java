package com.sihenzhang.crockpot.client.renderer.entity;

import com.sihenzhang.crockpot.client.renderer.entity.layers.VoltGoatPowerLayer;
import com.sihenzhang.crockpot.client.renderer.entity.state.VoltGoatRenderState;
import com.sihenzhang.crockpot.entity.VoltGoat;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.client.model.animal.goat.BabyGoatModel;
import net.minecraft.client.model.animal.goat.GoatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

@SuppressWarnings("deprecation")
public class VoltGoatRenderer extends AgeableMobRenderer<VoltGoat, VoltGoatRenderState, GoatModel> {
    private static final Identifier VOLT_GOAT_LOCATION = IdUtil.mod("textures/entity/volt_goat.png");
    private static final Identifier BABY_VOLT_GOAT_LOCATION = IdUtil.mod("textures/entity/volt_goat_baby.png");

    public VoltGoatRenderer(EntityRendererProvider.Context context) {
        super(context, new GoatModel(context.bakeLayer(ModelLayers.GOAT)), new BabyGoatModel(context.bakeLayer(ModelLayers.GOAT_BABY)), 0.7F);
        this.addLayer(new VoltGoatPowerLayer(this, context.getModelSet()));
    }

    @Override
    public VoltGoatRenderState createRenderState() {
        return new VoltGoatRenderState();
    }

    @Override
    public void extractRenderState(VoltGoat entity, VoltGoatRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isPowered = entity.isPowered();
    }

    @Override
    public Identifier getTextureLocation(VoltGoatRenderState state) {
        return state.isBaby ? BABY_VOLT_GOAT_LOCATION : VOLT_GOAT_LOCATION;
    }
}
