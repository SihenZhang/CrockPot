package com.sihenzhang.crockpot.integration.patchouli;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.util.JsonUtils;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Map;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ModIntegrationPatchouli {
    public static final String MOD_ID = "patchouli";

    private static final Map<String, Boolean> FLAGS = new Object2BooleanOpenHashMap<>(8);

    @SubscribeEvent
    public static void addConfigFlag(FMLServerAboutToStartEvent event) {
        if (ModList.get().isLoaded(ModIntegrationPatchouli.MOD_ID)) {
            // Set world gen flag
            ModIntegrationPatchouli.setConfigFlag("unknown_seeds", CrockPotConfig.ENABLE_UNKNOWN_SEEDS.get());
            ModIntegrationPatchouli.setConfigFlag("world_gen", CrockPotConfig.ENABLE_WORLD_GENERATION.get());
            ModIntegrationPatchouli.setConfigFlag("asparagus_gen", CrockPotConfig.ASPARAGUS_GENERATION.get());
            ModIntegrationPatchouli.setConfigFlag("corn_gen", CrockPotConfig.CORN_GENERATION.get());
            ModIntegrationPatchouli.setConfigFlag("eggplant_gen", CrockPotConfig.EGGPLANT_GENERATION.get());
            ModIntegrationPatchouli.setConfigFlag("onion_gen", CrockPotConfig.ONION_GENERATION.get());
            ModIntegrationPatchouli.setConfigFlag("pepper_gen", CrockPotConfig.PEPPER_GENERATION.get());
            ModIntegrationPatchouli.setConfigFlag("tomato_gen", CrockPotConfig.TOMATO_GENERATION.get());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            if (ModList.get().isLoaded(ModIntegrationPatchouli.MOD_ID)) {
                NetworkManager.sendToPlayer((ServerPlayerEntity) event.getPlayer(), new PacketCrockPotConfigFlag(ModIntegrationPatchouli.getConfigFlags()));
            }
        }
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
