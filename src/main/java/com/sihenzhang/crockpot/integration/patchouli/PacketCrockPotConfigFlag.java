package com.sihenzhang.crockpot.integration.patchouli;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.io.IOUtils;
import vazkii.patchouli.api.PatchouliAPI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PacketCrockPotConfigFlag {
    private final String data;

    public PacketCrockPotConfigFlag(String data) {
        this.data = data;
    }

    public static void serialize(PacketCrockPotConfigFlag pack, PacketBuffer buf) {
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

    public static PacketCrockPotConfigFlag deserialize(PacketBuffer buf) {
        try {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(buf.readByteArray()));
            String data = IOUtils.toString(gis, StandardCharsets.UTF_8);
            gis.close();
            return new PacketCrockPotConfigFlag(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress", e);
        }
    }

    public static void handle(PacketCrockPotConfigFlag pack, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (ModList.get().isLoaded(ModIntegrationPatchouli.MOD_ID)) {
                JsonObject flags = JSONUtils.parse(pack.data);
                flags.entrySet().forEach(entry -> PatchouliAPI.get().setConfigFlag(entry.getKey(), entry.getValue().getAsBoolean()));
                PatchouliAPI.get().reloadBookContents();
            }
        });
        context.setPacketHandled(true);
    }
}
