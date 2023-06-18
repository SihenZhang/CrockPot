package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.block.BirdcageBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Birdcage extends Entity {
    public Birdcage(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {
        // Do nothing because this entity has no synced data
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        // Do nothing because this entity has no additional data
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        // Do nothing because this entity has no additional data
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && (this.getPassengers().isEmpty() || !(this.getBlockStateOn().getBlock() instanceof BirdcageBlock))) {
            this.discard();
        }
    }
}
