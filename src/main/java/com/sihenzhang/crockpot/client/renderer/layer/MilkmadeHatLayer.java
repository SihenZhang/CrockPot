package com.sihenzhang.crockpot.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.client.renderer.model.MilkmadeHatModel;
import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MilkmadeHatLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private static final ResourceLocation MILKMADE_HAT_TEXTURE = new ResourceLocation(CrockPot.MOD_ID, "textures/entity/milkmade_hat.png");
    private final MilkmadeHatModel<T> milkmadeHatModel = new MilkmadeHatModel<>();

    public MilkmadeHatLayer(IEntityRenderer<T, M> p_i50926_1_) {
        super(p_i50926_1_);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stackBySlot = entity.getItemBySlot(EquipmentSlotType.HEAD);
        if (stackBySlot.getItem() instanceof MilkmadeHatItem) {
            this.getParentModel().copyPropertiesTo(this.milkmadeHatModel);
            this.milkmadeHatModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
            this.milkmadeHatModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(MILKMADE_HAT_TEXTURE), false, stackBySlot.hasFoil());
            this.milkmadeHatModel.renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
