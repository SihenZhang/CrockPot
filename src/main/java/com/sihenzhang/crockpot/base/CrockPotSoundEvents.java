package com.sihenzhang.crockpot.base;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrockPotSoundEvents {
    private CrockPotSoundEvents() {
    }

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CrockPot.MOD_ID);

    public static final String CROCK_POT_CLOSE_NAME = "block.crock_pot.close";
    public static final String CROCK_POT_OEPN_NAME = "block.crock_pot.open";
    public static final String CROCK_POT_FINISH_NAME = "block.crock_pot.finish";
    public static final String CROCK_POT_RATTLE_NAME = "block.crock_pot.rattle";

    public static final RegistryObject<SoundEvent> CROCK_POT_CLOSE = SOUND_EVENTS.register(CROCK_POT_CLOSE_NAME, () -> SoundEvent.createVariableRangeEvent(RLUtils.createRL(CROCK_POT_CLOSE_NAME)));
    public static final RegistryObject<SoundEvent> CROCK_POT_OPEN = SOUND_EVENTS.register(CROCK_POT_OEPN_NAME, () -> SoundEvent.createVariableRangeEvent(RLUtils.createRL(CROCK_POT_OEPN_NAME)));
    public static final RegistryObject<SoundEvent> CROCK_POT_FINISH = SOUND_EVENTS.register(CROCK_POT_FINISH_NAME, () -> SoundEvent.createVariableRangeEvent(RLUtils.createRL(CROCK_POT_FINISH_NAME)));
    public static final RegistryObject<SoundEvent> CROCK_POT_RATTLE = SOUND_EVENTS.register(CROCK_POT_RATTLE_NAME, () -> SoundEvent.createVariableRangeEvent(RLUtils.createRL(CROCK_POT_RATTLE_NAME)));
}
