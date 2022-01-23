package com.github.constmine.plugin.straight;

import com.github.constmine.plugin.straight.command.CommandStraight;
import com.github.constmine.plugin.straight.event.KnockBackEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Straight extends JavaPlugin {
    /*Todo
     * 직진 커맨드 입력 받기 (st)
     * 커맨드로 입력받은 대상(Player) 에게 권한 부여 (config를 통한 권한)
     */


    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.getConfig().set("Owner", " ");
        Objects.requireNonNull(getCommand("st")).setExecutor(new CommandStraight(this));
        Bukkit.getPluginManager().registerEvents(new KnockBackEvent(this), this);
    }

    @Override
    public void onDisable() {
        this.getConfig().set("Owner", " ");
    }
}
