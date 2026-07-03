package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    private ModEntities() {
    }

    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(CrockPot.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Birdcage>> BIRDCAGE = ENTITIES.registerEntityType("birdcage", Birdcage::new, MobCategory.MISC,
            builder -> builder.sized(0.0001F, 0.0001F).setUpdateInterval(20).setTrackingRange(256));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownParrotEgg>> PARROT_EGG = ENTITIES.registerEntityType("parrot_egg", ThrownParrotEgg::new, MobCategory.MISC,
            builder -> builder.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final DeferredHolder<EntityType<?>, EntityType<VoltGoat>> VOLT_GOAT = ENTITIES.registerEntityType("volt_goat", VoltGoat::new, MobCategory.CREATURE,
            builder -> builder.sized(0.9F, 1.3F).clientTrackingRange(10));
}
