package com.Theeef.me;

import com.Theeef.me.mobs.Spawning;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

public class Shadebound extends JavaPlugin {

    public static ConfigManager cfm;

    @Override
    public void onEnable() {
        loadConfig();

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Shadebound Enabled");
        getServer().getPluginManager().registerEvents(new Spawning(), this);
        getServer().getPluginManager().registerEvents(new LightDecay(), this);

        VoidDamage.voidDamageLoop();
        LightDecay.lightLoop();
    }

    @Override
    public void onDisable() {

    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        Shadebound.cfm = new ConfigManager();
        Shadebound.cfm.setup();
        Shadebound.cfm.getLights().options().copyDefaults(true);
        Shadebound.cfm.saveLights();
    }

    public static ConfigManager getCfm() {
        return Shadebound.cfm;
    }
}
