package com.Theeef.me;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Shadebound extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Shadebound Enabled");
    }

    @Override
    public void onDisable() {

    }
}
