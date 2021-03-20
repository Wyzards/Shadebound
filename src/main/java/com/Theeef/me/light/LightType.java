package com.Theeef.me.light;

import com.Theeef.me.Shadebound;
import com.Theeef.me.util.NBTHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class LightType {

    public static LightType TORCH = new LightType("TORCH", 1000, Sets.newHashSet(Material.TORCH, Material.WALL_TORCH), false, true);
    public static LightType CAMPFIRE = new LightType("CAMPFIRE", 2500, Material.CAMPFIRE, false, false);
    public static LightType JACK_O_LANTERN = new LightType("JACK_O_LANTERN", 1000, Material.JACK_O_LANTERN, true, true);
    public static LightType FIRE = new LightType("FIRE", 100, Material.FIRE, false, false);

    private final String name;
    private final int maxFuel;
    private final Set<Material> blocks;
    private final boolean ignoresWeather;
    private final boolean canBeMoved;

    public LightType(String name, int maxFuel, Set<Material> blocks, boolean ignoresWeather, boolean canBeMoved) {
        this.name = name;
        this.maxFuel = maxFuel;
        this.blocks = blocks;
        this.ignoresWeather = ignoresWeather;
        this.canBeMoved = canBeMoved;
    }

    public LightType(String name, int maxFuel, Material block, boolean ignoresWeather, boolean canBeMoved) {
        this(name, maxFuel, Sets.newHashSet(block), ignoresWeather, canBeMoved);
    }

    public static Set<LightType> values() {
        return Sets.newHashSet(LightType.TORCH, LightType.CAMPFIRE, LightType.JACK_O_LANTERN, LightType.FIRE);
    }

    public static boolean isLightMaterial(Material material) {
        for (LightType type : LightType.values())
            if (type.getBlocks().contains(material))
                return true;

        return false;
    }

    public static LightType getByMaterial(Material material) {
        for (LightType type : LightType.values())
            if (type.getBlocks().contains(material))
                return type;

        throw new IllegalArgumentException(material.name() + " is not a valid LightType");
    }

    public static Material extinguishesTo(Material material) {
        switch (material) {
            case TORCH:
                return Material.REDSTONE_TORCH;
            case WALL_TORCH:
                return Material.REDSTONE_WALL_TORCH;
            case CAMPFIRE:
                return Material.CAMPFIRE;
            case JACK_O_LANTERN:
                return Material.CARVED_PUMPKIN;
            case FIRE:
                return Material.AIR;
            default:
                throw new IllegalArgumentException(material.name() + " cannot be extinguished!");
        }
    }

    public static LightType getByName(String string) {
        for (LightType type : LightType.values())
            if (type.name.equalsIgnoreCase(string))
                return type;

        throw new NullPointerException("There is no LightType with the name: " + string);
    }


    public boolean isLight(Block block) {
        return Shadebound.getCfm().getLights().contains(this.name + "." + block.getWorld().getName() + "." + block.getX() + "," + block.getY() + "," + block.getZ());
    }

    public ItemStack getFueledItem(Block block) {
        if (this.name.equals("TORCH")) {
            return fueledTorch(LightDecay.getLight(this, block));
        }

        if (this.name.equals("JACK_O_LANTERN"))
            return fueledJackOLantern(LightDecay.getLight(this, block));

        throw new NullPointerException("LightType " + this.name + " cannot be moved");
    }

    public static ItemStack fueledJackOLantern(double fuel) {
        ItemStack item = new ItemStack(Material.JACK_O_LANTERN, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setLore(Lists.newArrayList(ChatColor.GRAY + "Fuel: " + fuel));
        item.setItemMeta(meta);

        return NBTHandler.addString(item, "lightFuel", Double.toString(fuel));
    }

    public static ItemStack fueledTorch(double fuel) {
        ItemStack item = new ItemStack(Material.TORCH, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setLore(Lists.newArrayList(ChatColor.GRAY + "Fuel: " + fuel));
        item.setItemMeta(meta);

        return NBTHandler.addString(item, "lightFuel", Double.toString(fuel));
    }

    public Set<Material> getBlocks() {
        return this.blocks;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxFuel() {
        return this.maxFuel;
    }

    public boolean ignoresWeather() {
        return this.ignoresWeather;
    }

    public boolean canBeMoved() {
        return this.canBeMoved;
    }
}
