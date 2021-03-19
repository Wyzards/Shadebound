package com.Theeef.me.mobs;

import org.bukkit.World;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class Spawning implements Listener {

    @EventHandler
    public void overworldSpawn(CreatureSpawnEvent event) {
        if (event.getEntity().getWorld().getEnvironment() == World.Environment.NORMAL) {
            if (event.getEntity().getCategory() == EntityCategory.UNDEAD || event.getEntityType() == EntityType.ENDERMAN || event.getEntityType() == EntityType.CREEPER || event.getLocation().getBlock().getLightLevel() <= 7)
                event.setCancelled(true);
        }
    }

}
