package com.dcgames.pillars;

import com.dcgames.pillars.adapter.Assemble;
import com.dcgames.pillars.adapter.AssembleStyle;
import com.dcgames.pillars.adapter.adapter.ScoreboardAdapter;
import com.dcgames.pillars.adapter.animation.IPAnimation;
import com.dcgames.pillars.database.types.MongoDB;
import com.dcgames.pillars.game.GameListener;
import com.dcgames.pillars.game.GameManager;

import com.dcgames.pillars.managers.VoteManager;
import com.dcgames.pillars.game.commands.CommandManager;
import com.dcgames.pillars.game.menu.adapter.MenuManager;
import com.dcgames.pillars.game.player.PlayerListener;
import com.dcgames.pillars.game.player.PlayerManager;
import com.dcgames.pillars.game.spectator.SpectatorListener;
import com.dcgames.pillars.game.spectator.SpectatorManager;
import com.dcgames.pillars.managers.LocationManager;
import com.dcgames.pillars.tasks.LobbyTask;
import com.dcgames.pillars.util.BungeeUtil;
import com.dcgames.pillars.util.chat.CC;
import java.util.Arrays;
import java.util.Scanner;

import com.dcgames.pillars.util.Util;
import com.dcgames.pillars.util.config.ConfigFile;
import com.dcgames.pillars.util.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;
import org.bukkit.entity.Entity;


@Getter
public class Fortune extends JavaPlugin {

    private GameManager gameManager;
    private PlayerManager playerManager;
    private LocationManager locationManager;
    private CommandManager commandManager;
    private SpectatorManager spectatorManager;
    private MenuManager menuManager;
    private VoteManager voteManager;
    private MongoDB mongoDB;
    private ConfigFile coreConfig,items,menus;
    private String storageType;
    private final long currentTimeMillis = System.currentTimeMillis();

    /**
     * Gets the instance of the plugin.
     *
     * @return the plugin's instance
     */
    public static Fortune getInstance() {
        return JavaPlugin.getPlugin(Fortune.class);
    }

    @Override
    public void onEnable() {
        this.storageType = getConfig().getString("TYPE");
        IPAnimation.init();
        //Loader
        this.loadManagers();
        this.loadListeners();
        saveDefaultConfig();
        this.loadConfigs();
        this.loadPlugin(currentTimeMillis);
        //Config
        //Task
        new LobbyTask();
        //Register
        if(getStorageType().equalsIgnoreCase("mongodb")) {
            this.mongoDB = new MongoDB();
            this.mongoDB.connect();
        }
        BungeeUtil.registerBungeecord();
        new Assemble(this, new ScoreboardAdapter()).setAssembleStyle(AssembleStyle.MODERN);
    }

    @Override
    public void onDisable() {
        if(getStorageType().equalsIgnoreCase("mongodb")) {
            this.mongoDB.disconnect();
        }
        org.bukkit.World arena = Bukkit.getWorld("arena");
        Util.deleteWorld();
    }

    private void loadConfigs() {
        this.coreConfig = new ConfigFile(this,"config.yml");
        //this.scoreboard = new ConfigFile(this, "scoreboard.yml");
        //this.messages = new ConfigFile(this, "messages.yml");
        this.items = new ConfigFile(this, "items.yml");
        this.menus = new ConfigFile(this, "menus.yml");
    }

    private void loadPlugin(double start) {
        log("&7---------------------------------------------------------");
        log("&6Pillars of The Fortune");
        log("&fYapımcı: &aVenslore");
        log("&fVersiyon: &a" + getDescription().getVersion());
        log("&fDestek: &ahttps://dsc.gg/dcgamesdev");
        log("&fEklentinin yüklenmesi &a" + (System.currentTimeMillis() - start) + "ms &fsürdü.");
        log("&7---------------------------------------------------------");
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(message));
    }

    private void loadManagers() {
        this.commandManager = new CommandManager();
        this.gameManager = new GameManager();
        this.locationManager = new LocationManager();
        this.spectatorManager = new SpectatorManager();
        this.playerManager = new PlayerManager();
        this.menuManager = new MenuManager();
        this.voteManager = new VoteManager();
    }


    private void loadListeners() {
        Arrays.asList(
                new PlayerListener(),
                new GameListener(),
                new MenuListener(),
                new com.dcgames.pillars.game.menu.adapter.listeners.MenuListener(),
                new SpectatorListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public void setWorldProperties() {
        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(Entity::remove));

        org.bukkit.World arena = Bukkit.getWorld("arena");
        arena.setGameRuleValue("doMobSpawning", "true");
        arena.setGameRuleValue("doDaylightCycle", "false");
        arena.setGameRuleValue("doFireTick", "false");
        arena.setGameRuleValue("difficulty", "0");
        arena.setTime(0);
    }
}
