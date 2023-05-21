package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CrockPotCreativeModeTab {
    @SubscribeEvent
    public static void registerCreativeModeTab(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(RLUtils.createRL("tab"), builder ->
                builder.title(Component.translatable("itemGroup.crockpot"))
                        .icon(() -> CrockPotItems.BASIC_CROCK_POT.get().getDefaultInstance())
                        .displayItems((params, output) -> CrockPotItems.ITEMS.getEntries().forEach(regObj -> {
                            var item = regObj.get();
                            if (item != CrockPotItems.AVAJ.get()) {
                                output.accept(item);
                            }
                        }))
        );
    }
}
