package com.Theeef.me;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class VoidDamage {

    public static Shadebound plugin = Shadebound.getPlugin(Shadebound.class);

    public static void voidDamageLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        voidDamage(entity);
                        blindnessAtNight(entity);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    public static void blindnessAtNight(Entity entity) {
        if (entity instanceof LivingEntity && entity.getLocation().getBlock().getLightFromBlocks() == 0 && entity.getLocation().getBlock().getLightLevel() <= 4 && (!(entity instanceof Player) || ((Player) entity).getGameMode() == GameMode.SURVIVAL))
            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 255, false, false, false));
    }

    public static void voidDamage(Entity entity) {
        int light = entity.getLocation().getBlock().getLightLevel();

        if (light > 7 || entity.isInvulnerable() || (entity instanceof Player && ((Player) entity).getGameMode() != GameMode.SURVIVAL))
            return;

        double damage = (8 - light) / 8.0 * 4;

        if (damage > 0) {
            if (entity instanceof Damageable) {
                ((Damageable) entity).damage(damage);
                entity.setLastDamageCause(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.VOID, damage));

                if (entity instanceof Player) {
                    VoidDamage.voidDamageSound(entity.getLocation());
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 255, false, false, false));
                }

            } else if (entity instanceof Item) {
                ItemStack itemstack = ((Item) entity).getItemStack();
                ItemMeta meta = itemstack.getItemMeta();
                org.bukkit.inventory.meta.Damageable damageable = (org.bukkit.inventory.meta.Damageable) meta;

                if (damageable.getDamage() + ((int) (damage * 5)) > itemstack.getType().getMaxDurability())
                    entity.remove();
                else {
                    damageable.setDamage(damageable.getDamage() + ((int) (damage * 5)));
                    itemstack.setItemMeta(meta);
                }
            }
        }
    }

    public static void voidDamageSound(Location location) {
        float volume = 0.75f;

        location.getWorld().playSound(location, Sound.ENTITY_GUARDIAN_AMBIENT, volume + 0.25f, 0.0f);
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EAT, volume - 0.25f, 0.0f);
        location.getWorld().playSound(location, Sound.ENTITY_ZOGLIN_HURT, volume, 0.0f);

        if (Math.random() * 100 > 90)
            location.getWorld().playSound(location, Sound.ENTITY_HOGLIN_DEATH, volume, 0.0f);

        if (Math.random() * 100 > 95)
            location.getWorld().playSound(location, Sound.ENTITY_ENDERMAN_STARE, 1.0f, 0.0f);

        new BukkitRunnable() {
            public void run() {
                location.getWorld().playSound(location, Sound.PARTICLE_SOUL_ESCAPE, 1.0f, 0.0f);
            }
        }.runTaskLater(plugin, 10);
    }
}
