package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.tag.ModItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public record MilkmadeHatCurios(ItemStack stack, boolean isCreative) implements ICurio {
    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void curioTick(SlotContext slotContext) {
        var entity = slotContext.entity();
        if (entity.level() instanceof ServerLevel serverLevel && entity instanceof Player player) {
            if (player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                if (!isCreative) {
                    stack.hurtAndBreak(1, serverLevel, player, e -> CuriosApi.broadcastCurioBreakEvent(slotContext));
                }
                player.getFoodData().eat(1, 0.05F);
                player.getCooldowns().addCooldown(stack.getItem(), isCreative ? 20 : 100);
            }
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext) {
        return !slotContext.entity().getItemBySlot(EquipmentSlot.HEAD).is(ModItemTags.MILKMADE_HATS) && !CuriosUtils.anyMatchInEquippedCurios(slotContext.entity(), ModItemTags.MILKMADE_HATS);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext) {
        return true;
    }
}
