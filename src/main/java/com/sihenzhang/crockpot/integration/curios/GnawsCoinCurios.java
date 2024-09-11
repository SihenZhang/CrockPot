package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.effect.ModEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;

public record GnawsCoinCurios(ItemStack stack) implements ICurio {
    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void curioTick(SlotContext slotContext) {
        var entity = slotContext.entity();
        if (!entity.level().isClientSide && entity instanceof Player && entity.tickCount % 19 == 0) {
            entity.addEffect(new MobEffectInstance(ModEffects.GNAWS_GIFT, 20, 0, true, true));
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext) {
        return true;
    }

    @Nonnull
    @Override
    public DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit) {
        return DropRule.ALWAYS_KEEP;
    }
}
