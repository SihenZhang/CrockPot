package com.sihenzhang.crockpot.loot;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CrockPotLootModifiers {
    private CrockPotLootModifiers() {
    }

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, CrockPot.MOD_ID);

    public static final RegistryObject<GlobalLootModifierSerializer<UnknownSeedsDropModifier>> UNKNOWN_SEEDS_DROP = LOOT_MODIFIER_SERIALIZERS.register("unknown_seeds_drop", UnknownSeedsDropModifier.Serializer::new);
}
