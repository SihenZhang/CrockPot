package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.curios.CuriosUtils;
import com.sihenzhang.crockpot.integration.curios.MilkmadeHatCuriosCapabilityProvider;
import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class MilkmadeHatItem extends Item {
    public MilkmadeHatItem() {
        this(new Properties().tab(CrockPot.ITEM_GROUP).durability(180).setNoRepair());
    }

    protected MilkmadeHatItem(Properties properties) {
        super(properties);
        // disable Dispenser behavior of equipping items because it does not call canEquip method
        // DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Nullable
    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
        return EquipmentSlotType.HEAD;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID) && entity instanceof LivingEntity && CuriosUtils.anyMatchInEquippedCurios((LivingEntity) entity, MilkmadeHatItem.class)) {
            return false;
        }
        return super.canEquip(stack, armorType, entity);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID) && CuriosUtils.anyMatchInEquippedCurios(playerIn, MilkmadeHatItem.class)) {
            return ActionResult.fail(stack);
        }
        EquipmentSlotType equipmentSlotForItem = MobEntity.getEquipmentSlotForItem(stack);
        ItemStack stackBySlot = playerIn.getItemBySlot(equipmentSlotForItem);
        if (stackBySlot.isEmpty()) {
            playerIn.setItemSlot(equipmentSlotForItem, stack.copy());
            stack.setCount(0);
            return ActionResult.sidedSuccess(stack, worldIn.isClientSide);
        } else {
            return ActionResult.fail(stack);
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
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (!world.isClientSide && player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(this)) {
            stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(EquipmentSlotType.HEAD));
            player.getFoodData().eat(1, 0.05F);
            player.getCooldowns().addCooldown(this, 100);
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID)) {
            return new MilkmadeHatCuriosCapabilityProvider(stack, nbt);
        }
        return super.initCapabilities(stack, nbt);
    }
}
