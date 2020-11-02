package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import com.sihenzhang.crockpot.item.CrockPotAlwaysEdibleItemFood;
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
public class Candy extends CrockPotAlwaysEdibleItemFood {
    public Candy() {
        super(5, 0.2F, () -> new EffectInstance(Effects.HUNGER, 15 * 20), FoodUseDuration.FAST);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            int val = worldIn.rand.nextInt(3);
            if (val != 0) {
                entityLiving.attackEntityFrom(CrockPotDamageSource.CANDY, val);
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            return new TranslationTextComponent("item.crockpot.candy.real");
        } else {
            return super.getDisplayName(stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new TranslationTextComponent("tooltip.crockpot.candy.real").applyTextStyles(TextFormatting.ITALIC, TextFormatting.DARK_GRAY));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.crockpot.candy"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
