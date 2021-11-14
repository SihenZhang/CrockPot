package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Candy extends CrockPotFood {
    public Candy() {
        super(CrockPotFood.builder().hunger(3).saturation(0.2F).setAlwaysEdible().duration(FoodUseDuration.FAST));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            float chance = worldIn.random.nextFloat();
            if (chance < 0.25F) {
                entityLiving.removeEffect(Effects.MOVEMENT_SLOWDOWN);
            } else if (chance < 0.45F) {
                entityLiving.removeEffect(Effects.HUNGER);
                entityLiving.addEffect(new EffectInstance(Effects.SATURATION, 1, 1));
            } else if (chance < 0.55F) {
                entityLiving.removeEffect(Effects.DIG_SLOWDOWN);
                entityLiving.addEffect(new EffectInstance(Effects.DIG_SPEED, 20 * 20));
            } else if (chance < 0.6F) {
                entityLiving.addEffect(new EffectInstance(Effects.WEAKNESS, 10 * 20));
                entityLiving.hurt(CrockPotDamageSource.CANDY, 2.0F);
            }
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new TranslationTextComponent("tooltip.crockpot.candy.real").withStyle(TextFormatting.ITALIC, TextFormatting.DARK_GRAY));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.crockpot.candy"));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
