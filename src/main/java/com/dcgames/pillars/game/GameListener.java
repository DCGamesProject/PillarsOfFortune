package com.dcgames.pillars.game;

import com.dcgames.pillars.game.player.PlayerData;
import com.dcgames.pillars.game.player.PlayerState;
import com.dcgames.pillars.game.type.MapType;
import com.dcgames.pillars.tasks.type.PortalsTask;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.chat.Msg;
import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.util.Tasks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();

        player.setHealth(20.0);
        player.teleport(player.getLocation());

        event.setDroppedExp(0);

        PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayer(player.getName());
        playerData.setPlayerState(PlayerState.SPECTATING);
        playerData.setDeaths(playerData.getDeaths() + 1);

        String title = ChatColor.translateAlternateColorCodes('&', "&cElendin!");
        String subtitle = ChatColor.translateAlternateColorCodes('&', "&aBir dahaki sefere :(");
        player.sendTitle(title, subtitle);

        if (killer != null) {
            PlayerData killerData = Fortune.getInstance().getPlayerManager().getPlayer(killer.getName());

            killerData.setKills(killerData.getKills() + 1);
            killerData.setGameKills(killerData.getGameKills() + 1);
            if (killerData.getGameKills() > killerData.getKillStreak()) {
                killerData.setKillStreak(killerData.getGameKills());
            }
        }

        playerData.setDied(true);

        Fortune.getInstance().getGameManager().onCheckWinners();

        Tasks.runAsync(() -> Fortune.getInstance().getPlayerManager().saveData(playerData));
        Bukkit.getScheduler().runTaskLater(Fortune.getInstance(), () -> Fortune.getInstance().getSpectatorManager().enable(player, true), 1L);
    }

    @EventHandler
    public void liquidFlow(BlockFromToEvent event) {
        Block block = event.getBlock();
        if (!block.getWorld().getName().equalsIgnoreCase("arena")) {
            return;
        }

        Material fromType = block.getType();
        Material toType = event.getToBlock().getType();

        if ((fromType == Material.LAVA && toType == Material.WATER)  ||
                (fromType == Material.WATER && toType == Material.LAVA) ||
                block.isLiquid()|| GameManager.getData().getMapType() == MapType.RISING_LAVA) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayer(player.getName());

        if (playerData != null && !playerData.isSpectator() && GameManager.getData().getMapType() == MapType.PORTALS && player.getLocation().getY() <= 1) {
            player.teleport(PortalsTask.topPortalLocation);
            player.sendMessage("You fell into the bottom portal and were teleported to the top!");
        }
    }


    @EventHandler
    public void onEntityDamageByEntityBow(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Arrow) || !(((Arrow) event.getDamager()).getShooter() instanceof Player)) {
            return;
        }

        Player entity = (Player) event.getEntity();
        Player shooter = (Player) ((Arrow) event.getDamager()).getShooter();

        if (entity.getName().equals(shooter.getName())) {
            return;
        }

        double health = Math.ceil(entity.getHealth() - event.getFinalDamage()) / 2.0D;

        if (health > 0.0D) {
            shooter.sendMessage(CC.translate(entity.getDisplayName() + CC.SECONDARY + " şuanda " + CC.PRIMARY + health + "&4" + Msg.HEART + CC.SECONDARY + "."));
        }
    }
}
