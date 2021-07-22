package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.curios.CreativeMilkmadeHatCuriosCapabilityProvider;
import com.sihenzhang.crockpot.integration.curios.CuriosUtils;
import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class CreativeMilkmadeHatItem extends MilkmadeHatItem {
    public CreativeMilkmadeHatItem() {
        super(new Properties().tab(CrockPot.ITEM_GROUP).stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (!world.isClientSide && player.getFoodData().needsFood() && player.tickCount % 20 == 0) {
            if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID) && CuriosUtils.anyMatchInEquippedCurios(player, stack)) {
                return;
            }
            player.getFoodData().eat(1, 0.05F);
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID)) {
            return new CreativeMilkmadeHatCuriosCapabilityProvider(stack, nbt);
        }
        return super.initCapabilities(stack, nbt);
    }
}
