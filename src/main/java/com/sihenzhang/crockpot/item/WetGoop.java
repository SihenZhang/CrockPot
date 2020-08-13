package com.sihenzhang.crockpot.item;

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
public class WetGoop extends CrockPotBaseItemFood {
    public WetGoop() {
        super(0, 0F, () -> new EffectInstance(Effects.NAUSEA, 10 * 20), () -> new EffectInstance(Effects.POISON,  2 * 20), 48);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.crockopt.wet_goop").applyTextStyles(TextFormatting.ITALIC, TextFormatting.GRAY));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
