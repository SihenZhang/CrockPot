package com.sihenzhang.crockpot.integration.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sihenzhang.crockpot.client.model.MilkmadeHatModel;
import com.sihenzhang.crockpot.client.model.geom.CrockPotModelLayers;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class MilkmadeHatCurioRenderer implements ICurioRenderer {
    private static final ResourceLocation MILKMADE_HAT_TEXTURE = RLUtils.createRL("textures/entity/milkmade_hat.png");
    private final MilkmadeHatModel<LivingEntity> milkmadeHatModel;

    public MilkmadeHatCurioRenderer() {
        this.milkmadeHatModel = new MilkmadeHatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CrockPotModelLayers.MILKMADE_HAT));
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderLayerParent.getModel().copyPropertiesTo((EntityModel<T>) milkmadeHatModel);
        milkmadeHatModel.prepareMobModel(slotContext.entity(), limbSwing, limbSwingAmount, partialTicks);
        milkmadeHatModel.setupAnim(slotContext.entity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(MILKMADE_HAT_TEXTURE), false, stack.hasFoil());
        milkmadeHatModel.renderToBuffer(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
