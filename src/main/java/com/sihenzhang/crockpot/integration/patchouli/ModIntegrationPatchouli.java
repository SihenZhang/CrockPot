package com.sihenzhang.crockpot.integration.patchouli;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.base.FoodCategory;
import org.apache.commons.lang3.EnumUtils;
import vazkii.patchouli.api.PatchouliAPI;

public class ModIntegrationPatchouli {
    public static final String MOD_ID = "patchouli";

    public static void addConfigFlag() {
        EnumUtils.getEnumList(FoodCategory.class).forEach(category -> {
            for (float value = 0.25F; value <= 4.0F; value += 0.25F) {
                setConfigFlag(category.name().toLowerCase() + ":" + value, !CrockPot.FOOD_CATEGORY_MANAGER.getMatchingItems(category, value).isEmpty());
            }
        });
        setConfigFlag("unknown_seeds", CrockPotConfig.ENABLE_UNKNOWN_SEEDS.get());
        setConfigFlag("world_gen", CrockPotConfig.ENABLE_WORLD_GENERATION.get());
        setConfigFlag("asparagus_gen", CrockPotConfig.ASPARAGUS_GENERATION.get());
        setConfigFlag("corn_gen", CrockPotConfig.CORN_GENERATION.get());
        setConfigFlag("eggplant_gen", CrockPotConfig.EGGPLANT_GENERATION.get());
        setConfigFlag("onion_gen", CrockPotConfig.ONION_GENERATION.get());
        setConfigFlag("pepper_gen", CrockPotConfig.PEPPER_GENERATION.get());
        setConfigFlag("tomato_gen", CrockPotConfig.TOMATO_GENERATION.get());
    }

    private static void setConfigFlag(String key, boolean value) {
        PatchouliAPI.instance.setConfigFlag(CrockPot.MOD_ID + ":" + key, value);
    }
}
