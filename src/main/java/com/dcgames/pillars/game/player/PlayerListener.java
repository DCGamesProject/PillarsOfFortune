package com.dcgames.pillars.game.player;

import com.dcgames.pillars.game.GameManager;
import com.dcgames.pillars.game.GameState;
import com.dcgames.pillars.game.menu.TypeSelectorMenu;
import com.dcgames.pillars.game.spectator.SpectateMenu;
import com.dcgames.pillars.game.type.GameType;
import com.dcgames.pillars.game.type.MapType;
import com.dcgames.pillars.util.*;
import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.chat.Msg;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {

    private final Fortune plugin = Fortune.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!GameManager.getData().isGenerated()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.RED + "Lütfen haritanın oluşturulmasını bekleyin!");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().name().startsWith("RIGHT_")) {
            ItemStack item = event.getItem();
            if (item == null) {
                return;
            }
            Player player = event.getPlayer();
            PlayerData data = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());

            if (event.getItem().isSimilar(ItemLoadUtil.getFromConfig("STATS"))) {
                player.performCommand("stats");
            }

            if (event.getItem().isSimilar(ItemLoadUtil.getFromConfig("SELECTOR"))) {
                new TypeSelectorMenu().open(player);
            }

            if (event.getItem().isSimilar(ItemLoadUtil.getFromConfig("LEAVE"))) {
                BungeeUtil.sendPlayer(player, Settings.FALLBACK_SERVER);
            }

            if (data.isSpectator()) {
                if (event.getItem().isSimilar(ItemLoadUtil.getFromConfig("SPECTATE-MENU"))) {
                    if (Fortune.getInstance().getPlayerManager().getAlivePlayers() == 0) {
                        player.sendMessage(CC.translate("&cŞu anda aktif bir oyuncu yok!"));
                        return;
                    }
                    new SpectateMenu(player).open();
                }
                if (event.getItem().isSimilar(ItemLoadUtil.getFromConfig("RANDOM-TELEPORT"))) {
                    if (Fortune.getInstance().getPlayerManager().getAlivePlayers() == 0) {
                        player.sendMessage(CC.translate("&cŞu anda aktif bir oyuncu yok!"));
                        return;
                    }

                    plugin.getSpectatorManager().randomTeleport(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        this.plugin.getPlayerManager().createPlayerData(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));

        int currentPlayerCount = Bukkit.getOnlinePlayers().size();

        if (currentPlayerCount > 8) {
            event.getPlayer().kickPlayer("The server has already reached the maximum number of players.");
        }

        switch (GameManager.getData().getGameState()) {
            case WINNER:
            case PLAYING:
                plugin.getSpectatorManager().enable(player, true);
                Location location = new Location(Bukkit.getWorld("arena"), 0, Bukkit.getWorld("arena").getHighestBlockYAt(0, 0) + 15, 0);
                player.teleport(location);

                Bukkit.getScheduler().runTaskLater(Fortune.getInstance(), () -> {
                    if (Fortune.getInstance().getPlayerManager().getPlayer(player.getName()).isSpectator()) {
                        Bukkit.getOnlinePlayers().stream().filter(p -> Fortune.getInstance().getPlayerManager().getPlayer(p.getName()).isSpectator()).forEach(player::hidePlayer);
                    }
                }, 1L);
                break;
            case STARTING:
            case LOBBY:
                Util.clearPlayer(player);
                if (GameManager.getData().getGameState().equals(GameState.LOBBY)) {
                    try {
                        player.teleport(plugin.getLocationManager().getLocation("Spawn"));
                    } catch (Exception exception) {
                        player.sendMessage(CC.RED + "Spawn bulunamadı!");
                    }
                    ItemLoadUtil.giveLobbyItems(player);
                    player.updateInventory();
                }
                Msg.sendMessage(player.getDisplayName() + CC.SECONDARY + " katıldı. " + CC.GRAY + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")");
                break;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();

        if (GameManager.getData().getGameState().equals(GameState.STARTING)) {
            if ((from.getX() != to.getX()) || (from.getZ() != to.getZ())) {
                player.teleport(from);
            }
        }

        if (GameManager.getData().getGameState().equals(GameState.LOBBY)) {
            if (player.getLocation().getBlockY() < -50.0) {
                try {
                    player.teleport(plugin.getLocationManager().getLocation("Spawn"));
                } catch (Exception exception) {
                    player.sendMessage(CC.RED + "Spawn bulunamadı!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.getSpectatorManager().disable(player);

        PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
        MapType mapType = playerData.getLastMapTypeVoted();
        GameType gameType = playerData.getLastGameTypeVoted();

        if(mapType != null) {
            mapType.setVotes(mapType.getVotes() - 1);
            playerData.setLastMapTypeVoted(null);
        }

        if(gameType != null) {
            gameType.setVotes(gameType.getVotes() - 1);
            playerData.setLastGameTypeVoted(null);
        }

        if (playerData.isAlive() && GameManager.getData().getGameState().equals(GameState.PLAYING)) {

            playerData.setPlayerState(PlayerState.SPECTATING);
            plugin.getGameManager().onCheckWinners();

            Msg.sendMessage("&c" + player.getName() + CC.DARK_RED + "[" + playerData.getGameKills() + "] " + CC.GRAY + "diskalifiye edildi");

        }
        Tasks.runAsync(() -> plugin.getPlayerManager().saveData(playerData));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (Util.isState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (Util.isState()) {
            event.setCancelled(true);
            return;
        }

        if (block.getLocation().getY() > 85.0D && Fortune.getInstance().getPlayerManager().getPlayerData(event.getPlayer().getUniqueId()).isAlive()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.RED + "Mevcut blok koyma sınırına ulaştın!");
        }
    }


    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (Util.isState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if (Util.isState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (Util.isState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (Util.isState()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!event.toWeatherState()) return;

        event.setCancelled(true);
    }
}
