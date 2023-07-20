package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.entity.CrockPotEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Goat.class)
public abstract class GoatMixin extends Animal {
    protected GoatMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
        if (ForgeEventFactory.canLivingConvert(this, CrockPotEntities.VOLT_GOAT.get(), (timer) -> {
        })) {
            var voltGoat = CrockPotEntities.VOLT_GOAT.get().create(pLevel);
            if (voltGoat != null) {
                voltGoat.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                voltGoat.setNoAi(this.isNoAi());
                voltGoat.setBaby(this.isBaby());
                if (this.hasCustomName()) {
                    voltGoat.setCustomName(this.getCustomName());
                    voltGoat.setCustomNameVisible(this.isCustomNameVisible());
                }
                voltGoat.setPersistenceRequired();
                ForgeEventFactory.onLivingConvert(this, voltGoat);
                pLevel.addFreshEntity(voltGoat);
                this.discard();
            } else {
                super.thunderHit(pLevel, pLightning);
            }
        } else {
            super.thunderHit(pLevel, pLightning);
        }
    }
}
