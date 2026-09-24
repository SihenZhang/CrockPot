package com.sihenzhang.crockpot.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class CreativeMilkmadeHatItem extends MilkmadeHatItem {
    public CreativeMilkmadeHatItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide && player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(this)) {
            player.getFoodData().eat(1, 0.05F);
            player.getCooldowns().addCooldown(this, 20);
        }
    }

}
