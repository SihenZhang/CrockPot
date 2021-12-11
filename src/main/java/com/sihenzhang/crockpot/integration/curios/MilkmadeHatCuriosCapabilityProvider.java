package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    public MilkmadeHatCuriosCapabilityProvider(ItemStack stack, @Nullable CompoundTag nbt) {
        this(stack, nbt, false);
    }

    public MilkmadeHatCuriosCapabilityProvider(ItemStack stack, @Nullable CompoundTag nbt, boolean isCreative) {
        this.curioOptional = LazyOptional.of(() -> new ICurio() {
            private Object model;

            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                LivingEntity entity = slotContext.entity();
                if (!entity.level.isClientSide) {
                    if (entity instanceof Player player) {
                        if (player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                            if (!isCreative) {
                                stack.hurtAndBreak(1, player, e -> CuriosApi.getCuriosHelper().onBrokenCurio(slotContext));
                            }
                            player.getFoodData().eat(1, 0.05F);
                            player.getCooldowns().addCooldown(stack.getItem(), isCreative ? 20 : 100);
                        }
                    }
                }
            }

            @Override
            public boolean canEquip(String identifier, LivingEntity livingEntity) {
                return !(livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem().getTags().contains(MilkmadeHatItem.TAG)) && !CuriosUtils.anyMatchInEquippedCurios(livingEntity, MilkmadeHatItem.TAG);
            }

            @Override
            public boolean canEquipFromUse(SlotContext slotContext) {
                return true;
            }

//            @Override
//            public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
//                if (!(this.model instanceof MilkmadeHatModel)) {
//                    model = new MilkmadeHatModel<>();
//                }
//                MilkmadeHatModel<?> milkmadeHatModel = (MilkmadeHatModel<?>) this.model;
//                RenderUtils.copyPropertiesFromLivingEntityModelTo(livingEntity, milkmadeHatModel);
//                ICurio.RenderHelper.followHeadRotations(livingEntity, milkmadeHatModel.head);
//                IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(MILKMADE_HAT_TEXTURE), false, stack.hasFoil());
//                milkmadeHatModel.renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//            }
        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CuriosCapability.ITEM.orEmpty(cap, this.curioOptional);
    }
}
