package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.integration.curios.CuriosUtils;
import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import com.sihenzhang.crockpot.tag.ModItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class MilkmadeHatItem extends Item {
    public MilkmadeHatItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
        return !hasEquippedCurioHat(entity) && super.canEquip(stack, slot, entity);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (hasEquippedCurioHat(player)) {
            return InteractionResult.FAIL;
        }
        return super.use(level, player, hand);
    }

    private static boolean hasEquippedCurioHat(LivingEntity entity) {
        return ModList.get().isLoaded(ModIntegrationCurios.MOD_ID)
                && CuriosUtils.anyMatchInEquippedCurios(entity, ModItemTags.MILKMADE_HATS);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if (slot == EquipmentSlot.HEAD && owner instanceof Player player) {
            feedPlayer(itemStack, level, player, item -> player.onEquippedItemBroken(item, EquipmentSlot.HEAD));
        }
    }

    public static void feedPlayer(ItemStack itemStack, ServerLevel level, Player player, Consumer<Item> onBreak) {
        if (player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(itemStack)) {
            var useCooldown = itemStack.get(DataComponents.USE_COOLDOWN);
            // Apply the cooldown before durability loss can empty the stack.
            if (useCooldown != null) {
                useCooldown.apply(itemStack, player);
            }
            itemStack.hurtAndBreak(1, level, player, onBreak);
            player.getFoodData().eat(1, 0.05F);
        }
    }
}
