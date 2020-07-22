package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
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
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class WetGoop extends Item {
    private static final Supplier<EffectInstance> nauseaEffect = () -> new EffectInstance(Effects.NAUSEA, 5 * 20);
    private static final Supplier<EffectInstance> poisonEffect = () -> new EffectInstance(Effects.POISON, 20);

    public WetGoop() {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(0).saturation(0F).effect(nauseaEffect, 1F).effect(poisonEffect, 1F).build()));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 48;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.crockopt.wet_goop").applyTextStyles(TextFormatting.ITALIC, TextFormatting.GRAY));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
