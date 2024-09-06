package com.dcgames.pillars.tasks.type;

import com.dcgames.pillars.Fortune;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class AblockalypseTask extends BukkitRunnable {

    private final Random random = new Random();
    private final Material[] materials = Material.values();

    public AblockalypseTask() {
        runTaskTimer(Fortune.getInstance(), 0, 20 * 15);
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            Player[] players = Bukkit.getOnlinePlayers().stream()
                    .filter(player -> !Fortune.getInstance().getPlayerManager().getPlayer(player.getName()).isSpectator())
                    .toArray(Player[]::new);
            if (players.length == 0) return;

            Player player = players[random.nextInt(players.length)];

            Location loc = player.getLocation().add(random.nextInt(5) - 2, random.nextInt(3) - 1, random.nextInt(5) - 2);

            Block block = loc.getBlock();
            if (!Fortune.getInstance().getPlayerManager().getPlayer(player.getName()).isSpectator() && block.getType() == Material.AIR) {
                block.setType(materials[random.nextInt(materials.length)]);
            }
        }
    }
}
