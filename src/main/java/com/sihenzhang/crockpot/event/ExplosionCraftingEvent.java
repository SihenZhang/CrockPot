package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ExplosionCraftingEvent {
    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        Level level = event.getWorld();
        if (!event.getWorld().isClientSide) {
            List<BlockPos> affectedBlocks = event.getAffectedBlocks();
            List<Entity> affectedEntities = event.getAffectedEntities();
            affectedBlocks.forEach(affectedBlock -> {
                BlockState blockState = level.getBlockState(affectedBlock);
                ExplosionCraftingRecipe recipe = ExplosionCraftingRecipe.getRecipeFor(blockState, level.getRecipeManager());
                if (recipe != null) {
                    blockState.onBlockExploded(level, affectedBlock, event.getExplosion());
                    spawnAsInvulnerableEntity(level, affectedBlock, recipe.assemble(level.random));
                }
            });
            affectedEntities.forEach(affectedEntity -> {
                if (affectedEntity instanceof ItemEntity itemEntity && affectedEntity.isAlive()) {
                    ExplosionCraftingRecipe recipe = ExplosionCraftingRecipe.getRecipeFor(itemEntity.getItem(), level.getRecipeManager());
                    if (recipe != null) {
                        while (!itemEntity.getItem().isEmpty()) {
                            shrinkItemEntity(itemEntity, 1);
                            spawnAsInvulnerableEntity(level, itemEntity.blockPosition(), recipe.assemble(level.random));
                        }
                    }
                }
            });
        }
    }

    private static void spawnAsInvulnerableEntity(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty()) {
            double x = pos.getX() + Mth.nextDouble(level.random, 0.25, 0.75);
            double y = pos.getY() + Mth.nextDouble(level.random, 0.25, 0.75);
            double z = pos.getZ() + Mth.nextDouble(level.random, 0.25, 0.75);
            ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack);
            itemEntity.setDefaultPickUpDelay();
            itemEntity.setInvulnerable(true);
            level.addFreshEntity(itemEntity);
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
