package com.sihenzhang.crockpot.client.model;

import com.google.common.collect.ImmutableList;
import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class MilkmadeHatModel<T extends LivingEntity> extends AgeableListModel<T> implements HeadedModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CrockPot.MOD_ID, "milkmade_hat"), "main");
    private final ModelPart hat;

    public MilkmadeHatModel(ModelPart root) {
        this.hat = root.getChild("hat");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 19).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -7.99F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
                .texOffs(32, 0).addBox(-4.0F, -9.01F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bottle1 = hat.addOrReplaceChild("bottle1", CubeListBuilder.create().texOffs(56, 16).mirror().addBox(-8.05F, -4.5F, -1.25F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(30, 16).mirror().addBox(-9.05F, -11.5F, -2.25F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 16).mirror().addBox(-8.06F, -9.5F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 24).mirror().addBox(-10.05F, -8.5F, -3.25F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition straw1 = bottle1.addOrReplaceChild("straw1", CubeListBuilder.create().texOffs(60, 19).addBox(-11.6F, -31.4F, -0.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition straw2 = bottle1.addOrReplaceChild("straw2", CubeListBuilder.create().texOffs(56, 19).addBox(-3.6F, -31.4F, -0.74F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition bottle2 = hat.addOrReplaceChild("bottle2", CubeListBuilder.create().texOffs(56, 16).addBox(6.05F, -4.5F, -1.25F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(30, 16).addBox(5.05F, -11.5F, -2.25F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 16).addBox(6.06F, -9.5F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 24).addBox(4.05F, -8.5F, -3.25F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition straw3 = bottle2.addOrReplaceChild("straw3", CubeListBuilder.create().texOffs(60, 19).mirror().addBox(10.6F, -31.4F, -0.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition straw4 = bottle2.addOrReplaceChild("straw4", CubeListBuilder.create().texOffs(56, 19).mirror().addBox(2.6F, -31.4F, -0.74F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(hat);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float swimAmount = entity.getSwimAmount(limbSwing);
        hat.yRot = netHeadYaw * ((float) Math.PI / 180F);
        if (entity.getFallFlyingTicks() > 4) {
            hat.xRot = (-(float) Math.PI / 4F);
        } else if (swimAmount > 0.0F) {
            if (entity.isVisuallySwimming()) {
                hat.xRot = this.rotlerpRad(swimAmount, hat.xRot, (-(float) Math.PI / 4F));
            } else {
                hat.xRot = this.rotlerpRad(swimAmount, hat.xRot, headPitch * ((float) Math.PI / 180F));
            }
        } else {
            hat.xRot = headPitch * ((float) Math.PI / 180F);
        }
        if (entity.isCrouching()) {
            hat.y = 4.2F;
        } else {
            hat.y = 0.0F;
        }
    }

    protected float rotlerpRad(float angle, float maxAngle, float mul) {
        float f = (mul - maxAngle) % ((float) Math.PI * 2F);
        if (f < -(float) Math.PI) {
            f += ((float) Math.PI * 2F);
        }

        if (f >= (float) Math.PI) {
            f -= ((float) Math.PI * 2F);
        }

        return maxAngle + angle * f;
    }

    @Override
    public ModelPart getHead() {
        return hat;
    }
}
