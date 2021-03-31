package com.Theeef.me.abilities;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.UUID;

public class Ability {

    private HashMap<UUID, Long> lastCast;
    private String id;
    private String name;
    private String description;
    private double cooldown;
    private ItemType itemType;
    private AbilityType abilityType;
    private Material displayMaterial;

    public Ability(String id, String name, String description, double cooldown, ItemType itemType, AbilityType abilityType, Material displayMaterial) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cooldown = cooldown;
        this.itemType = itemType;
        this.abilityType = abilityType;
        this.displayMaterial = displayMaterial;
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public AbilityType getAbilityType() {
        return this.abilityType;
    }

    public Material getDisplayMaterial() {
        return this.displayMaterial;
    }

    public enum ItemType {
        ANY, BOW, SWORD, AXE, SHIELD, MELEE_WEAPON;
    }

    public enum AbilityType {
        ACTIVE, TOGGLE, PASSIVE;
    }
}
