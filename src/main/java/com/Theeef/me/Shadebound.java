package com.Theeef.me;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

public class Shadebound extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Shadebound Enabled");
        getServer().getPluginManager().registerEvents(this, this);

        VoidDamage.voidDamageLoop();
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        event.getPlayer().sendMessage("Damage: " + (event.getItemDrop().getItemStack().getType().getMaxDurability() - ((Damageable) event.getItemDrop().getItemStack().getItemMeta()).getDamage()));
    }
}
