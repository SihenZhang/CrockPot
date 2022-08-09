package com.sihenzhang.crockpot.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BirdcageEntity extends Entity {
    public BirdcageEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) {
            if (this.getPassengers().isEmpty()) {
                this.discard();
            }
        }
    }
}
