package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    private ModEntities() {
    }

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, CrockPot.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Birdcage>> BIRDCAGE = ENTITIES.register("birdcage", () -> EntityType.Builder.of(Birdcage::new, MobCategory.MISC).sized(0.0001F, 0.0001F).setUpdateInterval(20).setTrackingRange(256).build(RLUtils.mod("birdcage").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownParrotEgg>> PARROT_EGG = ENTITIES.register("parrot_egg", () -> EntityType.Builder.<ThrownParrotEgg>of(ThrownParrotEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build(RLUtils.mod("parrot_egg").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<VoltGoat>> VOLT_GOAT = ENTITIES.register("volt_goat", () -> EntityType.Builder.of(VoltGoat::new, MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(10).build(RLUtils.mod("volt_goat").toString()));
}
