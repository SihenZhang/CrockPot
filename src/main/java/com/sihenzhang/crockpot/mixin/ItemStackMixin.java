package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.core.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    /**
     * Appends tooltip lines stored on {@link ModDataComponents#ITEM_TOOLTIPS} before vanilla
     * details are added. NeoForge's {@link net.neoforged.neoforge.event.entity.player.ItemTooltipEvent}
     * is fired after {@link ItemStack#addDetailsToTooltip}, so using the event cannot guarantee
     * these tooltips stay at the top of the tooltip.
     */
    @Inject(method = "addDetailsToTooltip", at = @At("HEAD"))
    private void addItemTooltipsComponent(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        ((ItemStack) (Object) this).addToTooltip(ModDataComponents.ITEM_TOOLTIPS, context, display, builder, tooltipFlag);
    }
}
