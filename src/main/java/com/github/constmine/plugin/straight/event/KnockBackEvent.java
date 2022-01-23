package com.github.constmine.plugin.straight.event;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class KnockBackEvent implements Listener {
    private final FileConfiguration config;

    public KnockBackEvent(Plugin plugin) {
        config = plugin.getConfig();
    }

    @EventHandler
    public void onKnockBack(EntityKnockbackByEntityEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;

            if(isOwner(player)) {

                if (player.isSneaking()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public boolean isOwner(Player player) {
        return (Objects.requireNonNull(config.getString("Owner")).equalsIgnoreCase(player.getName()));
    }

}
