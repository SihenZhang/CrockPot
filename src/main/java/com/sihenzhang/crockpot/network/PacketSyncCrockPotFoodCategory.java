package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.CrockPot;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
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

    @SuppressWarnings("deprecation")
    public static void serialize(PacketSyncCrockPotFoodCategory pack, PacketBuffer buf) {
        try {
            ByteOutputStream bos = new ByteOutputStream();
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
            data = new String(IOUtils.readAllBytes(gis), StandardCharsets.UTF_8);
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
