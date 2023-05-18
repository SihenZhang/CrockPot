package com.sihenzhang.crockpot.base;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public final class CrockPotDamageSource {
    public static final ResourceKey<DamageType> CANDY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(CrockPot.MOD_ID, "candy"));
    public static final ResourceKey<DamageType> MONSTER_FOOD = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(CrockPot.MOD_ID, "monster_food"));
    public static final ResourceKey<DamageType> POW_CAKE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(CrockPot.MOD_ID, "pow_cake"));
    public static final ResourceKey<DamageType> SPICY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(CrockPot.MOD_ID, "spicy"));
    public static final ResourceKey<DamageType> TAFFY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(CrockPot.MOD_ID, "taffy"));
}
