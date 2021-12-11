package com.sihenzhang.crockpot.mixin;

import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PiglinAi.class)
public interface IPiglinAiMixin {
    @Invoker
    static boolean callIsFood(ItemStack stack) {
        return true;
    }
}
