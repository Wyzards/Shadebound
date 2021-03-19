package com.Theeef.me;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private Shadebound plugin = Shadebound.getPlugin(Shadebound.class);

    // Files & File Configs Here
    public FileConfiguration lightConfig;
    public File lightFile;
    // --------------------------

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        lightFile = new File(plugin.getDataFolder(), "lights.yml");

        if (!lightFile.exists()) {
            try {
                lightFile.createNewFile();
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The lights.yml file has been created");
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + "Could not create the players.yml file");
            }
        }

        lightConfig = YamlConfiguration.loadConfiguration(lightFile);
    }

    public FileConfiguration getLights() {
        return lightConfig;
    }

    public void saveLights() {
        try {
            lightConfig.save(lightFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the players.yml file");
        }
    }

    public void reloadLights() {
        lightConfig = YamlConfiguration.loadConfiguration(lightFile);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "The players.yml file has been reloaded");
    }

}
