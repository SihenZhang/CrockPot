package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class MilkmadeHatCuriosCapabilityProvider implements ICurio {
    private final ItemStack stack;
    private final boolean isCreative;

    public MilkmadeHatCuriosCapabilityProvider(ItemStack stack, boolean isCreative) {
        this.stack = stack;
        this.isCreative = isCreative;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void curioTick(SlotContext slotContext) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide) {
            if (entity instanceof Player player) {
                if (player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                    if (!isCreative) {
                        stack.hurtAndBreak(1, (ServerLevel) player.level(), player, item -> CuriosApi.getCuriosHelper().onBrokenCurio(slotContext));
                    }
                    player.getFoodData().eat(1, 0.05F);
                    player.getCooldowns().addCooldown(stack.getItem(), isCreative ? 20 : 100);
                }
            }
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext) {
        return !slotContext.entity().getItemBySlot(EquipmentSlot.HEAD).is(CrockPotItemTags.MILKMADE_HATS) && !CuriosUtils.anyMatchInEquippedCurios(slotContext.entity(), CrockPotItemTags.MILKMADE_HATS);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext) {
        return true;
    }
}
