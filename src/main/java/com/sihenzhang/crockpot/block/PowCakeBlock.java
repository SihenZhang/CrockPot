package com.sihenzhang.crockpot.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.EnumSet;

public class PowCakeBlock extends Block {
    public PowCakeBlock() {
        super(Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL));
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).getMaterial().isSolid();
    }

    public static class EatPowCakeGoal extends MoveToBlockGoal {
        private final int horizontalSearchRange;

        public EatPowCakeGoal(CreatureEntity mob, double speedModifier, int horizontalSearchRange, int verticalSearchRange) {
            super(mob, speedModifier, horizontalSearchRange, verticalSearchRange);
            this.horizontalSearchRange = horizontalSearchRange;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        protected boolean isValidTarget(IWorldReader level, BlockPos pos) {
//            if (level.isAreaLoaded(pos, this.horizontalSearchRange)) {
//                return level.getBlockState(pos).is(CrockPotRegistry.powCakeBlock);
//            }
            return false;
        }
    }
}
