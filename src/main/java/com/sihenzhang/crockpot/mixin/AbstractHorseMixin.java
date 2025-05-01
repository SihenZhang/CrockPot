package com.sihenzhang.crockpot.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin {
    /**
     * Injects {@link AbstractHorse#isFood(ItemStack)} to make Steamed Stick a valid food item for horses.
     * This method intercepts the vanilla food check and returns true when the item is a Steamed Stick,
     * allowing horses to recognize it as food even though it's not in the vanilla food list.
     *
     * @param pStack The ItemStack being checked
     * @param cir Callback that can be used to override the return value
     */
    @Inject(
            method = "isFood(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isFoodHandler(ItemStack pStack, CallbackInfoReturnable<Boolean> cir) {
        if (pStack.is(CrockPotItems.STEAMED_STICKS.get())) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Modifies the result of item check in {@link AbstractHorse#handleEating(Player, ItemStack)} to include Steamed Stick.
     * This mixin ensures that when a player feeds a Steamed Stick to a horse, the handleEating method
     * processes it the same way as {@link net.minecraft.world.item.Items#WHEAT}. Without this mixin,
     * even though isFood returns true, the actual eating handling would not work correctly for this mod-added item.
     *
     * @param original The original result of vanilla item check
     * @param pStack The ItemStack being used to feed the horse
     * @return true if the item is either a vanilla food item or Steamed Stick
     */
    @ModifyExpressionValue(
            method = "handleEating(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
                    ordinal = 0
            )
    )
    private boolean handleEatingHandler(boolean original, @Local(argsOnly = true) ItemStack pStack) {
        return original || pStack.is(CrockPotItems.STEAMED_STICKS.get());
    }
}
