package com.dcgames.pillars.tasks.type;

import com.dcgames.pillars.Fortune;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FragileBlockTask extends BukkitRunnable {

    public FragileBlockTask() {
        runTaskTimer(Fortune.getInstance(),  0, 20 * 10);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Fortune.getInstance().getPlayerManager().getPlayer(player.getName()).isSpectator()) {
                Location loc = player.getLocation().subtract(0, 1, 0);
                if (loc.getBlock().getType() != Material.AIR) {
                    loc.getBlock().setType(Material.COBBLESTONE);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            loc.getBlock().setType(Material.AIR);
                        }
                    }.runTaskLater(Fortune.getInstance(), 20 * 5);
                }
            }
        }
    }
}