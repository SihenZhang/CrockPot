package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import com.sihenzhang.crockpot.client.KeyHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Candy extends CrockPotAlwaysEdibleItemFood {
    public Candy() {
        super(3, 0.1F);
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
        if (KeyHandler.getIsKeyPressed(Minecraft.getInstance().gameSettings.keyBindSneak)) {
            return new TranslationTextComponent("item.crockpot.candy.real");
        } else {
            return super.getDisplayName(stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (KeyHandler.getIsKeyPressed(Minecraft.getInstance().gameSettings.keyBindSneak)) {
            tooltip.add(new TranslationTextComponent("tooltip.crockpot.candy.real").applyTextStyles(TextFormatting.ITALIC, TextFormatting.DARK_GRAY));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.crockpot.candy"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
