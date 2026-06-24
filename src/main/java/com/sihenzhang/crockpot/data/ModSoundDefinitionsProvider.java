package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.core.ModSoundEvents;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {
    protected ModSoundDefinitionsProvider(PackOutput output) {
        super(output, CrockPot.MOD_ID);
    }

    @Override
    public void registerSounds() {
        this.add(ModSoundEvents.CROCK_POT_CLOSE, definition().subtitle(getSubtitleName("block.crock_pot.close")).with(sound(IdUtil.mod("crock_pot_close"))));
        this.add(ModSoundEvents.CROCK_POT_OPEN, definition().subtitle(getSubtitleName("block.crock_pot.open")).with(sound(IdUtil.mod("crock_pot_open"))));
        this.add(ModSoundEvents.CROCK_POT_FINISH, definition().subtitle(getSubtitleName("block.crock_pot.finish")).with(sound(IdUtil.mod("crock_pot_finish"))));
        this.add(ModSoundEvents.CROCK_POT_RATTLE, definition().subtitle(getSubtitleName("block.crock_pot.rattle")).with(
                sound(IdUtil.mod("crock_pot_rattle_1")),
                sound(IdUtil.mod("crock_pot_rattle_2")),
                sound(IdUtil.mod("crock_pot_rattle_3")),
                sound(IdUtil.mod("crock_pot_rattle_4")),
                sound(IdUtil.mod("crock_pot_rattle_5")),
                sound(IdUtil.mod("crock_pot_rattle_6")),
                sound(IdUtil.mod("crock_pot_rattle_7"))
        ));
    }

    public static String getSubtitleName(String name) {
        return "subtitles." + CrockPot.MOD_ID + "." + name;
    }
}
