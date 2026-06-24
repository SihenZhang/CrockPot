package com.sihenzhang.crockpot.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sihenzhang.crockpot.block.DryingRackBlock;
import com.sihenzhang.crockpot.block.entity.DryingRackBlockEntity;
import com.sihenzhang.crockpot.client.renderer.blockentity.state.DryingRackRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackBlockEntity, DryingRackRenderState> {
    private static final float FLOOR_ITEM_HEIGHT_PX = 10.5F;
    // The two visible rope pairs in the wall model end at these heights.
    // ItemDisplayContext.FIXED centres its transformed item below the submitted
    // origin, so the origin itself must be placed at the rope end.
    private static final float[] WALL_ITEM_HEIGHT_PX = {10.5F, 2.5F};
    private static final float WALL_ROPE_Z_PX = 15.99F;
    private static final float WALL_SURFACE_OFFSET_PX = 0.25F;
    private static final float BLOCK_CENTRE_PX = 8.0F;
    // Horizontal spacing (in pixels from the block centre) of the two items sharing one bar.
    private static final float SLOT_OFFSET_PX = 3.0F;
    private static final float FLOOR_STACK_OFFSET_PX = 4.0F;
    private static final float ITEM_SCALE = 6.0F / 16.0F;

    private final ItemModelResolver itemModelResolver;

    public DryingRackRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public DryingRackRenderState createRenderState() {
        return new DryingRackRenderState();
    }

    @Override
    public void extractRenderState(DryingRackBlockEntity blockEntity, DryingRackRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        var blockState = blockEntity.getBlockState();
        state.facing = blockState.getValue(DryingRackBlock.FACING);
        state.wall = blockState.getValue(DryingRackBlock.WALL);
        state.items.clear();
        var seed = (int) blockEntity.getBlockPos().asLong();
        var count = blockEntity.getActiveSlotCount();
        for (var slot = 0; slot < count; slot++) {
            var itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(itemState, blockEntity.getItem(slot), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seed + slot);
            state.items.add(itemState);
        }
    }

    @Override
    public void submit(DryingRackRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        var yaw = 180.0F - state.facing.toYRot();
        for (var slot = 0; slot < state.items.size(); slot++) {
            var itemState = state.items.get(slot);
            if (itemState.isEmpty()) {
                continue;
            }
            var tier = slot / DryingRackBlockEntity.SLOTS_PER_STACK;
            var indexInTier = slot % DryingRackBlockEntity.SLOTS_PER_STACK;
            var xOffset = (indexInTier == 0 ? -SLOT_OFFSET_PX : SLOT_OFFSET_PX) / 16.0F;
            var yOffset = (state.wall ? WALL_ITEM_HEIGHT_PX[Math.min(tier, WALL_ITEM_HEIGHT_PX.length - 1)] : FLOOR_ITEM_HEIGHT_PX) / 16.0F;
            var zOffset = getLocalZOffset(state, tier);
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.0F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.translate(xOffset, yOffset, zOffset);
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    private static float getLocalZOffset(DryingRackRenderState state, int tier) {
        if (state.wall) {
            // Keep the item just in front of the rope plane to avoid z-fighting.
            return (WALL_ROPE_Z_PX - WALL_SURFACE_OFFSET_PX - BLOCK_CENTRE_PX) / 16.0F;
        }
        if (state.items.size() > DryingRackBlockEntity.SLOTS_PER_STACK) {
            return (tier == 0 ? -FLOOR_STACK_OFFSET_PX : FLOOR_STACK_OFFSET_PX) / 16.0F;
        }
        return 0.0F;
    }
}
