package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.item.ParrotEggItem;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public class ThrownParrotEgg extends ThrowableItemProjectile {
    public ThrownParrotEgg(EntityType<? extends ThrownParrotEgg> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownParrotEgg(Level pLevel, double pX, double pY, double pZ) {
        super(CrockPotEntities.PARROT_EGG.get(), pX, pY, pZ, pLevel);
    }

    public ThrownParrotEgg(Level pLevel, LivingEntity pShooter) {
        super(CrockPotEntities.PARROT_EGG.get(), pShooter, pLevel);
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == EntityEvent.DEATH) {
            for (var i = 0; i < 8; i++) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), (random.nextFloat() - 0.5D) * 0.08D, (random.nextFloat() - 0.5D) * 0.08D, (random.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        pResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            if (random.nextInt(16) == 0) {
                Optional.of(this.getItem().getItem())
                        .filter(ParrotEggItem.class::isInstance)
                        .map(ParrotEggItem.class::cast)
                        .map(ParrotEggItem::getVariant)
                        .ifPresent(variant -> {
                            var parrot = EntityType.PARROT.create(this.level());
                            if (parrot != null) {
                                parrot.setVariant(variant);
                                parrot.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                                this.level().addFreshEntity(parrot);
                            }
                        });
            }
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return CrockPotItems.PARROT_EGGS.get(Parrot.Variant.RED_BLUE).get();
    }
}
