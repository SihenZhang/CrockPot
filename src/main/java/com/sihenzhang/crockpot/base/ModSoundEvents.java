package com.sihenzhang.crockpot.base;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSoundEvents {
    private ModSoundEvents() {
    }

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, CrockPot.MOD_ID);

    public static final String CROCK_POT_CLOSE_NAME = "block.crock_pot.close";
    public static final String CROCK_POT_OEPN_NAME = "block.crock_pot.open";
    public static final String CROCK_POT_FINISH_NAME = "block.crock_pot.finish";
    public static final String CROCK_POT_RATTLE_NAME = "block.crock_pot.rattle";

    public static final DeferredHolder<SoundEvent, SoundEvent> CROCK_POT_CLOSE = SOUND_EVENTS.register(CROCK_POT_CLOSE_NAME, () -> SoundEvent.createVariableRangeEvent(RLUtils.mod(CROCK_POT_CLOSE_NAME)));
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCK_POT_OPEN = SOUND_EVENTS.register(CROCK_POT_OEPN_NAME, () -> SoundEvent.createVariableRangeEvent(RLUtils.mod(CROCK_POT_OEPN_NAME)));
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCK_POT_FINISH = SOUND_EVENTS.register(CROCK_POT_FINISH_NAME, () -> SoundEvent.createVariableRangeEvent(RLUtils.mod(CROCK_POT_FINISH_NAME)));
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCK_POT_RATTLE = SOUND_EVENTS.register(CROCK_POT_RATTLE_NAME, () -> SoundEvent.createVariableRangeEvent(RLUtils.mod(CROCK_POT_RATTLE_NAME)));
}
