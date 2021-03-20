package com.Theeef.me.util;

import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;

public class NBTHandler {
    public static NBTTagCompound getTag(org.bukkit.inventory.ItemStack item) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        return nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
    }

    public static org.bukkit.inventory.ItemStack setTag(org.bukkit.inventory.ItemStack item, NBTTagCompound tag) {
        ItemStack itemNms = CraftItemStack.asNMSCopy(item);
        itemNms.setTag(tag);
        item.setItemMeta(CraftItemStack.asBukkitCopy(itemNms).getItemMeta());

        return item;
    }

    public static org.bukkit.inventory.ItemStack addString(org.bukkit.inventory.ItemStack item, String name, String value) {
        NBTTagCompound tag = getTag(item);
        tag.setString(name, value);
        return setTag(item, tag);
    }

    public static org.bukkit.inventory.ItemStack removeString(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = getTag(item);
        tag.remove(name);
        return setTag(item, tag);
    }

    public static boolean hasString(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = getTag(item);
        return tag.hasKey(name);
    }

    public static String getString(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = getTag(item);
        return tag.getString(name);
    }
}