package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.item.CrockPotAlwaysEdibleItemFood;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class WetGoop extends CrockPotAlwaysEdibleItemFood {
    public WetGoop() {
        super(0, 0.0F, () -> new EffectInstance(Effects.NAUSEA, 10 * 20), FoodUseDuration.SUPER_SLOW);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.crockopt.wet_goop").mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
