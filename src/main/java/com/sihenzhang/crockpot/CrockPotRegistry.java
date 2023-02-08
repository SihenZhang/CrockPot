package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.loot.CrockPotUnknownSeedsDropModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class CrockPotRegistry {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, CrockPot.MOD_ID);

    // Loot Modifiers
    static {
        LOOT_MODIFIER_SERIALIZERS.register("unknown_seeds_drop", CrockPotUnknownSeedsDropModifier.Serializer::new);
    }
}
