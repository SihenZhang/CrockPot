package com.sihenzhang.crockpot.block.entity;

import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BirdcageBlockEntity extends BlockEntity {
    public BirdcageBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(CrockPotRegistry.BIRDCAGE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }


}
