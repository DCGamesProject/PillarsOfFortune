package com.dcgames.pillars.database.types;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.database.DataHandler;
import com.dcgames.pillars.game.player.PlayerData;
import com.dcgames.pillars.util.chat.CC;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.UUID;

@Getter
public class MongoDB implements DataHandler {

    private MongoClient client;

    private MongoDatabase mongoDatabase;

    private final String host = Fortune.getInstance().getCoreConfig().getString("MONGO.HOST");
    private final int port = Fortune.getInstance().getCoreConfig().getInt("MONGO.PORT");
    private final String database = Fortune.getInstance().getCoreConfig().getString("MONGO.DATABASE");
    private final boolean authentication = Fortune.getInstance().getCoreConfig().getBoolean("MONGO.AUTH.ENABLED");

    private final String user = Fortune.getInstance().getCoreConfig().getString("MONGO.AUTH.USERNAME");
    private final String password = Fortune.getInstance().getCoreConfig().getString("MONGO.AUTH.PASSWORD");
    private final String authDatabase = Fortune.getInstance().getCoreConfig().getString("MONGO.AUTH.AUTH-DATABASE");

    private boolean connected;

    private MongoCollection<Document> playersCollection;

    public void connect() {
        try {
            Fortune.getInstance().getLogger().info("Connecting to MongoDB...");
            if (authentication) {
                MongoCredential mongoCredential = MongoCredential.createCredential(this.user, this.authDatabase, this.password.toCharArray());
                this.client = new MongoClient(new ServerAddress(this.host, this.port), Collections.singletonList(mongoCredential));
                this.connected = true;
                Bukkit.getConsoleSender().sendMessage(CC.translate("&aSuccessfully connected to MongoDB."));
            } else {
                this.client = new MongoClient(new ServerAddress(this.host, this.port));
                this.connected = true;
                Bukkit.getConsoleSender().sendMessage(CC.translate("&aSuccessfully connected to MongoDB."));
            }
            this.mongoDatabase = this.client.getDatabase(this.database);
            this.playersCollection = this.mongoDatabase.getCollection("fortune-data");
        } catch (Exception e) {
            this.connected = false;
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cDisabling because an error occurred while trying to connect to &aMongoDB."));
            Bukkit.getPluginManager().disablePlugins();
            Bukkit.shutdown();
        }
    }

    public void disconnect() {
        if (this.client != null) {
            Fortune.getInstance().getLogger().info("[MongoDB] Disconnecting...");
            this.client.close();
            this.connected = false;
            Fortune.getInstance().getLogger().info("[MongoDB] Successfully disconnected.");
        }
    }

    public Document getPlayer(UUID uuid) {
        return this.playersCollection.find(Filters.eq("uuid", uuid.toString())).first();
    }

    @Override
    public void loadData(PlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(Fortune.getInstance(), () -> {
            Document document = getPlayersCollection().find(Filters.eq("uuid", playerData.getUuid().toString())).first();
            if (document != null) {
                if (playerData.getName() != null && document.get("name") != null) {
                    if (!playerData.getName().equals(document.getString("name"))) {
                        this.playersCollection.updateOne(
                                Filters.eq("uuid", playerData.getUuid().toString()),
                                Updates.set("name", playerData.getName()));
                        this.playersCollection.updateOne(
                                Filters.eq("uuid", playerData.getUuid().toString()),
                                Updates.set("lowerCaseName", playerData.getLowerCaseName()));
                    }
                }
                playerData.setId(document.getInteger("id"));
                playerData.setKills(document.getInteger("kills"));
                playerData.setDeaths(document.getInteger("deaths"));
                playerData.setKillStreak(document.getInteger("killstreak"));
                playerData.setWins(document.getInteger("wins"));
                playerData.setPlayed(document.getInteger("played"));
                playerData.setLoaded(true);
            }
        });

    }

    @Override
    public void saveData(PlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(Fortune.getInstance(), () -> {
            Document document = new Document();
            
            document.put("name", playerData.getName());
            document.put("lowerCaseName", playerData.getLowerCaseName());
            document.put("uuid", playerData.getUuid().toString());
            document.put("id", playerData.getId());
            document.put("killstreak", playerData.getKillStreak());
            document.put("kills", playerData.getKills());
            document.put("deaths", playerData.getDeaths());
            document.put("wins", playerData.getWins());
            document.put("played", playerData.getPlayed());

            getPlayersCollection().replaceOne(Filters.eq("uuid", playerData.getUuid().toString()), document, new ReplaceOptions().upsert(true));
        });
    }

    @Override
    public boolean resetData(String player) {
        Document document = this.playersCollection.find(Filters.eq("lowerCaseName", player.toLowerCase())).first();

        if (document == null) {
            document = this.playersCollection.find(Filters.eq("uuid", player)).first();
        }

        if (document == null) {
            return false;
        }

        this.playersCollection.deleteOne(document);
        return true;
    }
}
