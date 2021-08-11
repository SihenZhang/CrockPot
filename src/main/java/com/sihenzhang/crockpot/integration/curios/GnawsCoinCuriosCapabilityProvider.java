package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GnawsCoinCuriosCapabilityProvider implements ICapabilityProvider {
    private final LazyOptional<ICurio> curioOptional;

    public GnawsCoinCuriosCapabilityProvider(ItemStack stack, @Nullable CompoundNBT nbt) {
        this.curioOptional = LazyOptional.of(() -> new ICurio()  {
            @Override
            public void curioTick(String identifier, int index, LivingEntity livingEntity) {
                if (!livingEntity.level.isClientSide && livingEntity instanceof PlayerEntity && livingEntity.tickCount % 19 == 0) {
                    livingEntity.addEffect(new EffectInstance(CrockPotRegistry.gnawsGift, 20, 0, true, true));
                }
            }

            @Override
            public boolean canEquipFromUse(SlotContext slotContext) {
                return true;
            }

            @Nonnull
            @Override
            public DropRule getDropRule(LivingEntity livingEntity) {
                return DropRule.ALWAYS_KEEP;
            }
        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CuriosCapability.ITEM.orEmpty(cap, this.curioOptional);
    }
}
