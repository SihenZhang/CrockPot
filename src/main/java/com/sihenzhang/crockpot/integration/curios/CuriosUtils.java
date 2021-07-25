package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.client.renderer.model.MilkmadeHatModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
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

    public static <T extends LivingEntity, M extends EntityModel<T>> void copyModelProperties(T livingEntity, M model) {
        EntityRenderer<? super T> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
        if (entityRenderer instanceof LivingRenderer) {
            LivingRenderer<T, M> livingRenderer = (LivingRenderer<T, M>) entityRenderer;
            M entityModel = livingRenderer.getModel();
            entityModel.copyPropertiesTo(model);
        }
    }

    public static void copyProperties(LivingEntity livingEntity, MilkmadeHatModel<LivingEntity> model) {
        EntityRenderer<? super LivingEntity> render = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
        if (render instanceof LivingRenderer) {
            @SuppressWarnings("unchecked") LivingRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer = (LivingRenderer<LivingEntity, EntityModel<LivingEntity>>) render;
            EntityModel<LivingEntity> entityModel = livingRenderer.getModel();
            entityModel.copyPropertiesTo(model);
        }
    }
}
