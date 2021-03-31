package com.Theeef.me;

import com.Theeef.me.abilities.AbilityEvents;
import com.Theeef.me.light.LightDecay;
import com.Theeef.me.light.LightType;
import com.Theeef.me.mobs.Spawning;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Shadebound extends JavaPlugin {

    public static ConfigManager cfm;
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        loadConfig();

        protocolManager = ProtocolLibrary.getProtocolManager();

        // cancelChangeSlotPacket();

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Shadebound Enabled");
        getServer().getPluginManager().registerEvents(new Spawning(), this);
        getServer().getPluginManager().registerEvents(new LightDecay(), this);
        getServer().getPluginManager().registerEvents(new AbilityEvents(), this);

        VoidDamage.voidDamageLoop();
        // LightDecay.lightLoop();
    }

    public void cancelChangeSlotPacket() {
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                int slot = event.getPacket().getIntegers().read(1);


            }
        });
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
