package com.sihenzhang.crockpot.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IItemProvider;

@MethodsReturnNonnullByDefault
public abstract class CrockPotCropsBlock extends CropsBlock {
    protected CrockPotCropsBlock() {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.CROP));
    }

    @Override
    protected abstract IItemProvider getSeedsItem();
}
