package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.block.BirdcageBlock;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.UsernameCache;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

import java.util.Optional;

public enum BirdcageProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        var data = new CompoundTag();
        getOwnerName(accessor).ifPresent(name -> data.putString("OwnerName", name));
        if (accessor.getLevel().getBlockEntity(getLowerPos(accessor)) instanceof BirdcageBlockEntity birdcage) {
            var outputs = new ListTag();
            for (var output : birdcage.getOutputBuffer()) {
                var entry = new CompoundTag();
                entry.put("Item", accessor.encodeAsNbt(ItemStack.STREAM_CODEC, output.getFirst()));
                entry.putLong("Time", output.getSecond() - accessor.getLevel().getGameTime());
                outputs.add(entry);
            }
            data.put("OutputBuffer", outputs);
        }
        tag.put(getUid().toString(), data);
    }

    private static BlockPos getLowerPos(BlockAccessor accessor) {
        return accessor.getBlockState().getValue(BirdcageBlock.HALF) == DoubleBlockHalf.LOWER
                ? accessor.getPosition() : accessor.getPosition().below();
    }

    static Optional<String> getOwnerName(BlockAccessor accessor) {
        var pos = getLowerPos(accessor);
        var parrots = accessor.getLevel().getEntitiesOfClass(Parrot.class,
                new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 2.0D, pos.getZ() + 1.0D));
        if (parrots.isEmpty()) {
            return Optional.empty();
        }
        var parrot = parrots.getFirst();
        var owner = parrot.getOwner();
        if (owner != null) {
            return Optional.of(owner.getName().getString());
        }
        var ownerReference = parrot.getOwnerReference();
        if (ownerReference == null) {
            return Optional.empty();
        }
        var username = UsernameCache.getLastKnownUsername(ownerReference.getUUID());
        return Optional.of(username == null ? "???" : username);
    }

    @Override
    public Identifier getUid() {
        return ModIntegrationJade.BIRDCAGE;
    }
}
