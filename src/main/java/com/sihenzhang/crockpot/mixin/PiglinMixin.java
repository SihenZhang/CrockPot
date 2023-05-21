package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Piglin.class)
public abstract class PiglinMixin extends AbstractPiglin {
    private PiglinMixin(EntityType<? extends AbstractPiglin> p_34652_, Level p_34653_) {
        super(p_34652_, p_34653_);
    }

    /**
     * Inject {@link Piglin#holdInOffHand(ItemStack)} so that Piglin will not drop items that can be used for
     * Special Piglin Bartering when the Piglin is killed.
     *
     * @param itemStack the ItemStack which will be hold in offhand
     * @param ci        Mixin CallbackInfo which is used to cancel the original method
     */
    @Inject(
            method = "holdInOffHand(Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/piglin/Piglin;setItemSlotAndDropWhenKilled(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void holdInOffHandHandler(ItemStack itemStack, CallbackInfo ci) {
        if (!itemStack.is(ItemTags.PIGLIN_REPELLENTS) && !IPiglinAiMixin.callIsFood(itemStack) && level.getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SimpleContainer(itemStack), level).isPresent()) {
            this.setItemSlot(EquipmentSlot.OFFHAND, itemStack);
            this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
            ci.cancel();
        }
    }

    /**
     * Inject {@link Piglin#getArmPose()} so that Piglin will pose correctly when admiring items that can be used for
     * Special Piglin Bartering.
     *
     * @param cir Mixin CallbackInfoReturnable which is used to cancel the original method
     */
    @Inject(
            method = "getArmPose()Lnet/minecraft/world/entity/monster/piglin/PiglinArmPose;",
            at = @At(value = "JUMP", opcode = Opcodes.IFEQ, ordinal = 2),
            cancellable = true
    )
    private void getArmPoseHandler(CallbackInfoReturnable<PiglinArmPose> cir) {
        var offhandStack = this.getOffhandItem();
        if (!offhandStack.is(ItemTags.PIGLIN_REPELLENTS) && !IPiglinAiMixin.callIsFood(offhandStack) && level.getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SimpleContainer(offhandStack), level).isPresent()) {
            cir.setReturnValue(PiglinArmPose.ADMIRING_ITEM);
        }
    }
}
