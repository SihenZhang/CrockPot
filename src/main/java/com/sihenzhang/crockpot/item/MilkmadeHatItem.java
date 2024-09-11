package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.integration.curios.CuriosUtils;
import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import com.sihenzhang.crockpot.tag.ModItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.fml.ModList;

import javax.annotation.Nullable;

public class MilkmadeHatItem extends Item {
    public MilkmadeHatItem() {
        this(new Properties().durability(180).setNoRepair());
    }

    protected MilkmadeHatItem(Properties pProperties) {
        super(pProperties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Nullable
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID) && CuriosUtils.anyMatchInEquippedCurios(entity, ModItemTags.MILKMADE_HATS)) {
            return false;
        }
        return super.canEquip(stack, armorType, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        var stack = pPlayer.getItemInHand(pUsedHand);
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID) && CuriosUtils.anyMatchInEquippedCurios(pPlayer, ModItemTags.MILKMADE_HATS)) {
            return InteractionResultHolder.fail(stack);
        }
        var equipmentSlotForItem = pPlayer.getEquipmentSlotForItem(stack);
        var stackBySlot = pPlayer.getItemBySlot(equipmentSlotForItem);
        if (stackBySlot.isEmpty()) {
            pPlayer.setItemSlot(equipmentSlotForItem, stack.copy());
            stack.setCount(0);
            return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player && player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(this) && slotId == 36) {
            stack.hurtAndBreak(1, player, EquipmentSlot.HEAD);
            player.getFoodData().eat(1, 0.05F);
            player.getCooldowns().addCooldown(this, 100);
        }
    }
}
