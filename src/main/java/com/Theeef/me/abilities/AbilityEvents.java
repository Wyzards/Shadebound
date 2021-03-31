package com.Theeef.me.abilities;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_16_R3.ItemAxe;
import net.minecraft.server.v1_16_R3.ItemShield;
import net.minecraft.server.v1_16_R3.ItemSword;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class AbilityEvents implements Listener {

    @EventHandler
    public void abilityButton(PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (item == null || item.getType() == Material.AIR) {
            // CAST ABILITIES VIA HOTBAR
        } else {

        }
    }

    public static void selectAbilityGUI(Player player, ItemStack item) {
        Set<Ability> activeAbilities = Abilities.getAvailableActiveAbilities(item);
        Inventory inventory = Bukkit.getServer().createInventory(null, activeAbilities.size() / 9 + 1, "Bind Ability");

        for (Ability ability : activeAbilities)
            inventory.addItem(abilityItem(ability));
    }

    public static ItemStack abilityItem(Ability ability) {
        ItemStack item = new ItemStack(ability.getDisplayMaterial());
        return item;
    }
}
