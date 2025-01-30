package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.block.BirdcageBlock;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.common.UsernameCache;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;

import java.util.List;
import java.util.Optional;

public enum BirdcageProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (config.get(ModIntegrationJade.BIRDCAGE)) {
            var serverData = accessor.getServerData();
            if (serverData.contains("OwnerName")) {
                tooltip.add(Component.translatable("jade.owner", serverData.getString("OwnerName")));
            } else {
                var parrots = this.getParrotsInBirdcage(accessor.getLevel(), accessor.getPosition(), accessor.getBlockState());
                if (!parrots.isEmpty()) {
                    var parrot = parrots.get(0);
                    var ownerUUID = parrot.getOwnerUUID();
                    if (ownerUUID != null) {
                        var username = UsernameCache.getLastKnownUsername(ownerUUID);
                        tooltip.add(Component.translatable("jade.owner", username == null ? "???" : username));
                    }
                }
            }
            if (serverData.contains("OutputBuffer", Tag.TAG_LIST)) {
                var elements = tooltip.getElementHelper();
                var outputBuffer = serverData.getList("OutputBuffer", Tag.TAG_COMPOUND);
                for (var i = 0; i < outputBuffer.size(); i++) {
                    var output = outputBuffer.getCompound(i);
                    tooltip.add(elements.item(ItemStack.of(output.getCompound("Item"))));
                    var progress = (float) (BirdcageBlockEntity.OUTPUT_COOLDOWN - output.getLong("Time")) / (float) BirdcageBlockEntity.OUTPUT_COOLDOWN;
                    tooltip.append(elements.progress(progress, Component.literal((int) (progress * 100) + "%"), elements.progressStyle(), BoxStyle.DEFAULT, false).size(new Vec2(60, 14)).translate(new Vec2(0, 4)));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        var level = accessor.getLevel();
        var pos = accessor.getPosition();
        var state = accessor.getBlockState();
        var parrots = this.getParrotsInBirdcage(level, pos, state);
        if (!parrots.isEmpty()) {
            Optional.ofNullable(parrots.get(0))
                    .map(Parrot::getOwnerUUID)
                    .map(UsernameCache::getLastKnownUsername)
                    .ifPresent(username -> data.putString("OwnerName", username));
        }
        if (accessor.getBlockEntity() instanceof BirdcageBlockEntity birdcageBlockEntity) {
            var list = new ListTag();
            for (var output : birdcageBlockEntity.getOutputBuffer()) {
                var tag = new CompoundTag();
                tag.put("Item", output.getFirst().serializeNBT());
                tag.putLong("Time", output.getSecond() - accessor.getLevel().getGameTime());
                list.add(tag);
            }
            data.put("OutputBuffer", list);
        }
    }

    private List<Parrot> getParrotsInBirdcage(Level level, BlockPos pos, BlockState state) {
        var lowerPos = state.getValue(BirdcageBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
        return level.getEntitiesOfClass(Parrot.class, new AABB(lowerPos.getX(), lowerPos.getY(), lowerPos.getZ(), lowerPos.getX() + 1.0D, lowerPos.getY() + 2.0D, lowerPos.getZ() + 1.0D));
    }

    @Override
    public ResourceLocation getUid() {
        return ModIntegrationJade.BIRDCAGE;
    }
}
