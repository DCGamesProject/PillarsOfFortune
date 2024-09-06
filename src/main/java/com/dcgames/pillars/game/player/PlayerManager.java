package com.dcgames.pillars.game.player;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.database.types.FlatFile;
import com.dcgames.pillars.database.types.MongoDB;
import com.dcgames.pillars.util.config.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PlayerManager {

    @Getter
    private final Map<UUID, PlayerData> playerDatas = new ConcurrentHashMap<>();

    public void createPlayerData(Player player) {
        PlayerData data = new PlayerData(player.getUniqueId());
        data.setName(player.getName());
        data.setLowerCaseName(player.getName().toLowerCase());

        this.playerDatas.put(data.getUuid(), data);
        this.loadData(data);
    }

    private void loadData(PlayerData playerData) {
        playerData.setLoaded(true);
        if (Fortune.getInstance().getStorageType().equalsIgnoreCase("mongodb")) {
            new MongoDB().loadData(playerData);
        } else {
            new FlatFile().loadData(playerData);
        }
    }


    public void saveData(PlayerData playerData) {
        if (playerData == null || !playerData.isLoaded()) {
            return;
        }

        Config config = new Config("/data/players/" + playerData.getUuid().toString(), Fortune.getInstance());

        config.save();

        if (Fortune.getInstance().getStorageType().equalsIgnoreCase("mongodb")) {
            new MongoDB().saveData(playerData);
        } else {
            new FlatFile().saveData(playerData);
        }
    }

    public PlayerData getPlayerData(UUID uuid) {
        if (this.playerDatas.containsKey(uuid)) {
            return this.playerDatas.get(uuid);
        }

        PlayerData data = new PlayerData(uuid);
        this.loadData(data);

        return data;
    }

    public int getAlivePlayers() {
        return (int) this.playerDatas.values().stream().filter(PlayerData::isAlive).count();
    }

    public List<PlayerData> getListOfAlivePlayers() {
        return this.playerDatas.values().stream().filter(PlayerData::isAlive).collect(Collectors.toList());
    }

    public int getSpectators() {
        return (int) this.playerDatas.values().stream().filter(data -> data != null && data.isSpectator()).count();
    }

    public PlayerData getPlayer(String name) {
        UUID uuid = Bukkit.getPlayer(name) == null ? Bukkit.getOfflinePlayer(name).getUniqueId() : Bukkit.getPlayer(name).getUniqueId();
        return this.getPlayerData(uuid);
    }
}
