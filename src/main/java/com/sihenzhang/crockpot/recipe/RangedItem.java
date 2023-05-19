package com.sihenzhang.crockpot.recipe;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class RangedItem {
    public final Item item;
    public final int min;
    public final int max;

    public RangedItem(Item item, int min, int max) {
        Preconditions.checkArgument(min >= 0 || max >= 0, "The count of RangedItem should not be less than 0");
        if (min == 0 && max == 0) {
            CrockPot.LOGGER.warn("The count of RangedItem is 0, make sure this is intentional!");
        }
        if (min > max) {
            CrockPot.LOGGER.warn("The minimum count of RangedItem is greater than the maximum count, make sure this is intentional!");
        }
        this.item = item;
        this.min = min;
        this.max = max;
    }

    public RangedItem(Item item, int count) {
        this(item, count, count);
    }

    public boolean isRanged() {
        return min != max;
    }

    public ItemStack getInstance(RandomSource random) {
        if (this.isRanged()) {
            return new ItemStack(item, Mth.nextInt(random, min, max));
        }
        return new ItemStack(item, min);
    }

    public static RangedItem fromJson(JsonElement json) {
        if (json == null || json.isJsonNull()) {
            throw new JsonSyntaxException("Json cannot be null");
        }
        var obj = GsonHelper.convertToJsonObject(json, "ranged item");
        var item = JsonUtils.getAsItem(obj, "item");
        if (item != null) {
            if (obj.has("count")) {
                var e = obj.get("count");
                if (e.isJsonObject()) {
                    var count = e.getAsJsonObject();
                    if (count.has("min") && count.has("max")) {
                        var min = GsonHelper.getAsInt(count, "min");
                        var max = GsonHelper.getAsInt(count, "max");
                        return new RangedItem(item, min, max);
                    } else {
                        var minOrMax = GsonHelper.getAsInt(count, "min", GsonHelper.getAsInt(count, "max", 1));
                        return new RangedItem(item, minOrMax);
                    }
                } else {
                    var count = GsonHelper.getAsInt(obj, "count", 1);
                    return new RangedItem(item, count);
                }
            } else {
                return new RangedItem(item, 1);
            }
        } else {
            return null;
        }
    }

    public JsonElement toJson() {
        var obj = new JsonObject();
        obj.addProperty("item", ForgeRegistries.ITEMS.getKey(this.item).toString());
        if (this.isRanged()) {
            var count = new JsonObject();
            count.addProperty("min", this.min);
            count.addProperty("max", this.max);
            obj.add("count", count);
        } else if (this.min > 1) {
            obj.addProperty("count", this.min);
        }
        return obj;
    }

    public static RangedItem fromNetwork(FriendlyByteBuf buffer) {
        Item item = Item.byId(buffer.readVarInt());
        int min = buffer.readByte();
        int max = buffer.readByte();
        return new RangedItem(item, min, max);
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(Item.getId(this.item));
        buffer.writeByte(this.min);
        buffer.writeByte(this.max);
    }
}
