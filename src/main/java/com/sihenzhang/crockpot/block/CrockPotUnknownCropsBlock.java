package com.sihenzhang.crockpot.block;

import com.google.common.collect.ImmutableList;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrockPotUnknownCropsBlock extends CrockPotCropsBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    private static final VoxelShape[] SHAPE_BY_AGE = {
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D)
    };
    private static final Lock LOCK = new ReentrantLock();
    private static volatile List<Block> CROPS_BLOCKS = null;

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 1;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
    }

    protected static List<Block> getCropsBlocks() {
        if (CROPS_BLOCKS == null) {
            LOCK.lock();
            try {
                if (CROPS_BLOCKS == null) {
                    List<Block> tmp = new ArrayList<>();
                    for (String key : CrockPotConfig.UNKNOWN_SEEDS_CROPS_LIST.get()) {
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
                        BlockItem blockItem = item instanceof BlockItem ? (BlockItem) item : null;
                        if (blockItem != null && blockItem.getBlock() instanceof IPlantable) {
                            tmp.add(blockItem.getBlock());
                        } else {
                            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(key));
                            if (block != null && block != Blocks.AIR) {
                                tmp.add(block);
                            }
                        }
                    }
                    CROPS_BLOCKS = ImmutableList.copyOf(tmp);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return CROPS_BLOCKS;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 1)) {
            return;
        }
        if (worldIn.getRawBrightness(pos, 0) >= 9) {
            float growthChance = getGrowthSpeed(this, worldIn, pos);
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                worldIn.setBlock(pos, getCropsBlocks().get(random.nextInt(getCropsBlocks().size())).defaultBlockState(), 2);
                ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
        }
    }

    @Override
    public void growCrops(World worldIn, BlockPos pos, BlockState state) {
        Block block = getCropsBlocks().get(worldIn.random.nextInt(getCropsBlocks().size()));
        int age = this.getBonemealAgeIncrease(worldIn) - 1;
        if (block instanceof CrockPotDoubleCropsBlock) {
            CrockPotDoubleCropsBlock cropsBlock = (CrockPotDoubleCropsBlock) block;
            int maxAge = cropsBlock.getMaxGrowthAge(cropsBlock.defaultBlockState());
            if (age > maxAge) {
                worldIn.setBlock(pos, cropsBlock.getStateForAge(maxAge), 2);
                if (worldIn.isEmptyBlock(pos.above())) {
                    worldIn.setBlock(pos.above(), cropsBlock.getStateForAge(age), 2);
                }
            } else {
                worldIn.setBlock(pos, cropsBlock.getStateForAge(age), 2);
            }
        } else if (block instanceof CropsBlock) {
            CropsBlock cropsBlock = (CropsBlock) block;
            worldIn.setBlock(pos, cropsBlock.getStateForAge(Math.min(age, cropsBlock.getMaxAge())), 2);
        }
        worldIn.setBlock(pos, block.defaultBlockState(), 2);
    }

    @Override
    protected IItemProvider getBaseSeedId() {
        return CrockPotRegistry.unknownSeeds;
    }
}
