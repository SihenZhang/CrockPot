package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Shadow
    public abstract boolean hasEffect(MobEffect potion);

    @Shadow
    protected abstract float getWaterSlowDown();

    @Shadow
    public abstract float getSpeed();

    @ModifyVariable(
            method = "travel(Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraftforge/common/ForgeMod;SWIM_SPEED:Lnet/minecraftforge/registries/RegistryObject;",
                    ordinal = 0,
                    opcode = 178,
                    remap = false
            ),
            ordinal = 0
    )
    private float modifyWaterSlowDown(float originalWaterSlowDown) {
        if (this.hasEffect(CrockPotRegistry.OCEAN_AFFINITY.get())) {
            if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                return 0.99F;
            }
            float waterSlowDown = this.isSprinting() ? 0.94F : this.getWaterSlowDown() + 0.06F;
            float depthStriderLevel = Math.min(EnchantmentHelper.getDepthStrider((LivingEntity) (Object) this), 3.0F);
            if (!this.onGround) {
                depthStriderLevel *= 0.5F;
            }
            if (depthStriderLevel > 0.0F) {
                waterSlowDown += (0.65F - waterSlowDown) * depthStriderLevel / 3.0F;
            }
            return waterSlowDown;
        }
        return originalWaterSlowDown;
    }

    @ModifyVariable(
            method = "travel(Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraftforge/common/ForgeMod;SWIM_SPEED:Lnet/minecraftforge/registries/RegistryObject;",
                    ordinal = 0,
                    opcode = 178,
                    remap = false
            ),
            ordinal = 1
    )
    private float modifyUnderwaterMovementSpeed(float originalUnderwaterMovementSpeed) {
        return this.hasEffect(CrockPotRegistry.OCEAN_AFFINITY.get()) ? originalUnderwaterMovementSpeed + this.getSpeed() * 0.1F : originalUnderwaterMovementSpeed;
    }
}
