package com.dcgames.pillars.game;

import com.dcgames.pillars.game.type.GameType;
import com.dcgames.pillars.game.type.MapType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameData {

    private int remaining;
    private int initial;
    private int border = 100;
    private int lavaLevel = 50;
    private boolean lavaEnded = false;

    private int startingTime = 11;
    private int lobbyTime = 15;
    private int endTime = 20;
    private int gameTime;
    private int lavaTime = 61;

    private boolean canStartCountdown;
    private boolean generated;

    private String winner;

    private String loading = "";

    private GameState gameState = GameState.LOBBY;
    private MapType mapType = MapType.NORMAL;
    private GameType gameType = GameType.NORMAL;

    public void updateGameTime() {
        ++this.gameTime;
    }

    public void updateLava() {
        ++this.lavaLevel;
    }
}
