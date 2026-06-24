package com.sihenzhang.crockpot.attachment;

import com.sihenzhang.crockpot.CrockPot;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ModAttachmentTypes {
    private ModAttachmentTypes() {
    }

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CrockPot.MOD_ID);

    public static final Supplier<AttachmentType<FoodCounter>> FOOD_COUNTER = ATTACHMENT_TYPES.register(
            "food_counter",
            () -> AttachmentType.serializable(FoodCounter::new)
                    .sync((holder, to) -> holder == to, FoodCounter.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );
}
