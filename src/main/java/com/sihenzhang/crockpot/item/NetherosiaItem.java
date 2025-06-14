package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class NetherosiaItem extends BlockItem {
    public NetherosiaItem() {
        super(CrockPotBlocks.NETHEROSIA.get(), new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResult place(BlockPlaceContext pContext) {
        if (!pContext.isSecondaryUseActive() && !pContext.replacingClickedOnBlock()) {
            return InteractionResult.FAIL;
        }
        return super.place(pContext);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(I18nUtils.createTooltipComponent("netherosia").withStyle(ChatFormatting.DARK_AQUA));
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(I18nUtils.createTooltipComponent("placeable_while_sneaking").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
