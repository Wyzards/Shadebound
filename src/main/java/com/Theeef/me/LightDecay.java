package com.Theeef.me;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.RedstoneWallTorch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.material.Redstone;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class LightDecay implements Listener {

    public enum LightType {
        TORCH(100, Sets.newHashSet(Material.TORCH, Material.WALL_TORCH)), CAMPFIRE(250, Material.CAMPFIRE);

        public int maxFuel;
        public Set<Material> material;

        private LightType(int maxFuel, Set<Material> material) {
            this.maxFuel = maxFuel;
            this.material = material;
        }

        private LightType(int maxFuel, Material material) {
            this(maxFuel, Sets.newHashSet(material));
        }

        public static LightType getType(Material type) {
            switch (type) {
                case TORCH:
                    return LightType.TORCH;
                case WALL_TORCH:
                    return LightType.TORCH;
                case CAMPFIRE:
                    return LightType.CAMPFIRE;
                default:
                    throw new NullPointerException(type.name() + " is not a light source!");
            }
        }

        public static Material extinguishesTo(Material material) {
            switch (material) {
                case TORCH:
                    return Material.REDSTONE_TORCH;
                case WALL_TORCH:
                    return Material.REDSTONE_WALL_TORCH;
                case CAMPFIRE:
                    return Material.CAMPFIRE;
                default:
                    throw new NullPointerException(material.name() + " cannot be extinguished!");
            }
        }
    }

    public static void lightLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String blockType : Shadebound.getCfm().getLights().getKeys(false))
                    for (String world : Shadebound.getCfm().getLights().getConfigurationSection(blockType).getKeys(false))
                        for (String xs : Shadebound.getCfm().getLights().getConfigurationSection(blockType + "." + world).getKeys(false))
                            for (String ys : Shadebound.getCfm().getLights().getConfigurationSection(blockType + "." + world + "." + xs).getKeys(false))
                                for (String zs : Shadebound.getCfm().getLights().getConfigurationSection(blockType + "." + world + "." + xs + "." + ys).getKeys(false)) {
                                    LightType type = LightType.valueOf(blockType);
                                    Block block = Bukkit.getWorld(world).getBlockAt(Integer.parseInt(xs), Integer.parseInt(ys), Integer.parseInt(zs));

                                    if (type.material.contains(block.getType()))
                                        decreaseLight(blockType, world, xs, ys, zs);
                                    else
                                        removeLight(type, block);
                                }
            }
        }.runTaskTimer(Shadebound.getPlugin(Shadebound.class), 0, 10 * 20);
    }

    public static void addLight(Block block) {
        LightType type = LightType.getType(block.getType());
        Shadebound.getCfm().getLights().set(type.name() + "." + block.getWorld().getName() + "." + block.getX() + "." + block.getY() + "." + block.getZ(), type.maxFuel);
        Shadebound.getCfm().saveLights();
    }

    public static void removeLight(LightType type, Block block) {
        Shadebound.getCfm().getLights().set(type.name() + "." + block.getWorld().getName() + "." + block.getX() + "." + block.getY() + "." + block.getZ(), null);
        Shadebound.getCfm().saveLights();
    }

    public static void decreaseLight(String blockType, String world, String x, String y, String z) {
        Block block = Bukkit.getWorld(world).getBlockAt(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));


        int fuel = Shadebound.getCfm().getLights().getInt(blockType + "." + world + "." + x + "." + y + "." + z);
        fuel -= (int) ((2.0 - block.getTemperature()) / 2.0 + 1) * (block.getWorld().isClearWeather() ? 1 : (block.getTemperature() >= .95 ? 1 : 10));

        if (fuel <= 0)
            extinguish(Bukkit.getWorld(world).getBlockAt(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z)));
        else {
            Shadebound.getCfm().getLights().set(blockType + "." + world + "." + x + "." + y + "." + z, fuel);
            Shadebound.getCfm().saveLights();
        }
    }

    public static void extinguish(Block block) {
        LightType type = LightType.getType(block.getType());
        Shadebound.getCfm().getLights().set(type.name() + "." + block.getWorld().getName() + "." + block.getX() + "." + block.getZ() + "." + block.getY(), null);
        Shadebound.getCfm().saveLights();

        BlockFace face = null;

        if (block.getBlockData() instanceof Directional)
            face = ((Directional) block.getBlockData()).getFacing();


        block.setType(LightType.extinguishesTo(block.getType()));

        if (face != null) {
            Directional directional = (Directional) block.getBlockData();
            directional.setFacing(face);
            block.setBlockData(directional);
        }

        if (block.getType() == Material.CAMPFIRE) {
            Campfire campfire = (Campfire) block.getBlockData();
            campfire.setLit(false);
            block.setBlockData(campfire);
        } else if (block.getType() == Material.REDSTONE_TORCH) {
            Lightable light = (Lightable) block.getBlockData();
            light.setLit(false);
            block.setBlockData(light);
        } else if (block.getType() == Material.REDSTONE_WALL_TORCH) {
            Lightable light = (Lightable) block.getBlockData();
            light.setLit(false);
            block.setBlockData(light);
        }
    }

    @EventHandler
    public void torchPlace(BlockPhysicsEvent event) {
        if (event.getChangedType() == Material.TORCH || event.getChangedType() == Material.WALL_TORCH || event.getChangedType() == Material.JACK_O_LANTERN || event.getChangedType() == Material.CAMPFIRE || event.getChangedType() == Material.FIRE) {
            addLight(event.getBlock());
            System.out.println("TYPE: " + event.getChangedType());
        }
    }

    @EventHandler
    public void redstoneTorchTick(BlockPhysicsEvent event) {
        if (event.getBlock().getType() == Material.REDSTONE_WALL_TORCH || event.getBlock().getType() == Material.REDSTONE_TORCH) {
            event.setCancelled(true);
            System.out.println("REDSTONE");
        }
    }
}
