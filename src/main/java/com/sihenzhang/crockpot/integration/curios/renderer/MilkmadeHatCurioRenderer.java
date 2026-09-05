package com.sihenzhang.crockpot.integration.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.crockpot.client.model.MilkmadeHatModel;
import com.sihenzhang.crockpot.client.model.geom.CrockPotModelLayers;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class MilkmadeHatCurioRenderer implements ICurioRenderer {
    private static final Identifier MILKMADE_HAT_TEXTURE = IdUtil.mod("textures/entity/milkmade_hat.png");
    private final MilkmadeHatModel<HumanoidRenderState> model;

    public MilkmadeHatCurioRenderer() {
        model = new MilkmadeHatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CrockPotModelLayers.MILKMADE_HAT));
    }

    public static void register() {
        ICurioRenderer.register(ModItems.MILKMADE_HAT.get(), MilkmadeHatCurioRenderer::new);
        ICurioRenderer.register(ModItems.CREATIVE_MILKMADE_HAT.get(), MilkmadeHatCurioRenderer::new);
    }

    @Override
    public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack poseStack, SubmitNodeCollector collector,
            int packedLight, S renderState, RenderLayerParent<S, M> renderLayerParent,
            EntityRendererProvider.Context context, float yRotation, float xRotation) {
        if (renderState instanceof HumanoidRenderState humanoidState) {
            model.setupAnim(humanoidState);
            collector.order(1).submitModel(model, humanoidState, poseStack,
                    RenderTypes.armorCutoutNoCull(MILKMADE_HAT_TEXTURE), packedLight,
                    OverlayTexture.NO_OVERLAY, -1, null, renderState.outlineColor, null);
            if (stack.hasFoil()) {
                collector.order(2).submitModel(model, humanoidState, poseStack,
                        RenderTypes.armorEntityGlint(), packedLight,
                        OverlayTexture.NO_OVERLAY, -1, null, renderState.outlineColor, null);
            }
        }
    }
}
