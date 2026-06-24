package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.item.ParrotEggItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public class ThrownParrotEgg extends ThrowableItemProjectile {
    public ThrownParrotEgg(EntityType<? extends ThrownParrotEgg> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownParrotEgg(Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(ModEntities.PARROT_EGG.get(), pShooter, pLevel, pStack);
    }

    public ThrownParrotEgg(Level pLevel, double pX, double pY, double pZ, ItemStack pStack) {
        super(ModEntities.PARROT_EGG.get(), pX, pY, pZ, pLevel, pStack);
    }

    public ThrownParrotEgg(Level pLevel, double pX, double pY, double pZ) {
        this(pLevel, pX, pY, pZ, new ItemStack(ModItems.PARROT_EGGS.get(Parrot.Variant.DEFAULT).get()));
    }

    public ThrownParrotEgg(Level pLevel, LivingEntity pShooter) {
        this(pLevel, pShooter, new ItemStack(ModItems.PARROT_EGGS.get(Parrot.Variant.DEFAULT).get()));
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == EntityEvent.DEATH) {
            var item = this.getItem();
            if (item.isEmpty()) {
                return;
            }
            var particle = new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(item));
            for (var i = 0; i < 8; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), (random.nextFloat() - 0.5D) * 0.08D, (random.nextFloat() - 0.5D) * 0.08D, (random.nextFloat() - 0.5D) * 0.08D);
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
        if (!this.level().isClientSide()) {
            if (random.nextInt(16) == 0) {
                Optional.of(this.getItem().getItem())
                        .filter(ParrotEggItem.class::isInstance)
                        .map(ParrotEggItem.class::cast)
                        .map(ParrotEggItem::getVariant)
                        .ifPresent(variant -> {
                            var parrot = EntityType.PARROT.create(this.level(), EntitySpawnReason.TRIGGERED);
                            if (parrot != null) {
                                parrot.setComponent(DataComponents.PARROT_VARIANT, variant);
                                parrot.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
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
        return ModItems.PARROT_EGGS.get(Parrot.Variant.RED_BLUE).get();
    }
}
