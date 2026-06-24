package com.sihenzhang.crockpot.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.crockpot.client.model.MilkmadeHatModel;
import com.sihenzhang.crockpot.client.model.geom.CrockPotModelLayers;
import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class MilkmadeHatLayer<S extends HumanoidRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
    private static final Identifier MILKMADE_HAT_TEXTURE = IdUtil.mod("textures/entity/milkmade_hat.png");
    private final MilkmadeHatModel<S> milkmadeHatModel;

    public MilkmadeHatLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.milkmadeHatModel = new MilkmadeHatModel<>(modelSet.bakeLayer(CrockPotModelLayers.MILKMADE_HAT));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        if (state.headEquipment.getItem() instanceof MilkmadeHatItem) {
            milkmadeHatModel.setupAnim(state);
            submitNodeCollector.order(1)
                    .submitModel(milkmadeHatModel, state, poseStack, MILKMADE_HAT_TEXTURE, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        }
    }
}
