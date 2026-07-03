package com.sihenzhang.crockpot.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;

public class MilkmadeHatModel<T extends HumanoidRenderState> extends EntityModel<T> {
    private final ModelPart hat;

    public MilkmadeHatModel(ModelPart root) {
        super(root);
        this.hat = root.getChild("hat");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        PartDefinition hat = root.addOrReplaceChild(
                "hat",
                CubeListBuilder.create()
                        .texOffs(0, 19).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-4.0F, -7.99F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
                        .texOffs(32, 0).addBox(-4.0F, -9.01F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );
        PartDefinition bottle1 = hat.addOrReplaceChild(
                "bottle1",
                CubeListBuilder.create()
                        .texOffs(56, 16).mirror().addBox(-8.05F, -4.5F, -1.25F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(30, 16).mirror().addBox(-9.05F, -11.5F, -2.25F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(48, 16).mirror().addBox(-8.06F, -9.5F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(40, 24).mirror().addBox(-10.05F, -8.5F, -3.25F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false),
                PartPose.ZERO
        );
        bottle1.addOrReplaceChild("straw1", CubeListBuilder.create().texOffs(60, 19).addBox(-11.6F, -31.4F, -0.75F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, 0.1309F));
        bottle1.addOrReplaceChild("straw2", CubeListBuilder.create().texOffs(56, 19).addBox(-3.6F, -31.4F, -0.74F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, -0.1309F));
        PartDefinition bottle2 = hat.addOrReplaceChild(
                "bottle2",
                CubeListBuilder.create()
                        .texOffs(56, 16).addBox(6.05F, -4.5F, -1.25F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(30, 16).addBox(5.05F, -11.5F, -2.25F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 16).addBox(6.06F, -9.5F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(40, 24).addBox(4.05F, -8.5F, -3.25F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F)),
                PartPose.ZERO
        );
        bottle2.addOrReplaceChild("straw3", CubeListBuilder.create().texOffs(60, 19).mirror().addBox(10.6F, -31.4F, -0.75F, 1.0F, 2.0F, 1.0F).mirror(false), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, -0.1309F));
        bottle2.addOrReplaceChild("straw4", CubeListBuilder.create().texOffs(56, 19).mirror().addBox(2.6F, -31.4F, -0.74F, 1.0F, 2.0F, 1.0F).mirror(false), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, 0.1309F));
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void setupAnim(T state) {
        super.setupAnim(state);
        if (state instanceof ArmorStandRenderState armorStand) {
            hat.setRotation(
                    armorStand.headPose.x() * Mth.DEG_TO_RAD,
                    armorStand.headPose.y() * Mth.DEG_TO_RAD,
                    armorStand.headPose.z() * Mth.DEG_TO_RAD
            );
            hat.setPos(0.0F, 1.0F, 0.0F);
            return;
        }
        hat.yRot = state.yRot * Mth.DEG_TO_RAD;
        if (state.isFallFlying) {
            hat.xRot = (float) (-Math.PI / 4.0);
        } else if (state.swimAmount > 0.0F) {
            hat.xRot = state.isVisuallySwimming
                    ? Mth.rotLerpRad(state.swimAmount, hat.xRot, (float) (-Math.PI / 4.0))
                    : Mth.rotLerpRad(state.swimAmount, hat.xRot, state.xRot * Mth.DEG_TO_RAD);
        } else {
            hat.xRot = state.xRot * Mth.DEG_TO_RAD;
        }
        hat.y = state.isCrouching ? 4.2F : 0.0F;
    }
}
