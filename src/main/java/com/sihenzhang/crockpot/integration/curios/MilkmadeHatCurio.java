package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import com.sihenzhang.crockpot.tag.ModItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public record MilkmadeHatCurio(ItemStack stack) implements ICurio {
    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void curioTick(SlotContext slotContext) {
        if (slotContext.entity() instanceof Player player && player.level() instanceof ServerLevel level) {
            MilkmadeHatItem.feedPlayer(stack, level, player, item -> CuriosApi.broadcastCurioBreakEvent(slotContext));
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext) {
        var entity = slotContext.entity();
        return !entity.getItemBySlot(EquipmentSlot.HEAD).is(ModItemTags.MILKMADE_HATS)
                && !CuriosUtils.anyMatchInEquippedCurios(entity, ModItemTags.MILKMADE_HATS);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext) {
        return true;
    }
}
