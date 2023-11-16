package com.sihenzhang.crockpot.block.food;

import com.sihenzhang.crockpot.block.CrockPotBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SoundType;

public class PowCakeBlock extends CrockPotFoodBlock {
    public PowCakeBlock() {
        super(Properties.of().sound(SoundType.WOOL));
    }

    public static class AnimalEatPowCakeGoal extends RemoveBlockGoal {
        public AnimalEatPowCakeGoal(PathfinderMob pRemoverMob, double pSpeedModifier, int pSearchRange) {
            super(CrockPotBlocks.POW_CAKE.get(), pRemoverMob, pSpeedModifier, pSearchRange);
        }

        @Override
        public void playDestroyProgressSound(LevelAccessor pLevel, BlockPos pPos) {
            pLevel.playSound(null, pPos, SoundEvents.WOOL_HIT, SoundSource.NEUTRAL, 0.5F, 0.9F + mob.getRandom().nextFloat() * 0.2F);
        }

        @Override
        public void playBreakSound(Level pLevel, BlockPos pPos) {
            pLevel.playSound(null, pPos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + pLevel.random.nextFloat() * 0.2F);
        }

        @Override
        protected BlockPos getMoveToTarget() {
            return blockPos;
        }
    }
}
