package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.block.BirdcageBlock;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class Birdcage extends Entity {
    public Birdcage(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        // Do nothing because this entity has no synced data
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        // Do nothing because this entity has no additional data
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        // Do nothing because this entity has no additional data
    }

    @Override
    public boolean hurtServer(ServerLevel pLevel, DamageSource pDamageSource, float pAmount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && (this.getPassengers().isEmpty() || !(this.getBlockStateOn().getBlock() instanceof BirdcageBlock))) {
            this.discard();
        }
    }
}
