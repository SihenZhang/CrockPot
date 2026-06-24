package com.sihenzhang.crockpot.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.crockpot.client.renderer.entity.state.VoltGoatRenderState;
import net.minecraft.client.model.animal.goat.BabyGoatModel;
import net.minecraft.client.model.animal.goat.GoatModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class VoltGoatPowerLayer extends RenderLayer<VoltGoatRenderState, GoatModel> {
    private static final Identifier POWER_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");

    private final GoatModel adultModel;
    private final GoatModel babyModel;

    public VoltGoatPowerLayer(RenderLayerParent<VoltGoatRenderState, GoatModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.adultModel = new GoatModel(modelSet.bakeLayer(ModelLayers.GOAT));
        this.babyModel = new BabyGoatModel(modelSet.bakeLayer(ModelLayers.GOAT_BABY));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, VoltGoatRenderState state, float yRot, float xRot) {
        if (state.isPowered) {
            float tickCount = state.ageInTicks;
            var model = state.isBaby ? this.babyModel : this.adultModel;
            submitNodeCollector.order(1)
                    .submitModel(
                            model,
                            state,
                            poseStack,
                            RenderTypes.energySwirl(POWER_LOCATION, tickCount * 0.01F % 1.0F, tickCount * 0.01F % 1.0F),
                            lightCoords,
                            OverlayTexture.NO_OVERLAY,
                            -8355712,
                            null,
                            state.outlineColor,
                            null
                    );
        }
    }
}
