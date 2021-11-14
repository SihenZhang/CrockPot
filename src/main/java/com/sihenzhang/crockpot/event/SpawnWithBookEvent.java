package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.patchouli.api.PatchouliAPI;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class SpawnWithBookEvent {
    private static final String SPAWN_WITH_BOOK_TAG_NAME = "crock_pot_book";

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (CrockPotConfig.SPAWN_WITH_BOOK.get()) {
            CompoundNBT playerData = event.getPlayer().getPersistentData();
            CompoundNBT data = event.getPlayer().getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            if (!data.getBoolean(SPAWN_WITH_BOOK_TAG_NAME)) {
                ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), PatchouliAPI.get().getBookStack(new ResourceLocation(CrockPot.MOD_ID, "book")));
                data.putBoolean(SPAWN_WITH_BOOK_TAG_NAME, true);
                playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
            }
        }
    }
}
