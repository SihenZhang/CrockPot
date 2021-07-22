package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MilkmadeHatCuriosCapabilityProvider implements ICapabilityProvider {
    private final LazyOptional<ICurio> curioOptional;

    public MilkmadeHatCuriosCapabilityProvider(ItemStack stack, @Nullable CompoundNBT nbt) {
        ICurio curio = new ICurio() {
            @Override
            public void curioTick(String identifier, int index, LivingEntity livingEntity) {
                if (livingEntity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) livingEntity;
                    if (!player.getCommandSenderWorld().isClientSide && player.getFoodData().needsFood() && player.tickCount % 100 == 0) {
                        if (player.getItemBySlot(EquipmentSlotType.HEAD).getItem() == CrockPotRegistry.creativeMilkmadeHat) {
                            return;
                        }
                        stack.hurtAndBreak(1, player, e -> CuriosApi.getCuriosHelper().onBrokenCurio(identifier, index, e));
                        player.getFoodData().eat(1, 0.05F);
                    }
                }
            }

            @Override
            public boolean canEquip(String identifier, LivingEntity livingEntity) {
                if (livingEntity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) livingEntity;
                    return !CuriosUtils.anyMatchInEquippedCurios(player, MilkmadeHatItem.class);
                }
                return false;
            }

            @Override
            public boolean canEquipFromUse(SlotContext slotContext) {
                return true;
            }
        };
        this.curioOptional = LazyOptional.of(() -> curio);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CuriosCapability.ITEM.orEmpty(cap, this.curioOptional);
    }
}
