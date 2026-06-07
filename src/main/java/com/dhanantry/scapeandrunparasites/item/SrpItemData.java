package com.dhanantry.scapeandrunparasites.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class SrpItemData {
    public static final String KILLS = "srpkills";
    public static final String HITS = "srphits";

    private SrpItemData() {
    }

    public static int getInt(ItemStack stack, String key) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return data.copyTag().getInt(key);
    }

    public static void setInt(ItemStack stack, String key, int value) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(key, Math.max(0, value)));
    }

    public static void addInt(ItemStack stack, String key, int amount) {
        setInt(stack, key, getInt(stack, key) + amount);
    }
}
