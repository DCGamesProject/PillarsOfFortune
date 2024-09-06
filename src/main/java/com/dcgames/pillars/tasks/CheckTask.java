package com.dcgames.pillars.tasks;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.GameListener;
import com.dcgames.pillars.game.GameManager;
import com.dcgames.pillars.game.GameState;
import com.dcgames.pillars.game.player.PlayerData;
import com.dcgames.pillars.game.player.PlayerListener;
import com.dcgames.pillars.game.player.PlayerState;
import com.dcgames.pillars.util.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class CheckTask extends BukkitRunnable {

    private final Fortune plugin = Fortune.getInstance();

    public CheckTask() {
        runTaskTimer(plugin, 0L, 10L);
    }

    @Override
    public void run() {
        if (!GameManager.getData().getGameState().equals(GameState.PLAYING)) {
            return;
        }

        if (Fortune.getInstance().getPlayerManager().getAlivePlayers() <= 1) {
            new EndTask();
            return;
        }

        if (GameManager.getData().getGameTime() == 300) {
            new EndTask();
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
            if (!data.isSpectator() && player.getLocation().getBlockY() == 51) {
                player.setHealth(20.0);
                player.teleport(player.getLocation());

                PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayer(player.getName());
                playerData.setPlayerState(PlayerState.SPECTATING);
                playerData.setDeaths(playerData.getDeaths() + 1);

                String title = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull("&cElendin!"));
                String subtitle = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull("&aBir dahaki sefere :("));
                player.sendTitle(title, subtitle);
                Fortune.getInstance().getGameManager().onCheckWinners();

                playerData.setDied(true);

                Tasks.runAsync(() -> Fortune.getInstance().getPlayerManager().saveData(playerData));
                Bukkit.getScheduler().runTaskLater(Fortune.getInstance(), () -> Fortune.getInstance().getSpectatorManager().enable(player, true), 1L);
            }
        }
    }
}


