package com.dcgames.pillars.game.commands;

import com.dcgames.pillars.game.menu.StatsMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StatsCommand extends BaseCommand {

   public StatsCommand() {
      super("stats", Arrays.asList("istatistik"), true);
   }

   public void execute(CommandSender sender, String[] args) {
      Player player = (Player) sender;
      if (player != null) {
         new StatsMenu(player).open(player);
      }
   }
}
