package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.entity.ThrownParrotEgg;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ParrotEggItem extends CrockPotBaseItem {
    private final Parrot.Variant variant;

    public ParrotEggItem(Parrot.Variant variant) {
        super(new Properties().stacksTo(16));
        this.variant = variant;
        DispenserBlock.registerBehavior(this, new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(Level pLevel, Position pPosition, ItemStack pStack) {
                return Util.make(new ThrownParrotEgg(pLevel, pPosition.x(), pPosition.y(), pPosition.z()), entity -> entity.setItem(pStack));
            }
        });
    }

    public Parrot.Variant getVariant() {
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
