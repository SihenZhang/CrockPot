package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class VoltGoat extends Animal implements ChargeableMob, NeutralMob {
    private static final EntityDataAccessor<Integer> DATA_REMAINING_CHARGE_TIME = SynchedEntityData.defineId(VoltGoat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(VoltGoat.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final int PERSISTENT_CHARGE_TIME = 48000;
    @Nullable
    private UUID persistentAngerTarget;
    @Nullable
    private UUID lastLightningBolt;

    public VoltGoat(EntityType<? extends VoltGoat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new VoltGoatChargeGoal());
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new VoltGoatPanicGoal(1.5D));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.4D, 1.5D));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_REMAINING_CHARGE_TIME, 0);
        entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    public int getRemainingPersistentChargeTime() {
        return entityData.get(DATA_REMAINING_CHARGE_TIME);
    }

    @Override
    public void setRemainingPersistentChargeTime(int pRemainingPersistentChargeTime) {
        entityData.set(DATA_REMAINING_CHARGE_TIME, pRemainingPersistentChargeTime);
    }

    @Override
    public void startPersistentChargeTimer() {
        this.setRemainingPersistentChargeTime(PERSISTENT_CHARGE_TIME);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
        entityData.set(DATA_REMAINING_ANGER_TIME, pRemainingPersistentAngerTime);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pPersistentAngerTarget) {
        persistentAngerTarget = pPersistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(random));
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return super.calculateFallDamage(pFallDistance, pDamageMultiplier) - Goat.GOAT_FALL_DAMAGE_REDUCTION;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GOAT_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.GOAT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GOAT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        this.playSound(SoundEvents.GOAT_STEP, 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.addPersistentChargeSaveData(pCompound);
        this.addPersistentAngerSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readPersistentChargeSaveData(pCompound);
        this.readPersistentAngerSaveData(this.level(), pCompound);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return CrockPotEntities.VOLT_GOAT.get().create(pLevel);
    }

    @Override
    protected void customServerAiStep() {
        this.updatePersistentCharge();
        this.updatePersistentAnger((ServerLevel) this.level(), true);
        super.customServerAiStep();
    }

    @Override
    public int getMaxHeadYRot() {
        return 15;
    }

    @Override
    public void setYHeadRot(float pRotation) {
        var maxHeadYRot = this.getMaxHeadYRot();
        var f = Mth.degreesDifference(this.yBodyRot, pRotation);
        var f1 = Mth.clamp(f, (float) (-maxHeadYRot), (float) maxHeadYRot);
        super.setYHeadRot(this.yBodyRot + f1);
    }

    @Override
    public SoundEvent getEatingSound(ItemStack pStack) {
        return SoundEvents.GOAT_EAT;
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        var stackInHand = pPlayer.getItemInHand(pHand);
        if (stackInHand.is(Items.BUCKET) && !this.isBaby()) {
            pPlayer.playSound(SoundEvents.GOAT_MILK, 1.0F, 1.0F);
            var milkBucket = ItemUtils.createFilledResult(stackInHand, pPlayer, Items.MILK_BUCKET.getDefaultInstance());
            pPlayer.setItemInHand(pHand, milkBucket);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            var interactionResult = super.mobInteract(pPlayer, pHand);
            if (interactionResult.consumesAction() && this.isFood(stackInHand)) {
                this.level().playSound(null, this, this.getEatingSound(stackInHand), SoundSource.NEUTRAL, 1.0F, Mth.randomBetween(this.level().random, 0.8F, 1.2F));
            }
            return interactionResult;
        }
    }

    public void setLastLightningBolt(UUID lastLightningBolt) {
        this.lastLightningBolt = lastLightningBolt;
    }

    @Override
    public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
        var uuid = pLightning.getUUID();
        if (!uuid.equals(lastLightningBolt)) {
            this.startPersistentChargeTimer();
            this.setLastLightningBolt(uuid);
        }
    }

    public static boolean checkVoltGoatSpawnRules(EntityType<? extends Animal> pVoltGoat, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getBlockState(pPos.below()).is(CrockPotBlockTags.VOLT_GOATS_SPAWNABLE_ON) && isBrightEnoughToSpawn(pLevel, pPos);
    }

    class VoltGoatPanicGoal extends PanicGoal {
        public VoltGoatPanicGoal(double pSpeedModifier) {
            super(VoltGoat.this, pSpeedModifier);
        }

        protected boolean shouldPanic() {
            return mob.isFreezing() || mob.isOnFire();
        }
    }

    class VoltGoatChargeGoal extends Goal {
        @Override
        public boolean canUse() {
            return VoltGoat.this.isPowered();
        }

        @Override
        public void start() {
            VoltGoat.this.addEffect(new MobEffectInstance(CrockPotEffects.CHARGE.get(), -1, 0, false, false));
        }

        @Override
        public void stop() {
            VoltGoat.this.removeEffect(CrockPotEffects.CHARGE.get());
        }
    }
}
