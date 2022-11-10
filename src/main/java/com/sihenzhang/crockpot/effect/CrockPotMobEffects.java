package com.sihenzhang.crockpot.effect;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CrockPotMobEffects {
    private CrockPotMobEffects() {
    }

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CrockPot.MOD_ID);

    public static final RegistryObject<MobEffect> GNAWS_GIFT = MOB_EFFECTS.register("gnaws_gift", () -> new CrockPotMobEffect(MobEffectCategory.BENEFICIAL, 0x650808));
    public static final RegistryObject<MobEffect> OCEAN_AFFINITY = MOB_EFFECTS.register("ocean_affinity", () -> new CrockPotMobEffect(MobEffectCategory.BENEFICIAL, 0x15ddf4).addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "0216DFD0-874B-45F6-B030-D298D365C8D0", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> WELL_FED = MOB_EFFECTS.register("well_fed", () -> new CrockPotMobEffect(MobEffectCategory.BENEFICIAL, 0xda765b).addAttributeModifier(Attributes.ARMOR, "095FA141-E902-4BEF-99DB-DDC55213C07A", 1.0, AttributeModifier.Operation.ADDITION).addAttributeModifier(Attributes.ATTACK_DAMAGE, "5762F89C-8317-4021-B7EE-4DD93902941C", 1.0, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> WITHER_RESISTANCE = MOB_EFFECTS.register("wither_resistance", () -> new CrockPotMobEffect(MobEffectCategory.BENEFICIAL, 0x72008f));
}
