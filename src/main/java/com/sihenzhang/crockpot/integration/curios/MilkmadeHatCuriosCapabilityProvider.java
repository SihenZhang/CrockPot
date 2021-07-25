package com.sihenzhang.crockpot.integration.curios;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.client.renderer.model.MilkmadeHatModel;
import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MilkmadeHatCuriosCapabilityProvider implements ICapabilityProvider {
    private static final ResourceLocation MILKMADE_HAT_TEXTURE = new ResourceLocation(CrockPot.MOD_ID, "textures/entity/milkmade_hat.png");
    private final LazyOptional<ICurio> curioOptional;

    public MilkmadeHatCuriosCapabilityProvider(ItemStack stack, @Nullable CompoundNBT nbt) {
        ICurio curio = new ICurio() {
            private Object model;

            @Override
            public void curioTick(String identifier, int index, LivingEntity livingEntity) {
                if (livingEntity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) livingEntity;
                    if (!player.getCommandSenderWorld().isClientSide && player.getFoodData().needsFood() && player.tickCount % 100 == 0) {
                        if (player.getItemBySlot(EquipmentSlotType.HEAD).getItem() == CrockPotRegistry.creativeMilkmadeHat) {
                            return;
                        }
                        stack.hurtAndBreak(1, player, e -> CuriosApi.getCuriosHelper().onBrokenCurio(identifier, index, e));
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
                if (!(this.model instanceof MilkmadeHatModel)) {
                    model = new MilkmadeHatModel<>();
                }
                MilkmadeHatModel<LivingEntity> milkmadeHatModel = (MilkmadeHatModel<LivingEntity>) this.model;
                CuriosUtils.copyModelProperties(livingEntity, milkmadeHatModel);
                ICurio.RenderHelper.followHeadRotations(livingEntity, milkmadeHatModel.head);
                IVertexBuilder vertexBuilder = ItemRenderer.getFoilBuffer(renderTypeBuffer, milkmadeHatModel.renderType(MILKMADE_HAT_TEXTURE), false, stack.hasFoil());
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
