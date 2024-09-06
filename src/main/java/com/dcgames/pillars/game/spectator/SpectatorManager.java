package com.dcgames.pillars.game.spectator;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.player.PlayerData;
import com.dcgames.pillars.game.player.PlayerState;
import com.dcgames.pillars.util.ItemLoadUtil;
import com.dcgames.pillars.util.Tasks;
import com.dcgames.pillars.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SpectatorManager {

    private final Fortune plugin;

    public SpectatorManager() {
        this.plugin = Fortune.getInstance();
    }

    public void enable(Player player, boolean tp) {
        player.setAllowFlight(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.CREATIVE);
        player.setHealth(20.0D);
        ItemLoadUtil.giveSpectatorItems(player);

        if (tp && Bukkit.getWorld("arena") != null) {
            Location spectatorSpawn = new Location(Bukkit.getWorld("arena"), 0, 100, 0);
            player.teleport(spectatorSpawn);
        }

        Bukkit.getOnlinePlayers().forEach(online -> online.hidePlayer(player));

        PlayerData data = plugin.getPlayerManager().getPlayerData(player.getUniqueId());

        player.sendMessage(CC.YELLOW + "You are now a spectator: " + CC.RED + "You were eliminated!");
        player.setFlySpeed(0.2F);
        data.setPlayerState(PlayerState.SPECTATING);

        if (plugin.getPlayerManager().getAlivePlayers() <= 4) {
            plugin.getGameManager().onCheckWinners();
        }
    }

    public void disable(Player player) {
        PlayerData data = plugin.getPlayerManager().getPlayerData(player.getUniqueId());
        if (player.getGameMode() == GameMode.CREATIVE) {
            player.setGameMode(GameMode.SURVIVAL);
        }

        if (data.isSpectator()) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setGameMode(GameMode.SURVIVAL);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.setFlySpeed(0.1F);
            data.setPlayerState(PlayerState.LOBBY);
        }
    }

    public void randomTeleport(Player player) {
        List<UUID> alivePlayers = plugin.getPlayerManager().getListOfAlivePlayers().stream()
                .map(PlayerData::getUuid)
                .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                .collect(Collectors.toList());

        if (alivePlayers.isEmpty()) {
            player.sendMessage(CC.RED + "No players are currently alive.");
            return;
        }

        Player target = Bukkit.getPlayer(alivePlayers.get(ThreadLocalRandom.current().nextInt(alivePlayers.size())));
        if (target != null) {
            player.teleport(target);
            player.sendMessage(CC.SECONDARY + "Teleported to " + CC.YELLOW + target.getDisplayName() + CC.SECONDARY + ".");
        } else {
            randomTeleport(player);
        }
    }
}
