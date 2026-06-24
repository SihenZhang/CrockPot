package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ExplosionCraftingEvent {
    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (event.getLevel() instanceof ServerLevel level) {
            var affectedBlocks = event.getAffectedBlocks();
            var affectedEntities = event.getAffectedEntities();
            affectedBlocks.forEach(affectedBlock -> {
                var blockState = level.getBlockState(affectedBlock);
                var container = new ExplosionCraftingRecipe.Wrapper(blockState.getBlock().asItem().getDefaultInstance(), true);
                var optionalRecipe = level.recipeAccess().getRecipeFor(ModRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get(), container, level);
                if (optionalRecipe.isPresent()) {
                    blockState.onBlockExploded(level, affectedBlock, event.getExplosion());
                    spawnAsInvulnerableEntity(level, affectedBlock, optionalRecipe.get().value().assemble(container));
                }
            });
            affectedEntities.forEach(affectedEntity -> {
                if (affectedEntity instanceof ItemEntity itemEntity && affectedEntity.isAlive()) {
                    var container = new ExplosionCraftingRecipe.Wrapper(itemEntity.getItem());
                    var optionalRecipe = level.recipeAccess().getRecipeFor(ModRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get(), container, level);
                    if (optionalRecipe.isPresent()) {
                        while (!itemEntity.getItem().isEmpty()) {
                            shrinkItemEntity(itemEntity, 1);
                            spawnAsInvulnerableEntity(level, itemEntity.blockPosition(), optionalRecipe.get().value().assemble(container));
                        }
                    }
                }
            });
        }
    }

    private static void spawnAsInvulnerableEntity(ServerLevel level, BlockPos pos, ItemStack stack) {
        if (!stack.isEmpty()) {
            var random = level.getRandom();
            var x = pos.getX() + Mth.nextDouble(random, 0.25, 0.75);
            var y = pos.getY() + Mth.nextDouble(random, 0.25, 0.75);
            var z = pos.getZ() + Mth.nextDouble(random, 0.25, 0.75);
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
