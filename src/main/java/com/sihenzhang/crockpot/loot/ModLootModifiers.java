package com.sihenzhang.crockpot.loot;

import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.CrockPot;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ModLootModifiers {
    private ModLootModifiers() {
    }

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CrockPot.MOD_ID);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", () -> AddItemModifier.CODEC);
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_ITEM_WITH_LOOTING_ENCHANT = LOOT_MODIFIERS.register("add_item_with_looting_enchant", () -> AddItemWithLootingEnchantModifier.CODEC);
}
