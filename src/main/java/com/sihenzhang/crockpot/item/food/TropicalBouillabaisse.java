package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotBaseItemFood;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TropicalBouillabaisse extends Item {
    public TropicalBouillabaisse() {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(7).saturation(0.6F)
                .effect(() -> new EffectInstance(Effects.SPEED, 5 * 60 * 20), 1.0F)
                .effect(() -> new EffectInstance(Effects.DOLPHINS_GRACE, 5 * 60 * 20), 1.0F)
                .effect(() -> new EffectInstance(Effects.WATER_BREATHING, 5 * 60 * 20), 1.0F)
                .setAlwaysEdible().build()));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return CrockPotBaseItemFood.FAST_USE_DURATION;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            if (entityLiving instanceof PlayerEntity) {
                ((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, 20);
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
