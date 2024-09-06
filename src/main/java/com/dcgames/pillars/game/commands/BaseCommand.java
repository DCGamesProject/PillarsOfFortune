package com.dcgames.pillars.game.commands;

import com.dcgames.pillars.util.StringUtil;
import com.dcgames.pillars.util.Tasks;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand extends BukkitCommand {
   private boolean forPlayersOnly;
   private String rank;
   private boolean async;

   public BaseCommand(String name) {
      this(name, (List)(new ArrayList()));
   }

   public BaseCommand(String name, List<String> aliases) {
      this(name, aliases, (String)null, false);
   }

   public BaseCommand(String name, String rank) {
      this(name, new ArrayList(), rank);
   }

   public BaseCommand(String name, List<String> aliases, String permission) {
      this(name, aliases, permission, false);
   }

   public BaseCommand(String name, boolean forPlayersOnly) {
      this(name, new ArrayList(), (String)null, forPlayersOnly);
   }

   public BaseCommand(String name, List<String> aliases, boolean forPlayersOnly) {
      this(name, aliases, (String)null, forPlayersOnly);
   }

   public BaseCommand(String name, String rank, boolean forPlayersOnly) {
      this(name, new ArrayList(), rank, forPlayersOnly);
   }

   public BaseCommand(String name, List<String> aliases, String rank, boolean forPlayersOnly) {
      super(name);
      this.setAliases(aliases);
      this.rank = rank;
      this.forPlayersOnly = forPlayersOnly;
   }

   public boolean execute(CommandSender sender, String alias, String[] args) {
      if (!(sender instanceof Player) && this.forPlayersOnly) {
         sender.sendMessage(StringUtil.FOR_PLAYER_ONLY);
         return false;
      } else if (this.rank != null && !sender.hasPermission(this.rank)) {
         sender.sendMessage(StringUtil.NO_PERMISSION);
         return false;
      } else {
         if (this.async) {
            Tasks.runAsync(() -> this.execute(sender, args));
         } else {
            this.execute(sender, args);
         }

         return true;
      }
   }

   public abstract void execute(CommandSender var1, String[] var2);

   public void setAsync(boolean async) {
      this.async = async;
   }
}
