package com.dcgames.pillars.game.commands;

import com.dcgames.pillars.Fortune;

import java.util.HashSet;
import java.util.Set;

public class CommandManager {
   private Set<BaseCommand> commands = new HashSet();

   public CommandManager() {
      //this.commands.add(new StatsCommand());
      this.commands.add(new GameCommand());
      this.commands.add(new StatsCommand());
      this.commands.forEach((command) -> Fortune.getInstance().getServer().getCommandMap().register(Fortune.getInstance().getName(), command));
   }
}
