package com.sihenzhang.crockpot.base;

import net.minecraft.util.DamageSource;

public final class CrockPotDamageSource {
    public static final DamageSource MONSTER_FOOD = new DamageSource("crockpot.monster_food").setDamageBypassesArmor();
    public static final DamageSource POW_CAKE = new DamageSource("crockpot.pow_cake").setDamageBypassesArmor();
    public static final DamageSource TAFFY = new DamageSource("crockpot.taffy").setDamageBypassesArmor();
}
