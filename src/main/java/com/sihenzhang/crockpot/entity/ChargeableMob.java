package com.sihenzhang.crockpot.entity;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface ChargeableMob {
    String TAG_CHARGE_TIME = "ChargeTime";

    int getRemainingPersistentChargeTime();

    void setRemainingPersistentChargeTime(int pRemainingPersistentChargeTime);

    void startPersistentChargeTimer();

    default void addPersistentChargeSaveData(ValueOutput output) {
        output.putInt(TAG_CHARGE_TIME, this.getRemainingPersistentChargeTime());
    }

    default void readPersistentChargeSaveData(ValueInput input) {
        this.setRemainingPersistentChargeTime(input.getIntOr(TAG_CHARGE_TIME, 0));
    }

    default void updatePersistentCharge() {
        if (this.getRemainingPersistentChargeTime() > 0) {
            this.setRemainingPersistentChargeTime(this.getRemainingPersistentChargeTime() - 1);
        }
    }

    default boolean isPowered() {
        return this.getRemainingPersistentChargeTime() > 0;
    }
}
