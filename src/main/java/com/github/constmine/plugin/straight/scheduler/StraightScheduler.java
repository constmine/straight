package com.github.constmine.plugin.straight.scheduler;

import com.github.constmine.plugin.straight.Straight;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class StraightScheduler implements Runnable {
    /*Todo
     * 1. 플레이어 위치 (3 X 3) 정도에 얼음 생성 (fillBlock)
     *      + 플레이어의 위치 Y를 고정 값으로 받아서 점프시에 올라가지 못하게 함
     * 2. 플레이어 주변 블록을 날리는 효과 (replaceBlockToFallingBlock)
     *      + 그냥 Vector로 날리는 것처럼 효과를 나타냄
     * 3. 플레이어를 제외한 엔티티를 날리는 효과 (EntityUp)
     */

    private final Straight plugin;
    private final Player player;
    private final int BlockY;

    /**
     * 생성자 호출에 Scheduler를 적용할 Player를 받음
     * @param player 효과를 주기위한 대상
     */
    public StraightScheduler(Player player, Plugin plugin) {
        this.player = player;
        BlockY = player.getLocation().getBlockY() - 1;
        this.plugin = (Straight) plugin;
    }

    @Override
    public void run() {
        if(!player.isOnline()) {
            Bukkit.getScheduler().cancelTasks(plugin);
            plugin.getConfig().set("Owner", " ");
        }

        Location location = player.getLocation();

        fillBlock(player.getWorld(), location);
        replaceBlockToFallingBlock(player.getWorld(), location);
        giveEffect();

        Bukkit.getScheduler().runTaskTimer(plugin, this::entityUp, 0L, 1L);
    }

    public void fillBlock(World world, Location coordinate) {
        int[] coordinates = new int[]{ coordinate.getBlockX(), BlockY, coordinate.getBlockZ() };

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Block block = world.getBlockAt(coordinates[0] + j, BlockY, coordinates[2] + i);

                if(!block.getType().equals(Material.ICE)) {
                    block.setType(Material.ICE);
                }
            }
        }
    }

    public void giveEffect() {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2, 100));
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        player.removePotionEffect(PotionEffectType.SLOW);
    }


    public void replaceBlockToFallingBlock(World world, Location coordinate) {
        int[] coordinates = new int[]{coordinate.getBlockX(), coordinate.getBlockY(), coordinate.getBlockZ()};

        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                for (int k = 0; k < 6; k++) {
                    Block block = world.getBlockAt(coordinates[0] + j, coordinate.getBlockY() + k, coordinates[2] + i);
                    BlockData blockData = block.getBlockData();

                    if(isBlockChange(block)) {
                        block.setType(Material.AIR);
                        Location location = block.getLocation();

                        fallingBlockUp(world, location, blockData, new Vector(0, 2.5, 0));
                    }
                }
            }
        }
    }

    public boolean isBlockChange(Block block) {
        return (!block.isEmpty() && !block.getType().equals(Material.AIR));
    }

    public FallingBlock fallingBlockUp(World world, Location location, BlockData blockData,  Vector vector) {
        FallingBlock fallingBlock = world.spawnFallingBlock(new Location(world, location.getX() + 0.5, location.getY(), location.getZ() + 0.5), blockData);
        fallingBlock.setDropItem(false);
        fallingBlock.setGravity(false);
        fallingBlock.setVelocity(vector);

        return fallingBlock;
    }

    public void entityUp() {
        for(Entity entity : player.getNearbyEntities(2, 4, 2)) {

            entity.setVelocity(new Vector(0, 2.5, 0));
        }
    }
}

