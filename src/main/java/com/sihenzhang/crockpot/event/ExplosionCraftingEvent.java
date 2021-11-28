package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ExplosionCraftingEvent {
    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        World world = event.getWorld();
        if (!event.getWorld().isClientSide) {
            List<BlockPos> affectedBlocks = event.getAffectedBlocks();
            List<Entity> affectedEntities = event.getAffectedEntities();
            affectedBlocks.forEach(affectedBlock -> {
                BlockState blockState = world.getBlockState(affectedBlock);
                ExplosionCraftingRecipe recipe = ExplosionCraftingRecipe.getRecipeFor(blockState, world.getRecipeManager());
                if (recipe != null) {
                    blockState.onBlockExploded(world, affectedBlock, event.getExplosion());
                    spawnAsInvulnerableEntity(world, affectedBlock, recipe.assemble(world.random));
                }
            });
            affectedEntities.forEach(affectedEntity -> {
                if (affectedEntity instanceof ItemEntity && affectedEntity.isAlive()) {
                    ItemEntity itemEntity = (ItemEntity) affectedEntity;
                    ExplosionCraftingRecipe recipe = ExplosionCraftingRecipe.getRecipeFor(itemEntity.getItem(), world.getRecipeManager());
                    if (recipe != null) {
                        while (!itemEntity.getItem().isEmpty()) {
                            shrinkItemEntity(itemEntity, 1);
                            spawnAsInvulnerableEntity(world, itemEntity.blockPosition(), recipe.assemble(world.random));
                        }
                    }
                }
            });
        }
    }

    private static void spawnAsInvulnerableEntity(World worldIn, BlockPos pos, ItemStack stack) {
        if (!worldIn.isClientSide && !stack.isEmpty()) {
            double x = pos.getX() + MathHelper.nextDouble(worldIn.random, 0.25, 0.75);
            double y = pos.getY() + MathHelper.nextDouble(worldIn.random, 0.25, 0.75);
            double z = pos.getZ() + MathHelper.nextDouble(worldIn.random, 0.25, 0.75);
            ItemEntity itemEntity = new ItemEntity(worldIn, x, y, z, stack);
            itemEntity.setDefaultPickUpDelay();
            itemEntity.setInvulnerable(true);
            worldIn.addFreshEntity(itemEntity);
        }
    }

    private static void shrinkItemEntity(ItemEntity itemEntity, int count) {
        itemEntity.setNeverPickUp();
        ItemStack itemStack = itemEntity.getItem().copy();
        itemStack.shrink(count);
        itemEntity.setItem(itemStack);
        itemEntity.setDefaultPickUpDelay();
    }
}
