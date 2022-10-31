package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    public MilkmadeHatCuriosCapabilityProvider(ItemStack stack, @Nullable CompoundTag nbt) {
        this(stack, nbt, false);
    }

    public MilkmadeHatCuriosCapabilityProvider(ItemStack stack, @Nullable CompoundTag nbt, boolean isCreative) {
        this.curioOptional = LazyOptional.of(() -> new ICurio() {
            private Object model;

            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                LivingEntity entity = slotContext.entity();
                if (!entity.level.isClientSide) {
                    if (entity instanceof Player player) {
                        if (player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                            if (!isCreative) {
                                stack.hurtAndBreak(1, player, e -> CuriosApi.getCuriosHelper().onBrokenCurio(slotContext));
                            }
                            player.getFoodData().eat(1, 0.05F);
                            player.getCooldowns().addCooldown(stack.getItem(), isCreative ? 20 : 100);
                        }
                    }
                }
            }

            @Override
            public boolean canEquip(SlotContext slotContext) {
                return !slotContext.entity().getItemBySlot(EquipmentSlot.HEAD).is(CrockPotItemTags.MILKMADE_HATS) && !CuriosUtils.anyMatchInEquippedCurios(slotContext.entity(), CrockPotItemTags.MILKMADE_HATS);
            }

            @Override
            public boolean canEquipFromUse(SlotContext slotContext) {
                return true;
            }
        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CuriosCapability.ITEM.orEmpty(cap, this.curioOptional);
    }
}
