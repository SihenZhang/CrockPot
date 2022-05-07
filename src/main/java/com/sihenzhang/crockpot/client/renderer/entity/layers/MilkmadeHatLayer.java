package com.sihenzhang.crockpot.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sihenzhang.crockpot.client.model.MilkmadeHatModel;
import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MilkmadeHatLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation MILKMADE_HAT_TEXTURE = RLUtils.createRL("textures/entity/milkmade_hat.png");
    private final MilkmadeHatModel<T> milkmadeHatModel;

    public MilkmadeHatLayer(RenderLayerParent<T, M> renderer, EntityModelSet entityModelSet) {
        super(renderer);
        this.milkmadeHatModel = new MilkmadeHatModel<>(entityModelSet.bakeLayer(MilkmadeHatModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stackBySlot = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        if (stackBySlot.getItem() instanceof MilkmadeHatItem) {
            this.getParentModel().copyPropertiesTo(milkmadeHatModel);
            milkmadeHatModel.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
            milkmadeHatModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(MILKMADE_HAT_TEXTURE), false, stackBySlot.hasFoil());
            milkmadeHatModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
