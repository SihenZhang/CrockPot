package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.curios.CuriosUtils;
import com.sihenzhang.crockpot.integration.curios.MilkmadeHatCuriosCapabilityProvider;
import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
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
        super(new Properties().tab(CrockPot.ITEM_GROUP).durability(180).setNoRepair());
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    protected MilkmadeHatItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
        return EquipmentSlotType.HEAD;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        ItemStack stackBySlot = playerIn.getItemBySlot(EquipmentSlotType.HEAD);
        if (stackBySlot.isEmpty()) {
            playerIn.setItemSlot(EquipmentSlotType.HEAD, stack.copy());
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
        if (!world.isClientSide && player.getFoodData().needsFood() && player.tickCount % 100 == 0) {
            if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID) && CuriosUtils.anyMatchInEquippedCurios(player, MilkmadeHatItem.class)) {
                return;
            }
            stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(EquipmentSlotType.HEAD));
            player.getFoodData().eat(1, 0.05F);
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
