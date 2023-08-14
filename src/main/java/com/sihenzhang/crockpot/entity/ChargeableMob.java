package com.sihenzhang.crockpot.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.PowerableMob;

public interface ChargeableMob extends PowerableMob {
    String TAG_CHARGE_TIME = "ChargeTime";

    int getRemainingPersistentChargeTime();

    void setRemainingPersistentChargeTime(int pRemainingPersistentChargeTime);

    void startPersistentChargeTimer();

    default void addPersistentChargeSaveData(CompoundTag pNbt) {
        pNbt.putInt(TAG_CHARGE_TIME, this.getRemainingPersistentChargeTime());
    }

    default void readPersistentChargeSaveData(CompoundTag pTag) {
        this.setRemainingPersistentChargeTime(pTag.getInt(TAG_CHARGE_TIME));
    }

    default void updatePersistentCharge() {
        if (this.getRemainingPersistentChargeTime() > 0) {
            this.setRemainingPersistentChargeTime(this.getRemainingPersistentChargeTime() - 1);
        }
    }

    @Override
    default boolean isPowered() {
        return this.getRemainingPersistentChargeTime() > 0;
    }
}
