package com.sihenzhang.crockpot.block.food;

import com.sihenzhang.crockpot.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class PowCakeBlock extends CrockPotFoodBlock {
    public PowCakeBlock(Properties properties) {
        super(properties);
    }

    public static class AnimalEatPowCakeGoal extends RemoveBlockGoal {
        public AnimalEatPowCakeGoal(PathfinderMob removerMob, double speedModifier, int searchRange) {
            super(ModBlocks.POW_CAKE.get(), removerMob, speedModifier, searchRange);
        }

        @Override
        public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.WOOL_HIT, SoundSource.NEUTRAL, 0.5F, 0.9F + this.mob.getRandom().nextFloat() * 0.2F);
        }

        @Override
        public void playBreakSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
        }

        @Override
        public double acceptedDistance() {
            return 1.14;
        }

        @Override
        protected BlockPos getMoveToTarget() {
            return this.blockPos;
        }
    }
}
