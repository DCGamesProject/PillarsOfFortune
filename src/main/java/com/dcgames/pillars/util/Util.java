package com.dcgames.pillars.util;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.GameManager;
import com.dcgames.pillars.game.GameState;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.io.File;
import java.util.*;

public class Util {

   public static boolean deleteFile(File file) {
      if(file.isDirectory()) {
         for (File subfile : file.listFiles()) {
            if(!deleteFile(subfile)) {
               return false;
            }
         }
      }

      return file.delete();
   }

   public static void teleportPlayers() {
      Bukkit.getScheduler().runTaskLaterAsynchronously(Fortune.getInstance(), () -> {
         World world = Bukkit.getWorld("arena");
         if (world != null) {
            List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
            int numPlayers = players.size();

            if (numPlayers > 0) {
               numPlayers = Math.min(numPlayers, 8);

               double radius = (numPlayers * 10) / (2 * Math.PI);
               Location center = new Location(world, 0, 75, 0);
               int numLocations = numPlayers;

               List<Location> teleportLocations = new ArrayList<>();
               for (int i = 0; i < numLocations; i++) {
                  double angle = 2 * Math.PI * i / numLocations;
                  double x = center.getX() + radius * Math.cos(angle);
                  double z = center.getZ() + radius * Math.sin(angle);
                  Location location = new Location(world, x, center.getY(), z);
                  teleportLocations.add(location);
               }

               Iterator<Location> locationIterator = teleportLocations.iterator();
               for (Player player : players) {
                  if (locationIterator.hasNext()) {
                     Location teleportLocation = locationIterator.next();

                     Tasks.runTaskLater(() -> player.teleport(teleportLocation), 10L);

                     Tasks.runTaskLater(() -> {
                        for (int y = 50; y < teleportLocation.getY(); y++) {
                           Location bedrockLocation = new Location(world, teleportLocation.getX(), y, teleportLocation.getZ());
                           bedrockLocation.getBlock().setType(Material.BEDROCK);
                        }
                     }, 10L);
                  } else {
                     player.sendMessage("Not enough unique locations to teleport all players.");
                     break;
                  }
               }
            } else {
               Bukkit.getLogger().warning("No players online to teleport.");
            }
         } else {
            Bukkit.getLogger().warning("The flatworld does not exist or is not loaded.");
         }
      },20L);
   }

   public static void deleteWorld() {
      World world = Bukkit.getWorld("arena");

      if(world != null) {
         Bukkit.getServer().unloadWorld(world, false);

         deleteFile(world.getWorldFolder());
      }
   }

   public static void clearPlayer(Player player) {
      player.setHealth(20.0D);
      player.setFoodLevel(20);
      player.setSaturation(5);

      player.setMaximumNoDamageTicks(20);
      player.setFireTicks(0);
      player.setFallDistance(0.0F);
      player.setLevel(0);
      player.setExp(0.0F);
      player.setWalkSpeed(0.2F);
      player.setAllowFlight(false);

      player.getInventory().clear();
      player.getInventory().setArmorContents(null);
      player.closeInventory();
      player.setGameMode(GameMode.SURVIVAL);
      player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

      player.updateInventory();
   }

   public static boolean isState() {
      return !GameManager.getData().getGameState().equals(GameState.PLAYING);

   }

   public static boolean testPermission(CommandSender sender, String perm) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         return player.hasPermission(perm);
      } else {
         return true;
      }
   }
}
