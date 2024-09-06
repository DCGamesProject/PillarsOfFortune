package com.dcgames.pillars.database.types;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.database.DataHandler;
import com.dcgames.pillars.game.player.PlayerData;
import com.dcgames.pillars.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class FlatFile implements DataHandler {

    @Override
    public void loadData(PlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(Fortune.getInstance(), () -> {
            File file = new File(Fortune.getInstance().getDataFolder(), "/data/players/" + playerData.getUuid().toString() + ".yml");
            if(!file.exists()) return;

            Config config = new Config("/data/players/" + playerData.getUuid().toString(), Fortune.getInstance());
            playerData.setId(config.getConfig().getInt("id"));
            playerData.setKills(config.getConfig().getInt("kills"));
            playerData.setDeaths(config.getConfig().getInt("deaths"));
            playerData.setKillStreak(config.getConfig().getInt("killstreak"));
            playerData.setWins(config.getConfig().getInt("wins"));
            playerData.setPlayed(config.getConfig().getInt("played"));
            playerData.setLoaded(true);
        });
    }

    @Override
    public void saveData(PlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(Fortune.getInstance(), () -> {
            Config config = new Config("/data/players/" + playerData.getUuid().toString(), Fortune.getInstance());

            FileConfiguration document = config.getConfig();

            document.set("name", playerData.getName());
            document.set("lowerCaseName", playerData.getLowerCaseName());
            document.set("uuid", playerData.getUuid().toString());
            document.set("id", playerData.getId());
            document.set("killstreak", playerData.getKillStreak());
            document.set("kills", playerData.getKills());
            document.set("deaths", playerData.getDeaths());
            document.set("wins", playerData.getWins());
            document.set("played", playerData.getPlayed());

            config.save();
        });
    }

    @Override
    public boolean resetData(String player) {
        return false;
    }
}
