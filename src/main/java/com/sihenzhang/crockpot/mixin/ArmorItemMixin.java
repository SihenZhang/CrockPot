package com.sihenzhang.crockpot.mixin;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin {
    /**
     * Inject {@link ArmorItem#dispenseArmor(IBlockSource, ItemStack)} so that Dispenser will call
     * {@link IForgeItemStack#canEquip(EquipmentSlotType, Entity)} to check the ItemStack can be equipped or not.
     *
     * @param blockSource    information about the Dispenser block
     * @param stack          ItemStack that is in the Dispenser
     * @param cir            Mixin CallbackInfoReturnable which is used to cancel the original method
     * @param pos            BlockPos that the Dispenser block facing
     * @param livingEntities List of LivingEntities that are in the <code>pos</code>
     * @param livingEntity   the first one of the <code>livingEntities</code>
     * @param armorType      EquipmentSlotType of the <code>stack</code>
     * @param equippedStack  ItemStack that will be equipped
     */
    @Inject(
            method = "dispenseArmor(Lnet/minecraft/dispenser/IBlockSource;Lnet/minecraft/item/ItemStack;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;setItemSlot(Lnet/minecraft/inventory/EquipmentSlotType;Lnet/minecraft/item/ItemStack;)V",
                    ordinal = 0
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private static void dispenseArmorHandler(IBlockSource blockSource, ItemStack stack, CallbackInfoReturnable<Boolean> cir, BlockPos pos, List<LivingEntity> livingEntities, LivingEntity livingEntity, EquipmentSlotType armorType, ItemStack equippedStack) {
        if (!equippedStack.canEquip(armorType, livingEntity)) {
            cir.setReturnValue(false);
        }
    }
}
