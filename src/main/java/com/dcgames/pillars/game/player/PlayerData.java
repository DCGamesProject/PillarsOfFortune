package com.dcgames.pillars.game.player;

import com.dcgames.pillars.game.type.GameType;
import com.dcgames.pillars.game.type.MapType;
import com.dcgames.pillars.util.ItemLoadUtil;
import com.dcgames.pillars.util.Util;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class PlayerData {

    private String name;
    private final UUID uuid;
    private String realName;
    private String lowerCaseName;
    private boolean died;

    private int gameKills = 0;

    private int kills = 0;
    private int deaths = 0;
    private int wins = 0;
    private int played = 0;
    private int killStreak = 0;
    private int id;
    private MapType lastMapTypeVoted;
    private GameType lastGameTypeVoted;

    private PlayerState playerState = PlayerState.LOBBY;

    private boolean loaded;

    public boolean isAlive() {
        return playerState.equals(PlayerState.PLAYING);
    }

    public boolean isSpectator() {
        return playerState.equals(PlayerState.SPECTATING);
    }

    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
