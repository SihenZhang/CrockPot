package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CrockPotCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CrockPot.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_MODE_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
            .icon(() -> CrockPotItems.BASIC_CROCK_POT.get().getDefaultInstance())
            .displayItems((params, output) -> CrockPotItems.ITEMS.getEntries().forEach(regObj -> {
                            var item = regObj.get();
                            if (item != CrockPotItems.AVAJ.get()) {
                                output.accept(item);
                            }
                        }))
            .build()
    );
}
