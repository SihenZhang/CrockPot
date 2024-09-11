package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.ModSoundEvents;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class CrockPotSoundDefinitionsProvider extends SoundDefinitionsProvider {
    protected CrockPotSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, CrockPot.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(ModSoundEvents.CROCK_POT_CLOSE.get(), definition().subtitle(getSubtitleName(ModSoundEvents.CROCK_POT_CLOSE_NAME)).with(sound(RLUtils.mod("crock_pot_close"))));
        this.add(ModSoundEvents.CROCK_POT_OPEN.get(), definition().subtitle(getSubtitleName(ModSoundEvents.CROCK_POT_OEPN_NAME)).with(sound(RLUtils.mod("crock_pot_open"))));
        this.add(ModSoundEvents.CROCK_POT_FINISH.get(), definition().subtitle(getSubtitleName(ModSoundEvents.CROCK_POT_FINISH_NAME)).with(sound(RLUtils.mod("crock_pot_finish"))));
        this.add(ModSoundEvents.CROCK_POT_RATTLE.get(), definition().subtitle(getSubtitleName(ModSoundEvents.CROCK_POT_RATTLE_NAME)).with(
                sound(RLUtils.mod("crock_pot_rattle_1")),
                sound(RLUtils.mod("crock_pot_rattle_2")),
                sound(RLUtils.mod("crock_pot_rattle_3")),
                sound(RLUtils.mod("crock_pot_rattle_4")),
                sound(RLUtils.mod("crock_pot_rattle_5")),
                sound(RLUtils.mod("crock_pot_rattle_6")),
                sound(RLUtils.mod("crock_pot_rattle_7"))
        ));
    }

    public static String getSubtitleName(String name) {
        return "subtitles." + CrockPot.MOD_ID + "." + name;
    }
}
