package com.sihenzhang.crockpot.loot;

import com.mojang.serialization.Codec;
import com.sihenzhang.crockpot.CrockPot;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class CrockPotLootModifiers {
    private CrockPotLootModifiers() {
    }

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CrockPot.MOD_ID);

}
