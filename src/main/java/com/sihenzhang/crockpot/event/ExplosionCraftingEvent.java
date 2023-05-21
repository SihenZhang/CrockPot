package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ExplosionCraftingEvent {
    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        var level = event.getLevel();
        if (!level.isClientSide) {
            var affectedBlocks = event.getAffectedBlocks();
            var affectedEntities = event.getAffectedEntities();
            affectedBlocks.forEach(affectedBlock -> {
                var blockState = level.getBlockState(affectedBlock);
                var container = new SimpleContainer(blockState.getBlock().asItem().getDefaultInstance(), ExplosionCraftingRecipe.FROM_BLOCK_FLAG);
                var optionalRecipe = level.getRecipeManager().getRecipeFor(CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get(), container, level);
                if (optionalRecipe.isPresent()) {
                    blockState.onBlockExploded(level, affectedBlock, event.getExplosion());
                    spawnAsInvulnerableEntity(level, affectedBlock, optionalRecipe.get().assemble(container, level.registryAccess()));
                }
            });
            affectedEntities.forEach(affectedEntity -> {
                if (affectedEntity instanceof ItemEntity itemEntity && affectedEntity.isAlive()) {
                    var container = new SimpleContainer(itemEntity.getItem());
                    var optionalRecipe = level.getRecipeManager().getRecipeFor(CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get(), container, level);
                    if (optionalRecipe.isPresent()) {
                        while (!itemEntity.getItem().isEmpty()) {
                            shrinkItemEntity(itemEntity, 1);
                            spawnAsInvulnerableEntity(level, itemEntity.blockPosition(), optionalRecipe.get().assemble(container, level.registryAccess()));
                        }
                    }
                }
            });
        }
    }

    private static void spawnAsInvulnerableEntity(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty()) {
            var x = pos.getX() + Mth.nextDouble(level.random, 0.25, 0.75);
            var y = pos.getY() + Mth.nextDouble(level.random, 0.25, 0.75);
            var z = pos.getZ() + Mth.nextDouble(level.random, 0.25, 0.75);
            var itemEntity = new ItemEntity(level, x, y, z, stack);
            itemEntity.setDefaultPickUpDelay();
            itemEntity.setInvulnerable(true);
            level.addFreshEntity(itemEntity);
        }
    }

    private static void shrinkItemEntity(ItemEntity itemEntity, int count) {
        itemEntity.setNeverPickUp();
        var itemStack = itemEntity.getItem().copy();
        itemStack.shrink(count);
        itemEntity.setItem(itemStack);
        itemEntity.setDefaultPickUpDelay();
    }
}
