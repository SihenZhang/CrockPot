package com.sihenzhang.crockpot.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin {
    /**
     * Inject {@link ArmorItem#dispenseArmor(BlockSource, ItemStack)} so that Dispenser will call
     * {@link net.minecraftforge.common.extensions.IForgeItemStack#canEquip(EquipmentSlot, Entity)} to check
     * the ItemStack can be equipped or not.
     *
     * @param blockSource    information about the Dispenser block
     * @param stack          ItemStack that is in the Dispenser
     * @param cir            Mixin CallbackInfoReturnable which is used to cancel the original method
     * @param pos            BlockPos that the Dispenser block facing
     * @param livingEntities List of LivingEntities that are in the <code>pos</code>
     * @param livingEntity   the first one of the <code>livingEntities</code>
     * @param equipmentSlot  EquipmentSlot of the <code>stack</code>
     */
    @Inject(
            method = "dispenseArmor(Lnet/minecraft/core/BlockSource;Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/entity/Mob;getEquipmentSlotForItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/EquipmentSlot;",
                    ordinal = 0
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private static void dispenseArmorHandler(BlockSource blockSource, ItemStack stack, CallbackInfoReturnable<Boolean> cir, BlockPos pos, List<LivingEntity> livingEntities, LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
        if (!stack.canEquip(equipmentSlot, livingEntity)) {
            cir.setReturnValue(false);
        }
    }
}
