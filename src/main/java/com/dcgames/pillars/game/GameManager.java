package com.dcgames.pillars.game;

import com.dcgames.pillars.game.player.PlayerData;
import com.dcgames.pillars.game.player.PlayerState;
import com.dcgames.pillars.game.type.GameType;
import com.dcgames.pillars.game.type.MapType;
import com.dcgames.pillars.tasks.*;
import com.dcgames.pillars.tasks.type.AblockalypseTask;
import com.dcgames.pillars.tasks.type.FragileBlockTask;
import com.dcgames.pillars.tasks.type.LavaTask;
import com.dcgames.pillars.tasks.type.PortalsTask;
import com.dcgames.pillars.tasks.type.mode.ShuffleModeTask;
import com.dcgames.pillars.tasks.type.mode.SwapperModeTask;
import com.dcgames.pillars.util.*;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.chat.Clickable;
import com.dcgames.pillars.util.chat.Msg;
import lombok.Getter;
import com.dcgames.pillars.Fortune;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GameManager {

    @Getter
    private static final GameData data = new GameData();
    private boolean broadcasted;

    public GameManager() {
        new WorldGenTask(this).runTaskTimer(Fortune.getInstance(), 0L, 20L);
    }

    public void onStart() {
        data.setGameState(GameState.PLAYING);
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
            playerData.setPlayed(playerData.getPlayed() + 1);
            playerData.setPlayerState(PlayerState.PLAYING);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1F, 1F);
        });

        Msg.sendMessage("");
        Msg.sendMessage("&eGamemode&7: &f" + Fortune.getInstance().getVoteManager().getHighestGameTypeVote().getName());
        Msg.sendMessage("&eMap Mode&7: &f" + Fortune.getInstance().getVoteManager().getHighestMapTypeVote().getName());
        Msg.sendMessage("");

        if (data.getMapType() == MapType.RISING_LAVA) {
            new LavaTask();
        }
        if (data.getMapType() == MapType.PORTALS) {
            new PortalsTask();
        }
        if (data.getMapType() == MapType.ABLOCKALYPSE) {
            new AblockalypseTask();
        }
        if (data.getMapType() == MapType.FRAGILE_BLOCKS) {
            new FragileBlockTask();
        }
        if(data.getGameType() == GameType.SHUFFLE) {
            new ShuffleModeTask();
        }
        if(data.getGameType() == GameType.SWAPPER) {
            new SwapperModeTask();
        }

        new CheckTask();
        new GiveItemTask();
        new GameTimeTask();
        data.setRemaining(Fortune.getInstance().getPlayerManager().getAlivePlayers());
        data.setInitial(Fortune.getInstance().getPlayerManager().getAlivePlayers());
    }

    public void onStarting() {
        Bukkit.getOnlinePlayers().forEach(Util::clearPlayer);
        Util.teleportPlayers();

        data.setGameState(GameState.STARTING);
        GameType gameType = Fortune.getInstance().getVoteManager().getHighestGameTypeVote();
        MapType mapType = Fortune.getInstance().getVoteManager().getHighestMapTypeVote();
        GameManager.getData().setGameType(gameType);
        GameManager.getData().setMapType(mapType);
        new StartingTask();
    }

    public void onCheckWinners() {
        if (!data.getGameState().equals(GameState.PLAYING)) {
            return;
        }

        if (Fortune.getInstance().getPlayerManager().getAlivePlayers() == 1 && !broadcasted) {
            Fortune.getInstance().getPlayerManager().getListOfAlivePlayers().forEach(this::onSelectWinner);
        }
    }

    private void onSelectWinner(PlayerData winner) {
        broadcasted = true;

        Msg.sendMessage("&7&m" + StringUtils.repeat("-", 45));
        Msg.sendMessage(CC.PRIMARY + "Oyun Sonucu ");
        Msg.sendMessage("");

        winner.setWins(winner.getWins() + 1);
        data.setWinner(winner.getName());

        Tasks.runAsync(() -> Fortune.getInstance().getPlayerManager().saveData(winner));

        Clickable clickable = new Clickable(CC.SECONDARY + "Kazanan: " + CC.PRIMARY + winner.getName());
        Bukkit.getOnlinePlayers().forEach(clickable::sendToPlayer);

        Msg.sendMessage(CC.SECONDARY + "Öldürmeler: " + CC.PRIMARY + winner.getGameKills() +
                CC.GRAY + " - " + CC.SECONDARY + "Toplam Kazanma: " + CC.PRIMARY + winner.getWins());

        Msg.sendMessage("&7&m" + StringUtils.repeat("-", 45));
        data.setGameState(GameState.WINNER);
        new EndTask();
    }

    public List<PlayerData> getSpectators() {
        return Fortune.getInstance().getPlayerManager().getPlayerDatas().values().stream()
                .filter(PlayerData::isSpectator)
                .filter(PlayerData::isOnline)
                .collect(Collectors.toList());
    }

    public void onLoadChunks() {
        Bukkit.getScheduler().runTaskLater(Fortune.getInstance(), () -> {
            for (int x = -110; x < 110; x++) {
                for (int z = -110; z < 110; z++) {
                    Location location = new Location(Bukkit.getWorld("arena"), x, 60, z);

                    if (!location.getChunk().isLoaded()) {
                        location.getWorld().loadChunk(x, z);
                    }
                }
            }
        }, 100L);

        Bukkit.getScheduler().runTaskLater(Fortune.getInstance(), () ->
                data.setGenerated(true), 100L);
    }
}
