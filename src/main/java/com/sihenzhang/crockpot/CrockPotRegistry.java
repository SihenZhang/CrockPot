package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.loot.CrockPotUnknownSeedsDropModifier;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CrockPotRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CrockPot.MOD_ID);
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, CrockPot.MOD_ID);

    public static final RegistryObject<MenuType<CrockPotMenu>> CROCK_POT_MENU_TYPE = CONTAINERS.register("crock_pot", () -> IForgeMenuType.create((windowId, inv, data) -> new CrockPotMenu(windowId, inv, (CrockPotBlockEntity) inv.player.level.getBlockEntity(data.readBlockPos()))));

    // Loot Modifiers
    static {
        LOOT_MODIFIER_SERIALIZERS.register("unknown_seeds_drop", CrockPotUnknownSeedsDropModifier.Serializer::new);
    }
}
