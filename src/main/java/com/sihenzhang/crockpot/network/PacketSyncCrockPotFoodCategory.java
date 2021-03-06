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

public class PacketSyncCrockPotFoodCategory {
    private final String data;

    public PacketSyncCrockPotFoodCategory(String data) {
        this.data = data;
    }

    public static void serialize(PacketSyncCrockPotFoodCategory pack, PacketBuffer buf) {
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

    public static PacketSyncCrockPotFoodCategory deserialize(PacketBuffer buf) {
        String data;
        try {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(buf.readByteArray()));
            data = IOUtils.toString(gis, StandardCharsets.UTF_8);
            gis.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress", e);
        }
        return new PacketSyncCrockPotFoodCategory(data);
    }

    public static void handle(PacketSyncCrockPotFoodCategory pack, Supplier<NetworkEvent.Context> ctx) {
        CrockPot.FOOD_CATEGORY_MANAGER.deserialize(pack.data);
        ctx.get().setPacketHandled(true);
    }
}
