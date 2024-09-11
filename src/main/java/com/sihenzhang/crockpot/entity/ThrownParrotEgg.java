package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.item.ParrotEggItem;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityDimensions;
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
    private static final EntityDimensions ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);

    public ThrownParrotEgg(EntityType<? extends ThrownParrotEgg> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownParrotEgg(Level level, LivingEntity shooter) {
        super(ModEntities.PARROT_EGG.get(), shooter, level);
    }

    public ThrownParrotEgg(Level level, double x, double y, double z) {
        super(ModEntities.PARROT_EGG.get(), x, y, z, level);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EntityEvent.DEATH) {
            for (var i = 0; i < 8; i++) {
                this.level().addParticle(
                        new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (random.nextFloat() - 0.5) * 0.08,
                        (random.nextFloat() - 0.5) * 0.08,
                        (random.nextFloat() - 0.5) * 0.08
                );
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
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
                                if (parrot.fudgePositionAfterSizeChange(ZERO_SIZED_DIMENSIONS)) {
                                    this.level().addFreshEntity(parrot);
                                }
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
