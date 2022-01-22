package com.github.constmine.plugin.straight.command;

import com.github.constmine.plugin.straight.Straight;
import com.github.constmine.plugin.straight.scheduler.StraightScheduler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandStraight implements TabExecutor {
    /*Todo
     * Sender를 플레이어(Owner)로 지정
     * Owner에게 Scheduler지정 (아래 블록 생성, 주변블록 날리기, Entity 날리기)
     */
    private final Straight plugin;

    public CommandStraight(Plugin plugin) {
        this.plugin = (Straight) plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        plugin.getConfig().set("Owner", player.getName());
        Bukkit.getScheduler().runTaskTimer(plugin, new StraightScheduler(player, plugin), 0L, 1L);

        if(args.length > 0) {
            if (args[0].equalsIgnoreCase("stop")) {
                Bukkit.getScheduler().cancelTasks(plugin);
                plugin.getConfig().set("Owner", " ");
            }
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("stop");
            return list;
        }
        return null;
    }
}
