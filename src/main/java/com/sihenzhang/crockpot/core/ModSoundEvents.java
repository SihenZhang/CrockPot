package com.sihenzhang.crockpot.core;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSoundEvents {
    private ModSoundEvents() {
    }

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, CrockPot.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> CROCK_POT_CLOSE = register("block.crock_pot.close");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCK_POT_OPEN = register("block.crock_pot.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCK_POT_FINISH = register("block.crock_pot.finish");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCK_POT_RATTLE = register("block.crock_pot.rattle");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(IdUtil.mod(name)));
    }
}
