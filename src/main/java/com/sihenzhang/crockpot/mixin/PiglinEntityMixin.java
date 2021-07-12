package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinAction;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends AbstractPiglinEntity {
    private PiglinEntityMixin(EntityType<? extends AbstractPiglinEntity> p_i241915_1_, World p_i241915_2_) {
        super(p_i241915_1_, p_i241915_2_);
    }

    /**
     * Inject {@link PiglinEntity#holdInOffHand(ItemStack)} so that Piglin will not drop items that can be used for
     * Special Piglin Bartering when the Piglin is killed.
     *
     * @param itemStack the ItemStack which will be hold in offhand
     * @param ci        Mixin CallbackInfo which is used to cancel the original method
     */
    @Inject(
            method = "holdInOffHand(Lnet/minecraft/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/monster/piglin/PiglinEntity;setItemSlotAndDropWhenKilled(Lnet/minecraft/inventory/EquipmentSlotType;Lnet/minecraft/item/ItemStack;)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void holdInOffHandHandler(ItemStack itemStack, CallbackInfo ci) {
        if (!itemStack.getItem().is(ItemTags.PIGLIN_REPELLENTS) && !IPiglinTasksMixin.callIsFood(itemStack.getItem()) && !CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.match(itemStack).isEmpty()) {
            this.setItemSlot(EquipmentSlotType.OFFHAND, itemStack);
            this.setGuaranteedDrop(EquipmentSlotType.OFFHAND);
            ci.cancel();
        }
    }

    /**
     * Inject {@link PiglinEntity#getArmPose()} so that Piglin will pose correctly when admiring items that can be used for
     * Special Piglin Bartering.
     *
     * @param cir Mixin CallbackInfoReturnable which is used to cancel the original method
     */
    @Inject(
            method = "getArmPose()Lnet/minecraft/entity/monster/piglin/PiglinAction;",
            at = @At(
                    value = "JUMP",
                    ordinal = 2,
                    opcode = 153
            ),
            cancellable = true
    )
    private void getArmPoseHandler(CallbackInfoReturnable<PiglinAction> cir) {
        if (!this.getOffhandItem().getItem().is(ItemTags.PIGLIN_REPELLENTS) && !IPiglinTasksMixin.callIsFood(this.getOffhandItem().getItem()) && !CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.match(this.getOffhandItem()).isEmpty()) {
            cir.setReturnValue(PiglinAction.ADMIRING_ITEM);
        }
    }
}
