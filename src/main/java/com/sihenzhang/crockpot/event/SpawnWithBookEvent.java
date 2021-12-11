package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class SpawnWithBookEvent {
    private static final String SPAWN_WITH_BOOK_TAG_NAME = "crock_pot_book";

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
//        if (ModList.get().isLoaded(ModIntegrationPatchouli.MOD_ID) && CrockPotConfig.SPAWN_WITH_BOOK.get()) {
//            CompoundTag playerData = event.getPlayer().getPersistentData();
//            CompoundTag data = event.getPlayer().getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
//            if (!data.getBoolean(SPAWN_WITH_BOOK_TAG_NAME)) {
//                ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), PatchouliAPI.get().getBookStack(new ResourceLocation(CrockPot.MOD_ID, "book")));
//                data.putBoolean(SPAWN_WITH_BOOK_TAG_NAME, true);
//                playerData.put(Player.PERSISTED_NBT_TAG, data);
//            }
//        }
    }
}
