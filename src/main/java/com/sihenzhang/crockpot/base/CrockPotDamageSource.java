package com.sihenzhang.crockpot.base;

import net.minecraft.world.damagesource.DamageSource;

public final class CrockPotDamageSource {
    public static final DamageSource CANDY = new DamageSource("crockpot.candy").bypassArmor();
    public static final DamageSource MONSTER_FOOD = new DamageSource("crockpot.monster_food").bypassArmor();
    public static final DamageSource POW_CAKE = new DamageSource("crockpot.pow_cake").bypassArmor();
    public static final DamageSource SPICY = new DamageSource("crockpot.spicy").bypassArmor();
    public static final DamageSource TAFFY = new DamageSource("crockpot.taffy").bypassArmor();
}
