package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private LivingEntityMixin(EntityType<?> type, World level) {
        super(type, level);
    }

    @Shadow
    public abstract boolean hasEffect(Effect p_70644_1_);

    @Shadow
    protected abstract float getWaterSlowDown();

    @Shadow
    public abstract float getSpeed();

    @ModifyVariable(
            method = "travel(Lnet/minecraft/util/math/vector/Vector3d;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraftforge/common/ForgeMod;SWIM_SPEED:Lnet/minecraftforge/fml/RegistryObject;",
                    ordinal = 0,
                    opcode = 178,
                    remap = false
            ),
            ordinal = 0
    )
    private float modifyWaterSlowDown(float originalWaterSlowDown) {
        if (this.hasEffect(CrockPotRegistry.oceanAffinity)) {
            if (this.hasEffect(Effects.DOLPHINS_GRACE)) {
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
            method = "travel(Lnet/minecraft/util/math/vector/Vector3d;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraftforge/common/ForgeMod;SWIM_SPEED:Lnet/minecraftforge/fml/RegistryObject;",
                    ordinal = 0,
                    opcode = 178,
                    remap = false
            ),
            ordinal = 1
    )
    private float modifyUnderwaterMovementSpeed(float originalUnderwaterMovementSpeed) {
        return this.hasEffect(CrockPotRegistry.oceanAffinity) ? originalUnderwaterMovementSpeed + this.getSpeed() * 0.1F : originalUnderwaterMovementSpeed;
    }
}
