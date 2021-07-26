package com.sihenzhang.crockpot.integration.curios;

import com.google.common.base.Preconditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosUtils {
    public static boolean anyMatchInEquippedCurios(PlayerEntity player, ItemStack stack) {
        return anyMatchInEquippedCurios(player, stack.getItem());
    }

    public static boolean anyMatchInEquippedCurios(PlayerEntity player, Item item) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(player).map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemHandler.getStackInSlot(i).getItem() == item) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    public static boolean anyMatchInEquippedCurios(PlayerEntity player, Class<? extends Item> itemClass) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(player).map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemClass.isInstance(itemHandler.getStackInSlot(i).getItem())) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    public static <T extends LivingEntity, M extends EntityModel<T>> void copyPropertiesFromLivingEntityModelTo(T livingEntity, Object model) {
        Preconditions.checkArgument(model instanceof EntityModel, "model should be an instance of EntityModel");
        @SuppressWarnings("unchecked") M entityModel = (M) model;
        EntityRenderer<? super T> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
        if (entityRenderer instanceof LivingRenderer) {
            @SuppressWarnings("unchecked") LivingRenderer<T, M> livingRenderer = (LivingRenderer<T, M>) entityRenderer;
            M livingEntityModel = livingRenderer.getModel();
            livingEntityModel.copyPropertiesTo(entityModel);
        }
    }
}
