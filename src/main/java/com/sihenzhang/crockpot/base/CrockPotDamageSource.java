package com.sihenzhang.crockpot.base;

import net.minecraft.util.DamageSource;

public final class CrockPotDamageSource {
    public static final DamageSource MONSTER_FOOD = new DamageSource("crock_pot.monster_food").setDamageBypassesArmor();
    public static final DamageSource TAFFY = new DamageSource("crock_pot.taffy").setDamageBypassesArmor();
}
