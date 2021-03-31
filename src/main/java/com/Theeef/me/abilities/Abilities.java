package com.Theeef.me.abilities;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_16_R3.ItemAxe;
import net.minecraft.server.v1_16_R3.ItemSword;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class Abilities {

    public static Ability ROPED_ARROW = new Ability("ROPED_ARROW", "Roped Arrow", "Shoot an arrow that pulls you towards it", 5.0, Ability.ItemType.BOW, Ability.AbilityType.ACTIVE, Material.LEAD);
    public static Ability BONE_EXPLOSION = new Ability("BONE_EXPLOSION", "Bone Explosion", "Knock back all entities around you.", 10.0, Ability.ItemType.ANY, Ability.AbilityType.ACTIVE, Material.BONE);
    public static Ability DOUBLE_JUMP = new Ability("DOUBLE_JUMP", "Double Jump", "Jump extra far", 0, Ability.ItemType.ANY, Ability.AbilityType.PASSIVE, Material.FEATHER);

    public static Set<Ability> getAvailableActiveAbilities(ItemStack item) {
        Set<Ability> abilities = new HashSet<Ability>();

        for (Ability ability : getAvailableAbilities(item))
            if (ability.getAbilityType() == Ability.AbilityType.ACTIVE)
                abilities.add(ability);

        return abilities;
    }

    public static Set<Ability> getAvailableAbilities(ItemStack item) {
        return getAbilitiesByType(getItemTypes(item));
    }

    public static Set<Ability> getValues() {
        return Sets.newHashSet(Abilities.ROPED_ARROW, Abilities.BONE_EXPLOSION, Abilities.DOUBLE_JUMP);
    }

    public static Set<Ability.ItemType> getItemTypes(ItemStack item) {
        Set<Ability.ItemType> types = Sets.newHashSet(Ability.ItemType.ANY);

        if (item != null && item.getType() != Material.AIR) {
            if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemSword)
                types.add(Ability.ItemType.SWORD);
            else if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemAxe)
                types.add(Ability.ItemType.AXE);
            else if (item.getType() == Material.SHIELD)
                types.add(Ability.ItemType.SHIELD);
            else if (item.getType() == Material.BOW)
                if (types.contains(Ability.ItemType.SWORD) || types.contains(Ability.ItemType.AXE))
                    types.add(Ability.ItemType.MELEE_WEAPON);
        }

        return types;
    }

    public static Set<Ability> getAbilitiesByType(Set<Ability.ItemType> types) {
        Set<Ability> set = new HashSet<Ability>();

        for (Ability ability : Abilities.getValues())
            if (types.contains(ability.getItemType()))
                set.add(ability);

        return set;
    }

}
