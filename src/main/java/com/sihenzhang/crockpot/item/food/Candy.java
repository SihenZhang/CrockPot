package com.sihenzhang.crockpot.item.food;

import com.mojang.blaze3d.platform.InputConstants;
import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;

public class Candy extends CrockPotFood {
    public Candy() {
        super(CrockPotFood.builder().nutrition(3).saturationMod(0.2F).alwaysEat().duration(FoodUseDuration.FAST));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide) {
            float chance = level.random.nextFloat();
            if (chance < 0.25F) {
                livingEntity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            } else if (chance < 0.45F) {
                livingEntity.removeEffect(MobEffects.HUNGER);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 1));
            } else if (chance < 0.55F) {
                livingEntity.removeEffect(MobEffects.DIG_SLOWDOWN);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 20));
            } else if (chance < 0.6F) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20));
                livingEntity.hurt(CrockPotDamageSource.CANDY, 2.0F);
            }
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltipComponents.add(new TranslatableComponent("tooltip.crockpot.candy.real").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(new TranslatableComponent("tooltip.crockpot.candy"));
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
