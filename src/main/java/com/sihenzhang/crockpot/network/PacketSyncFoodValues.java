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

public class PacketSyncFoodValues {
    private final String data;

    public PacketSyncFoodValues(String data) {
        this.data = data;
    }

    public static void serialize(PacketSyncFoodValues pack, PacketBuffer buf) {
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

    public static PacketSyncFoodValues deserialize(PacketBuffer buf) {
        try {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(buf.readByteArray()));
            String data = IOUtils.toString(gis, StandardCharsets.UTF_8);
            gis.close();
            return new PacketSyncFoodValues(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress", e);
        }
    }

    public static void handle(PacketSyncFoodValues pack, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> CrockPot.FOOD_VALUES_MANAGER.deserialize(pack.data));
        context.setPacketHandled(true);
    }
}
