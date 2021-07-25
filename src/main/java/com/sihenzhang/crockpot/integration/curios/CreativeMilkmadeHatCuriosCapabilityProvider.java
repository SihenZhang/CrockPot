package com.sihenzhang.crockpot.integration.curios;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.client.renderer.model.MilkmadeHatModel;
import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CreativeMilkmadeHatCuriosCapabilityProvider implements ICapabilityProvider {
    private static final ResourceLocation MILKMADE_HAT_TEXTURE = new ResourceLocation(CrockPot.MOD_ID, "textures/entity/milkmade_hat.png");
    private final LazyOptional<ICurio> curioOptional;

    public CreativeMilkmadeHatCuriosCapabilityProvider(ItemStack stack, @Nullable CompoundNBT nbt) {
        ICurio curio = new ICurio() {
            private final MilkmadeHatModel<LivingEntity> milkmadeHatModel = new MilkmadeHatModel<>();

            @Override
            public void curioTick(String identifier, int index, LivingEntity livingEntity) {
                if (livingEntity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) livingEntity;
                    World world = player.getCommandSenderWorld();
                    if (!world.isClientSide && player.getFoodData().needsFood() && player.tickCount % 20 == 0) {
                        player.getFoodData().eat(1, 0.05F);
                    }
                }
            }

            @Override
            public boolean canEquip(String identifier, LivingEntity livingEntity) {
                if (livingEntity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) livingEntity;
                    return !CuriosUtils.anyMatchInEquippedCurios(player, MilkmadeHatItem.class);
                }
                return false;
            }

            @Override
            public boolean canEquipFromUse(SlotContext slotContext) {
                return true;
            }

            @Override
            public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
                return true;
            }

            @Override
            public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                EntityRenderer<? super LivingEntity> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
                if (entityRenderer instanceof LivingRenderer) {
                    @SuppressWarnings("unchecked") LivingRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer = (LivingRenderer<LivingEntity, EntityModel<LivingEntity>>) entityRenderer;
                    livingRenderer.getModel().copyPropertiesTo(this.milkmadeHatModel);
                }
                ICurio.RenderHelper.followHeadRotations(livingEntity, milkmadeHatModel.head);
                IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(MILKMADE_HAT_TEXTURE), false, stack.hasFoil());
                milkmadeHatModel.renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        };
        this.curioOptional = LazyOptional.of(() -> curio);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CuriosCapability.ITEM.orEmpty(cap, this.curioOptional);
    }
}
