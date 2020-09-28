package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.item.CrockPotAlwaysEdibleItemFood;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class MilkBottle extends CrockPotAlwaysEdibleItemFood {
    public MilkBottle() {
        super(0, 0.0F, true);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.crockpot.milk_bottle"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
