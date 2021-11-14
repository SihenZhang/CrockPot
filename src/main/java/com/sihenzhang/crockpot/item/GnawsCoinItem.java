package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.integration.curios.GnawsCoinCuriosCapabilityProvider;
import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.List;

public class GnawsCoinItem extends Item {
    public GnawsCoinItem() {
        super(new Properties().tab(CrockPot.ITEM_GROUP).stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.crockpot.gnaws_coin").withStyle(TextFormatting.AQUA));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && entity instanceof PlayerEntity && entity.tickCount % 19 == 0) {
            ((PlayerEntity) entity).addEffect(new EffectInstance(CrockPotRegistry.gnawsGift, 20, 0, true, true));
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID)) {
            return new GnawsCoinCuriosCapabilityProvider(stack, nbt);
        }
        return super.initCapabilities(stack, nbt);
    }
}
