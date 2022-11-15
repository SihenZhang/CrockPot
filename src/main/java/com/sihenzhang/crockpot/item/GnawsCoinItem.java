package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.integration.curios.GnawsCoinCuriosCapabilityProvider;
import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.List;

public class GnawsCoinItem extends CrockPotBaseItem {
    public GnawsCoinItem() {
        super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
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
    public int getEntityLifespan(ItemStack stack, Level level) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(I18nUtils.createComponent("tooltip", "gnaws_coin").withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player && player.tickCount % 19 == 0) {
            player.addEffect(new MobEffectInstance(CrockPotEffects.GNAWS_GIFT.get(), 20, 0, true, true));
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID)) {
            return new GnawsCoinCuriosCapabilityProvider(stack, nbt);
        }
        return super.initCapabilities(stack, nbt);
    }
}
