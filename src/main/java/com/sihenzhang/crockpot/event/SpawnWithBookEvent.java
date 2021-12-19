package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.integration.patchouli.ModIntegrationPatchouli;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.patchouli.api.PatchouliAPI;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class SpawnWithBookEvent {
    private static final String SPAWN_WITH_BOOK_TAG_NAME = "crock_pot_book";

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (ModList.get().isLoaded(ModIntegrationPatchouli.MOD_ID) && CrockPotConfig.SPAWN_WITH_BOOK.get()) {
            CompoundTag playerData = event.getPlayer().getPersistentData();
            CompoundTag data = event.getPlayer().getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            if (!data.getBoolean(SPAWN_WITH_BOOK_TAG_NAME)) {
                ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), PatchouliAPI.get().getBookStack(new ResourceLocation(CrockPot.MOD_ID, "book")));
                data.putBoolean(SPAWN_WITH_BOOK_TAG_NAME, true);
                playerData.put(Player.PERSISTED_NBT_TAG, data);
            }
        }
    }
}
