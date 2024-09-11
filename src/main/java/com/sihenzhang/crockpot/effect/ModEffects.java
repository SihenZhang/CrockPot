package com.sihenzhang.crockpot.effect;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.RLUtils;
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

    public static final DeferredHolder<MobEffect, MobEffect> CHARGE = EFFECTS.register("charge", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x4ea4ff).addAttributeModifier(Attributes.ATTACK_DAMAGE, RLUtils.mod("effect.charge"), 0.35, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static final DeferredHolder<MobEffect, MobEffect> GNAWS_GIFT = EFFECTS.register("gnaws_gift", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x650808));
    public static final DeferredHolder<MobEffect, MobEffect> OCEAN_AFFINITY = EFFECTS.register("ocean_affinity", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x15ddf4).addAttributeModifier(NeoForgeMod.SWIM_SPEED, RLUtils.mod("effect.ocean_affinity"), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static final DeferredHolder<MobEffect, MobEffect> WELL_FED = EFFECTS.register("well_fed", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0xda765b).addAttributeModifier(Attributes.ARMOR, RLUtils.mod("effect.well_fed.armor"), 1.0, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(Attributes.ATTACK_DAMAGE, RLUtils.mod("effect.well_fed.attack_damage"), 1.0, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> WITHER_RESISTANCE = EFFECTS.register("wither_resistance", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x72008f));
}
