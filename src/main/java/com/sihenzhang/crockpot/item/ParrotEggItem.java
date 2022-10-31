package com.sihenzhang.crockpot.item;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.entity.ThrownParrotEgg;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ParrotEggItem extends Item {
    public static final List<Pair<Integer, String>> VARIANT_NAMES = ImmutableList.of(
            Pair.of(0, "red_blue"),
            Pair.of(1, "blue"),
            Pair.of(2, "green"),
            Pair.of(3, "yellow_blue"),
            Pair.of(4, "grey")
    );

    private final int variant;

    public ParrotEggItem(int variant) {
        super(new Properties().stacksTo(16).tab(CrockPot.ITEM_GROUP));
        this.variant = variant;
    }

    public int getVariant() {
        return variant;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        var stackInHand = pPlayer.getItemInHand(pUsedHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!pLevel.isClientSide) {
            var thrownEgg = new ThrownParrotEgg(pLevel, pPlayer);
            thrownEgg.setItem(stackInHand);
            thrownEgg.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity(thrownEgg);
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            stackInHand.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stackInHand, pLevel.isClientSide());
    }
}
