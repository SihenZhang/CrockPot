package com.sihenzhang.crockpot.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class DryingRackRenderState extends BlockEntityRenderState {
    public final List<ItemStackRenderState> items = new ArrayList<>();
    public Direction facing = Direction.NORTH;
    public boolean wall = false;
}
