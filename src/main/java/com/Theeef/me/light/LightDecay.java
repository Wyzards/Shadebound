package com.Theeef.me.light;

import com.Theeef.me.Shadebound;
import com.Theeef.me.util.NBTHandler;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class LightDecay implements Listener {

    public static void lightLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String blockType : Shadebound.getCfm().getLights().getKeys(false))
                    for (String world : Shadebound.getCfm().getLights().getConfigurationSection(blockType).getKeys(false))
                        for (String xyz : Shadebound.getCfm().getLights().getConfigurationSection(blockType + "." + world).getKeys(false)) {
                            LightType type = LightType.getByName(blockType);
                            Block block = Bukkit.getWorld(world).getBlockAt(Integer.parseInt(xyz.substring(0, xyz.indexOf(','))), Integer.parseInt(xyz.substring(xyz.indexOf(',') + 1, xyz.lastIndexOf(','))), Integer.parseInt(xyz.substring(xyz.lastIndexOf(',') + 1)));

                            if (type.getBlocks().contains(block.getType())) {
                                decreaseLight(type, block);
                            } else {
                                untrackLight(type, block);
                            }
                        }
            }
        }.runTaskTimer(Shadebound.getPlugin(Shadebound.class), 0, 20);
    }

    public static void setLight(LightType type, Block block, double fuel) {
        if (fuel <= 0) {
            untrackLight(type, block);
            extinguish(block);
        } else {
            Shadebound.getCfm().getLights().set(type.getName() + "." + block.getWorld().getName() + "." + block.getX() + "," + block.getY() + "," + block.getZ(), fuel);
            Shadebound.getCfm().saveLights();
        }
    }

    public static double getLight(LightType type, Block block) {
        return Shadebound.getCfm().getLights().getDouble(type.getName() + "." + block.getWorld().getName() + "." + block.getX() + "," + block.getY() + "," + block.getZ());
    }

    public static void trackLight(Block block) {
        setLight(LightType.getByMaterial(block.getType()), block, LightType.getByMaterial(block.getType()).getMaxFuel());
    }

    public static void untrackLight(LightType type, Block block) {
        Shadebound.getCfm().getLights().set(type.getName() + "." + block.getWorld().getName() + "." + block.getX() + "," + block.getY() + "," + block.getZ(), null);
        Shadebound.getCfm().saveLights();
    }

    public static void decreaseLight(LightType type, Block block) {
        double fuel = getLight(type, block);
        double temp = block.getTemperature();
        fuel -= (1.989652 - 1.495125 * temp + 0.3803238 * Math.pow(temp, 2)) * (block.getWorld().isClearWeather() ? 1 : (type.ignoresWeather() ? 1 : (temp >= .95 ? 1 : (block.getLightFromSky() > 12 ? 10 * (block.getLightFromSky() - 12) : 1))));

        if (fuel <= 0)
            extinguish(block);
        else
            setLight(type, block, fuel);
    }

    public static void extinguish(Block block) {
        BlockFace face = null;
        LightType lightType = LightType.getByMaterial(block.getType());

        if (block.getBlockData() instanceof Directional)
            face = ((Directional) block.getBlockData()).getFacing();

        block.setType(LightType.extinguishesTo(block.getType()));

        if (face != null) {
            Directional directional = (Directional) block.getBlockData();
            directional.setFacing(face);
            block.setBlockData(directional);
        }

        if (block.getBlockData() instanceof Lightable) {
            Lightable lightable = (Lightable) block.getBlockData();
            lightable.setLit(false);
            block.setBlockData(lightable);
        }

        untrackLight(lightType, block);
    }

    public static ItemStack burntTorch(int amount) {
        ItemStack item = new ItemStack(Material.LEVER, amount);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RESET + "Used Torch");
        meta.setLore(Lists.newArrayList(ChatColor.GRAY + "Combine with burnable items in", ChatColor.GRAY + "a crafting table to refuel.", "", ChatColor.GRAY + "Hold in offhand with a flint and steel,", ChatColor.GRAY + "torch, or fire charge in your main hand", ChatColor.GRAY + "and right click to light."));
        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void torchPlace(BlockPlaceEvent event) {
        Material type = event.getBlock().getType();

        if (type == Material.TORCH || type == Material.WALL_TORCH || type == Material.JACK_O_LANTERN || type == Material.CAMPFIRE)
            trackLight(event.getBlock());
    }

    @EventHandler
    public void playerBreakLight(BlockBreakEvent event) {
        if (LightType.isLightMaterial(event.getBlock().getType())) {
            LightType light = LightType.getByMaterial(event.getBlock().getType());

            if (light.canBeMoved() && light.isLight(event.getBlock())) {
                event.getBlock().getWorld().dropItem(event.getBlock().getLocation().add(0.5, 0.5, 0.5), light.getFueledItem(event.getBlock()));
            }
        }
    }

    @EventHandler
    public void torchExplode(BlockExplodeEvent event) {
        for (Block block : event.blockList())
            if (LightType.isLightMaterial(block.getType())) {
                LightType light = LightType.getByMaterial(block.getType());

                if (light.canBeMoved() && light.isLight(block)) {
                    block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), light.getFueledItem(block));
                }
            }
    }

    @EventHandler
    public void torchExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList())
            if (LightType.isLightMaterial(block.getType())) {
                LightType light = LightType.getByMaterial(block.getType());

                if (light.canBeMoved() && light.isLight(block)) {
                    block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), light.getFueledItem(block));
                }
            }
    }

    @EventHandler
    public void lightDropNaturally(ItemSpawnEvent event) {
        if (LightType.isLightMaterial(event.getEntity().getItemStack().getType()) && LightType.getByMaterial(event.getEntity().getItemStack().getType()).canBeMoved() && !NBTHandler.hasString(event.getEntity().getItemStack(), "lightFuel"))
            event.setCancelled(true);
    }

    @EventHandler
    public void fireSpawn(BlockPhysicsEvent event) {
        if (event.getChangedType() == Material.FIRE)
            trackLight(event.getBlock());
    }

    @EventHandler
    public void redstoneTorchTick(BlockPhysicsEvent event) {
        if (event.getBlock().getType() == Material.REDSTONE_WALL_TORCH || event.getBlock().getType() == Material.REDSTONE_TORCH)
            event.setCancelled(true);
    }

    @EventHandler
    public void unlitTorchDrop(ItemSpawnEvent event) {
        if (event.getEntity().getItemStack().getType() == Material.REDSTONE_TORCH) {
            event.getEntity().setItemStack(burntTorch(event.getEntity().getItemStack().getAmount()));
        }
    }
}
