package com.sihenzhang.crockpot.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 4.0.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports
public class MilkmadeHatModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CrockPot.MOD_ID, "milkmade_hat"), "main");
    private final ModelPart headscarf;
    private final ModelPart bottle1;
    private final ModelPart bottle2;

    public MilkmadeHatModel(ModelPart root) {
        this.headscarf = root.getChild("headscarf");
        this.bottle1 = root.getChild("bottle1");
        this.bottle2 = root.getChild("bottle2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition headscarf = partdefinition.addOrReplaceChild("headscarf", CubeListBuilder.create().texOffs(0, 19).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -7.99F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
                .texOffs(32, 0).addBox(-4.0F, -9.01F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bottle1 = partdefinition.addOrReplaceChild("bottle1", CubeListBuilder.create().texOffs(56, 16).mirror().addBox(-8.05F, -4.5F, -1.25F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(30, 16).mirror().addBox(-9.05F, -11.5F, -2.25F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 16).mirror().addBox(-8.06F, -9.5F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 24).mirror().addBox(-10.05F, -8.5F, -3.25F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition straw1 = bottle1.addOrReplaceChild("straw1", CubeListBuilder.create().texOffs(60, 19).addBox(-11.6F, -31.4F, -0.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition straw2 = bottle1.addOrReplaceChild("straw2", CubeListBuilder.create().texOffs(56, 19).addBox(-3.6F, -31.4F, -0.74F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition bottle2 = partdefinition.addOrReplaceChild("bottle2", CubeListBuilder.create().texOffs(56, 16).addBox(6.05F, -4.5F, -1.25F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(30, 16).addBox(5.05F, -11.5F, -2.25F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 16).addBox(6.06F, -9.5F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 24).addBox(4.05F, -8.5F, -3.25F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition straw3 = bottle2.addOrReplaceChild("straw3", CubeListBuilder.create().texOffs(60, 19).mirror().addBox(10.6F, -31.4F, -0.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition straw4 = bottle2.addOrReplaceChild("straw4", CubeListBuilder.create().texOffs(56, 19).mirror().addBox(2.6F, -31.4F, -0.74F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 28.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        headscarf.render(poseStack, buffer, packedLight, packedOverlay);
        bottle1.render(poseStack, buffer, packedLight, packedOverlay);
        bottle2.render(poseStack, buffer, packedLight, packedOverlay);
    }
}