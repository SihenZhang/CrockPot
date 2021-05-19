package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ExplosionCraftingEvent {
    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
//        World world = event.getWorld();
//        List<BlockPos> affectedBlocks = event.getAffectedBlocks();
//        List<Entity> affectedEntities = event.getAffectedEntities();
//        if (!event.getWorld().isRemote) {
//            for (BlockPos affectedBlock : affectedBlocks) {
//                BlockState blockState = world.getBlockState(affectedBlock);
//                if (blockState.isIn(Blocks.GILDED_BLACKSTONE)) {
//                    blockState.onBlockExploded(world, affectedBlock, event.getExplosion());
//                    if (world.rand.nextDouble() < 0.4) {
//                        spawnAsInvulnerableEntity(world, affectedBlock, CrockPotRegistry.collectedDust.getDefaultInstance());
//                    }
//                }
//            }
//            for (Entity affectedEntity : affectedEntities) {
//                if (affectedEntity instanceof ItemEntity && affectedEntity.isAlive()) {
//                    ItemEntity itemEntity = (ItemEntity) affectedEntity;
//                    while (!itemEntity.getItem().isEmpty() && itemEntity.getItem().getItem() == Blocks.GILDED_BLACKSTONE.asItem()) {
//                        shrinkItemEntity(itemEntity, 1);
//                        if (world.rand.nextDouble() < 0.4) {
//                            spawnAsInvulnerableEntity(world, itemEntity.getPosition(), CrockPotRegistry.collectedDust.getDefaultInstance());
//                        }
//                    }
//                }
//            }
//        }
    }

    private static void spawnAsInvulnerableEntity(World worldIn, BlockPos pos, ItemStack stack) {
        if (!worldIn.isRemote && !stack.isEmpty()) {
            double x = pos.getX() + MathHelper.nextDouble(worldIn.rand, 0.25, 0.75);
            double y = pos.getY() + MathHelper.nextDouble(worldIn.rand, 0.25, 0.75);
            double z = pos.getZ() + MathHelper.nextDouble(worldIn.rand, 0.25, 0.75);
            ItemEntity itemEntity = new ItemEntity(worldIn, x, y, z, stack);
            itemEntity.setDefaultPickupDelay();
            itemEntity.setInvulnerable(true);
            worldIn.addEntity(itemEntity);
        }
    }

    private static void shrinkItemEntity(ItemEntity itemEntity, int count) {
        itemEntity.setInfinitePickupDelay();
        ItemStack itemStack = itemEntity.getItem().copy();
        itemStack.shrink(count);
        itemEntity.setItem(itemStack);
        itemEntity.setDefaultPickupDelay();
    }
}
