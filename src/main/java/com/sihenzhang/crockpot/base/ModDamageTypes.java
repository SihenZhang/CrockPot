package com.sihenzhang.crockpot.base;

import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public final class ModDamageTypes {
    private ModDamageTypes() {
    }

    public static final ResourceKey<DamageType> CANDY = ResourceKey.create(Registries.DAMAGE_TYPE, RLUtils.mod("candy"));
    public static final ResourceKey<DamageType> MONSTER_FOOD = ResourceKey.create(Registries.DAMAGE_TYPE, RLUtils.mod("monster_food"));
    public static final ResourceKey<DamageType> POW_CAKE = ResourceKey.create(Registries.DAMAGE_TYPE, RLUtils.mod("pow_cake"));
    public static final ResourceKey<DamageType> SPICY = ResourceKey.create(Registries.DAMAGE_TYPE, RLUtils.mod("spicy"));
    public static final ResourceKey<DamageType> TAFFY = ResourceKey.create(Registries.DAMAGE_TYPE, RLUtils.mod("taffy"));
}
