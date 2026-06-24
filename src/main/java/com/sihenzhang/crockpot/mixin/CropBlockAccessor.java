package com.sihenzhang.crockpot.mixin;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CropBlock.class)
public interface CropBlockAccessor {
    /**
     * Exposes the {@code protected} {@link CropBlock#getBonemealAgeIncrease} method via a Mixin
     * {@link Invoker} accessor rather than an Access Transformer (AT). An AT directive only
     * widens the visibility of the method it directly references; overriding methods in subclasses
     * are left untouched and therefore retain their original (more restrictive) visibility. Because
     * {@link CropBlock#getBonemealAgeIncrease} is overridden in several vanilla subclasses, applying
     * an AT to it would cause the JVM to throw an error at runtime due to the visibility mismatch
     * between the transformed base method and its non-transformed overrides.
     */
    @Invoker("getBonemealAgeIncrease")
    int invokeGetBonemealAgeIncrease(Level level);
}
