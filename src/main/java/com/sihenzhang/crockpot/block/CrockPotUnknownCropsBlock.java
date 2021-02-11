package com.sihenzhang.crockpot.block;

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

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrockPotUnknownCropsBlock extends CrockPotCropsBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_1;
    private static final VoxelShape[] SHAPE_BY_AGE = {
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D)
    };
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
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
    }

    protected static List<Block> getCropsBlocks() {
        if (CROPS_BLOCKS == null) {
            synchronized (CrockPotUnknownCropsBlock.class) {
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
                    CROPS_BLOCKS = tmp;
                }
            }
        }
        return CROPS_BLOCKS;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 1)) {
            return;
        }
        if (worldIn.getLightSubtracted(pos, 0) >= 9) {
            float growthChance = getGrowthChance(this, worldIn, pos);
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                worldIn.setBlockState(pos, getCropsBlocks().get(random.nextInt(getCropsBlocks().size())).getDefaultState(), 2);
                ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
        }
    }

    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
        Block block = getCropsBlocks().get(worldIn.rand.nextInt(getCropsBlocks().size()));
        int age = this.getBonemealAgeIncrease(worldIn) - 1;
        if (block instanceof CrockPotDoubleCropsBlock) {
            CrockPotDoubleCropsBlock cropsBlock = (CrockPotDoubleCropsBlock) block;
            int maxAge = cropsBlock.getMaxGrowthAge(cropsBlock.getDefaultState());
            if (age > maxAge) {
                worldIn.setBlockState(pos, cropsBlock.withAge(maxAge), 2);
                if (worldIn.isAirBlock(pos.up())) {
                    worldIn.setBlockState(pos.up(), cropsBlock.withAge(age), 2);
                }
            } else {
                worldIn.setBlockState(pos, cropsBlock.withAge(age), 2);
            }
        } else if (block instanceof CropsBlock) {
            CropsBlock cropsBlock = (CropsBlock) block;
            worldIn.setBlockState(pos, cropsBlock.withAge(Math.min(age, cropsBlock.getMaxAge())), 2);
        }
        worldIn.setBlockState(pos, block.getDefaultState(), 2);
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return CrockPotRegistry.unknownSeeds.get();
    }
}
