package com.dcgames.pillars.database;


import com.dcgames.pillars.game.player.PlayerData;

public interface DataHandler {

    void loadData(PlayerData playerData);

    void saveData(PlayerData playerData);

    boolean resetData(String player);
}
