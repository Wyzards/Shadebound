package com.Theeef.me.mobs;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

public class LightEater extends EntityZombie {

    public LightEater(Location location) {
        super(EntityTypes.ZOMBIE, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosition(location.getX(), location.getY(), location.getZ());

        this.setBaby(true);
        this.setHealth(20.0f);
        this.setCustomName(new ChatComponentText(ChatColor.BLACK + "Light Eater"));
        this.setCustomNameVisible(true);
    }

    public static void spawn(Location location) {
        ((CraftWorld) location.getWorld()).getHandle().addEntity(new LightEater(location));
    }
}
