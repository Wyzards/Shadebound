package com.Theeef.me;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class VoidDamage {

    public static Shadebound plugin = Shadebound.getPlugin(Shadebound.class);

    public static void voidDamageLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        int light = entity.getLocation().getBlock().getLightLevel();
                        double damage = (7 - light) / 7.0 * 8;

                        if (damage > 0) {
                            if (entity instanceof Damageable) {
                                ((Damageable) entity).damage(damage);
                                entity.setLastDamageCause(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.VOID, damage));
                            } else if (entity instanceof Item) {
                                ItemStack itemstack = ((Item) entity).getItemStack();
                                ItemMeta meta = itemstack.getItemMeta();
                                org.bukkit.inventory.meta.Damageable damageable = (org.bukkit.inventory.meta.Damageable) meta;
                                int dura = itemstack.getType().getMaxDurability() - damageable.getDamage();

                                if (dura - (int) (damage * 5) <= 0)
                                    entity.remove();
                                else {
                                    damageable.setDamage(dura - (int) (damage * 5));
                                    itemstack.setItemMeta(meta);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }
}
