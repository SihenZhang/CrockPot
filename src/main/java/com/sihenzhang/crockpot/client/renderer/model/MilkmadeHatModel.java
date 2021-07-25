package com.sihenzhang.crockpot.client.renderer.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class MilkmadeHatModel<T extends LivingEntity> extends BipedModel<T> {
    public MilkmadeHatModel() {
        super(1.0F);

        texWidth = 64;
        texHeight = 32;

        this.head = new ModelRenderer(this);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.texOffs(0, 19).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 3.0F, 10.0F, 0.0F, false);
        this.head.texOffs(0, 0).addBox(-4.0F, -7.99F, -4.0F, 8.0F, 8.0F, 8.0F, 0.6F, false);
        this.head.texOffs(32, 0).addBox(-4.0F, -9.01F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        ModelRenderer bottle1 = new ModelRenderer(this);
        bottle1.setPos(0.0F, 0.0F, 0.0F);
        this.head.addChild(bottle1);
        bottle1.texOffs(56, 16).addBox(-8.05F, -4.5F, -1.25F, 2.0F, 1.0F, 2.0F, 0.0F, true);
        bottle1.texOffs(30, 16).addBox(-9.05F, -11.5F, -2.25F, 4.0F, 7.0F, 4.0F, 0.0F, true);
        bottle1.texOffs(48, 16).addBox(-8.06F, -9.5F, -1.25F, 2.0F, 4.0F, 2.0F, 0.0F, true);
        bottle1.texOffs(40, 24).addBox(-10.05F, -8.5F, -3.25F, 6.0F, 2.0F, 6.0F, 0.01F, true);

        ModelRenderer straw1 = new ModelRenderer(this);
        straw1.setPos(0.0F, 28.0F, 0.0F);
        bottle1.addChild(straw1);
        setRotationAngle(straw1, 0.0F, 0.0F, 0.1309F);
        straw1.texOffs(60, 19).addBox(-11.6F, -31.4F, -0.75F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer straw2 = new ModelRenderer(this);
        straw2.setPos(0.0F, 28.0F, 0.0F);
        bottle1.addChild(straw2);
        setRotationAngle(straw2, 0.0F, 0.0F, -0.1309F);
        straw2.texOffs(56, 19).addBox(-3.6F, -31.4F, -0.74F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer bottle2 = new ModelRenderer(this);
        bottle2.setPos(0.0F, 0.0F, 0.0F);
        this.head.addChild(bottle2);
        bottle2.texOffs(56, 16).addBox(6.05F, -4.5F, -1.25F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        bottle2.texOffs(30, 16).addBox(5.05F, -11.5F, -2.25F, 4.0F, 7.0F, 4.0F, 0.0F, false);
        bottle2.texOffs(48, 16).addBox(6.06F, -9.5F, -1.25F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        bottle2.texOffs(40, 24).addBox(4.05F, -8.5F, -3.25F, 6.0F, 2.0F, 6.0F, 0.01F, false);

        ModelRenderer straw3 = new ModelRenderer(this);
        straw3.setPos(0.0F, 28.0F, 0.0F);
        bottle2.addChild(straw3);
        setRotationAngle(straw3, 0.0F, 0.0F, -0.1309F);
        straw3.texOffs(60, 19).addBox(10.6F, -31.4F, -0.75F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        ModelRenderer straw4 = new ModelRenderer(this);
        straw4.setPos(0.0F, 28.0F, 0.0F);
        bottle2.addChild(straw4);
        setRotationAngle(straw4, 0.0F, 0.0F, 0.1309F);
        straw4.texOffs(56, 19).addBox(2.6F, -31.4F, -0.74F, 1.0F, 2.0F, 1.0F, 0.0F, true);
    }

    @Override
    protected Iterable<ModelRenderer> bodyParts() {
        return ImmutableList.of();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(entity instanceof ArmorStandEntity)) {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            return;
        }

        ArmorStandEntity armorStandEntity = (ArmorStandEntity) entity;
        setRotationAngle(this.head, ((float) Math.PI / 180F) * armorStandEntity.getHeadPose().getX(), ((float) Math.PI / 180F) * armorStandEntity.getHeadPose().getY(), ((float) Math.PI / 180F) * armorStandEntity.getHeadPose().getZ());
        this.head.setPos(0.0F, 1.0F, 0.0F);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
