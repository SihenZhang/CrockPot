package com.sihenzhang.crockpot.integration.patchouli;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.util.JsonUtils;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import org.apache.commons.lang3.EnumUtils;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Map;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ModIntegrationPatchouli {
    public static final String MOD_ID = "patchouli";

    private static final Map<String, Boolean> FLAGS = new Object2BooleanOpenHashMap<>(168);

    @SubscribeEvent
    public static void addConfigFlag(AddReloadListenerEvent event) {
        event.addListener(new ReloadListener<Void>() {
            @Override
            protected Void prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
                return null;
            }

            @Override
            protected void apply(Void objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
                if (ModList.get().isLoaded(ModIntegrationPatchouli.MOD_ID)) {
                    // Set category flag
                    EnumUtils.getEnumList(FoodCategory.class).forEach(category -> {
                        for (float value = 0.25F; value <= 4.0F; value += 0.25F) {
                            setConfigFlag(category.name().toLowerCase() + ":" + value, !CrockPot.FOOD_VALUES_MANAGER.getMatchedItems(category, value).isEmpty());
                        }
                    });
                    // Set world gen flag
                    setConfigFlag("unknown_seeds", CrockPotConfig.ENABLE_UNKNOWN_SEEDS.get());
                    setConfigFlag("world_gen", CrockPotConfig.ENABLE_WORLD_GENERATION.get());
                    setConfigFlag("asparagus_gen", CrockPotConfig.ASPARAGUS_GENERATION.get());
                    setConfigFlag("corn_gen", CrockPotConfig.CORN_GENERATION.get());
                    setConfigFlag("eggplant_gen", CrockPotConfig.EGGPLANT_GENERATION.get());
                    setConfigFlag("onion_gen", CrockPotConfig.ONION_GENERATION.get());
                    setConfigFlag("pepper_gen", CrockPotConfig.PEPPER_GENERATION.get());
                    setConfigFlag("tomato_gen", CrockPotConfig.TOMATO_GENERATION.get());
                }
            }
        });
    }

    private static void setConfigFlag(String key, boolean value) {
        String flag = CrockPot.MOD_ID + ":" + key;
        PatchouliAPI.get().setConfigFlag(flag, value);
        FLAGS.put(flag, value);
    }

    public static String getConfigFlags() {
        return JsonUtils.GSON.toJson(FLAGS);
    }

    public static void registerPacket() {
        NetworkManager.registerPacket(
                PacketCrockPotConfigFlag.class,
                PacketCrockPotConfigFlag::serialize,
                PacketCrockPotConfigFlag::deserialize,
                PacketCrockPotConfigFlag::handle,
                NetworkDirection.PLAY_TO_CLIENT
        );
    }
}
