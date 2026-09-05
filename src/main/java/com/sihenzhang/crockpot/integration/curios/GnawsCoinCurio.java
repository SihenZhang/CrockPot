package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.effect.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.common.DropRule;
import top.theillusivec4.curios.api.type.capability.ICurio;

public record GnawsCoinCurio(ItemStack stack) implements ICurio {
    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void curioTick(SlotContext slotContext) {
        var entity = slotContext.entity();
        if (entity.level() instanceof ServerLevel && entity instanceof Player && entity.tickCount % 19 == 0) {
            entity.addEffect(new MobEffectInstance(ModEffects.GNAWS_GIFT, 20, 0, true, false));
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext) {
        return true;
    }

    @Override
    public DropRule getDropRule(SlotContext slotContext, DamageSource source, boolean recentlyHit) {
        return DropRule.ALWAYS_KEEP;
    }
}
