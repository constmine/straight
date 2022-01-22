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

    /*
    Owner권한을 config파일로 저장함.
    ...문제는 Owner 권한이 이미 있는 상태에서
     */
    @EventHandler
    public void onKnockBack(EntityKnockbackByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if(Objects.requireNonNull(config.getString("Owner")).equalsIgnoreCase(entity.getName())) {
                Player player = (Player) entity;
                if (player.isSneaking()) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
