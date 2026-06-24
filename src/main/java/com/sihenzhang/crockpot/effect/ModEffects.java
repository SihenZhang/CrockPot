package com.sihenzhang.crockpot.effect;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEffects {
    private ModEffects() {
    }

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, CrockPot.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> CHARGE = EFFECTS.register(
            "charge",
            () -> new BaseMobEffect(MobEffectCategory.BENEFICIAL, 0x4EA4FF)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, IdUtil.mod("effect.charge.attack_damage"), 0.35, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final DeferredHolder<MobEffect, MobEffect> OCEAN_AFFINITY = EFFECTS.register(
            "ocean_affinity",
            () -> new BaseMobEffect(MobEffectCategory.BENEFICIAL, 0x15DDF4)
                    .addAttributeModifier(NeoForgeMod.SWIM_SPEED, IdUtil.mod("effect.ocean_affinity.swim_speed"), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final DeferredHolder<MobEffect, MobEffect> WELL_FED = EFFECTS.register(
            "well_fed",
            () -> new BaseMobEffect(MobEffectCategory.BENEFICIAL, 0xDA765B)
                    .addAttributeModifier(Attributes.ARMOR, IdUtil.mod("effect.well_fed.armor"), 1.0, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, IdUtil.mod("effect.well_fed.attack_damage"), 1.0, AttributeModifier.Operation.ADD_VALUE)
    );
    public static final DeferredHolder<MobEffect, MobEffect> WITHER_RESISTANCE = EFFECTS.register(
            "wither_resistance",
            () -> new BaseMobEffect(MobEffectCategory.BENEFICIAL, 0x72008F)
    );
    public static final DeferredHolder<MobEffect, MobEffect> GNAWS_GIFT = EFFECTS.register(
            "gnaws_gift",
            () -> new BaseMobEffect(MobEffectCategory.BENEFICIAL, 0x650808)
    );
}
