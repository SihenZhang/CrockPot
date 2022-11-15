package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.integration.curios.CuriosUtils;
import com.sihenzhang.crockpot.integration.curios.MilkmadeHatCuriosCapabilityProvider;
import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class MilkmadeHatItem extends CrockPotBaseItem {
    public MilkmadeHatItem() {
        this(new Properties().durability(180).setNoRepair());
    }

    protected MilkmadeHatItem(Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Nullable
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID) && entity instanceof LivingEntity livingEntity && CuriosUtils.anyMatchInEquippedCurios(livingEntity, CrockPotItemTags.MILKMADE_HATS)) {
            return false;
        }
        return super.canEquip(stack, armorType, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID) && CuriosUtils.anyMatchInEquippedCurios(player, CrockPotItemTags.MILKMADE_HATS)) {
            return InteractionResultHolder.fail(stack);
        }
        var equipmentSlotForItem = LivingEntity.getEquipmentSlotForItem(stack);
        var stackBySlot = player.getItemBySlot(equipmentSlotForItem);
        if (stackBySlot.isEmpty()) {
            player.setItemSlot(equipmentSlotForItem, stack.copy());
            stack.setCount(0);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide && player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(this)) {
            stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(EquipmentSlot.HEAD));
            player.getFoodData().eat(1, 0.05F);
            player.getCooldowns().addCooldown(this, 100);
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID)) {
            return new MilkmadeHatCuriosCapabilityProvider(stack, nbt);
        }
        return super.initCapabilities(stack, nbt);
    }
}
