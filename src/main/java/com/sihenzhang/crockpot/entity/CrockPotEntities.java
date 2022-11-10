package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CrockPotEntities {
    private CrockPotEntities() {
    }

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, CrockPot.MOD_ID);

    public static final RegistryObject<EntityType<Birdcage>> BIRDCAGE = ENTITIES.register("birdcage", () -> EntityType.Builder.of(Birdcage::new, MobCategory.MISC).sized(0.0001F, 0.0001F).setUpdateInterval(20).setTrackingRange(256).build(RLUtils.createRL("birdcage").toString()));
    public static final RegistryObject<EntityType<ThrownParrotEgg>> PARROT_EGG = ENTITIES.register("parrot_egg", () -> EntityType.Builder.<ThrownParrotEgg>of(ThrownParrotEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build(RLUtils.createRL("parrot_egg").toString()));
}
