package com.sihenzhang.crockpot.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class PowCakeBlock extends Block {
    public PowCakeBlock() {
        super(Properties.of().strength(0.5F).sound(SoundType.WOOL));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    public static class EatPowCakeGoal extends MoveToBlockGoal {
        private final int horizontalSearchRange;

        public EatPowCakeGoal(PathfinderMob mob, double speedModifier, int horizontalSearchRange, int verticalSearchRange) {
            super(mob, speedModifier, horizontalSearchRange, verticalSearchRange);
            this.horizontalSearchRange = horizontalSearchRange;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
//            if (level.isAreaLoaded(pos, this.horizontalSearchRange)) {
//                return level.getBlockState(pos).is(CrockPotRegistry.powCakeBlock);
//            }
            return false;
        }
    }
}
