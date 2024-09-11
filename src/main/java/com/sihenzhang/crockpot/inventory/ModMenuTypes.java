package com.sihenzhang.crockpot.inventory;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModMenuTypes {
    private ModMenuTypes() {
    }

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, CrockPot.MOD_ID);

    public static final Supplier<MenuType<CrockPotMenu>> CROCK_POT_MENU_TYPE = MENU_TYPES.register("crock_pot", () -> IMenuTypeExtension.create((windowId, inv, data) -> new CrockPotMenu(windowId, inv, (CrockPotBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()))));
}
