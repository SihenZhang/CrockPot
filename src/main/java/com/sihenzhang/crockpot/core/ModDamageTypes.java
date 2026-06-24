package com.sihenzhang.crockpot.core;

import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public final class ModDamageTypes {
    private ModDamageTypes() {
    }

    public static final ResourceKey<DamageType> CANDY = create("candy");
    public static final ResourceKey<DamageType> MONSTER_FOOD = create("monster_food");
    public static final ResourceKey<DamageType> POW_CAKE = create("pow_cake");
    public static final ResourceKey<DamageType> SPICY = create("spicy");
    public static final ResourceKey<DamageType> TAFFY = create("taffy");

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, IdUtil.mod(name));
    }
}
