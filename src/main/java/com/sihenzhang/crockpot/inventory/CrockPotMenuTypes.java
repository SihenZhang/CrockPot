package com.sihenzhang.crockpot.inventory;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CrockPotMenuTypes {
    private CrockPotMenuTypes() {
    }

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, CrockPot.MOD_ID);

    public static final RegistryObject<MenuType<CrockPotMenu>> CROCK_POT_MENU_TYPE = MENU_TYPES.register("crock_pot", () -> IForgeMenuType.create((windowId, inv, data) -> new CrockPotMenu(windowId, inv, (CrockPotBlockEntity) inv.player.level.getBlockEntity(data.readBlockPos()))));
}
