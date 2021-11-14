package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PacketSyncExplosionCraftingRecipe {
    private final String data;

    public PacketSyncExplosionCraftingRecipe(String data) {
        this.data = data;
    }

    public static void serialize(PacketSyncExplosionCraftingRecipe pack, PacketBuffer buf) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(bos);
            gos.write(pack.data.getBytes(StandardCharsets.UTF_8));
            gos.close();
            buf.writeByteArray(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Unable to compress", e);
        }
    }

    public static PacketSyncExplosionCraftingRecipe deserialize(PacketBuffer buf) {
        try {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(buf.readByteArray()));
            String data = IOUtils.toString(gis, StandardCharsets.UTF_8);
            gis.close();
            return new PacketSyncExplosionCraftingRecipe(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress", e);
        }
    }

    public static void handle(PacketSyncExplosionCraftingRecipe pack, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> CrockPot.EXPLOSION_CRAFTING_RECIPE_MANAGER.deserialize(pack.data));
        context.setPacketHandled(true);
    }
}
