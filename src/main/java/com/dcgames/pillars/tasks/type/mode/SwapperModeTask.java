package com.dcgames.pillars.tasks.type.mode;

import com.dcgames.pillars.Fortune;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SwapperModeTask extends BukkitRunnable {

    public SwapperModeTask() {
        runTaskTimer(Fortune.getInstance(), 0, 20 * 45);
    }

    @Override
    public void run() {
        List<Player> activePlayers = Bukkit.getOnlinePlayers().stream()
                .filter(player -> !Fortune.getInstance().getPlayerManager().getPlayer(player.getName()).isSpectator())
                .collect(Collectors.toList());
        Random random = new Random();

        if (activePlayers.size() > 1) {
            for (Player player : activePlayers) {
                Player randomPlayer = activePlayers.get(random.nextInt(activePlayers.size()));
                if (!player.equals(randomPlayer)) {
                    Location tempLocation = player.getLocation().clone();
                    player.teleport(randomPlayer.getLocation());
                    randomPlayer.teleport(tempLocation);
                }
            }
        }
    }
}
