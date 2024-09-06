package com.dcgames.pillars.util.menu.event;

import com.dcgames.pillars.util.menu.Menu;
import com.dcgames.pillars.util.menu.click.Click;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MenuClickEvent extends Event {
   private static final HandlerList handlerList = new HandlerList();
   private final Player player;
   private final Menu menu;
   private final Click click;

   public HandlerList getHandlers() {
      return handlerList;
   }

   public Player getPlayer() {
      return this.player;
   }

   public Menu getMenu() {
      return this.menu;
   }

   public Click getClick() {
      return this.click;
   }

   public MenuClickEvent(Player player, Menu menu, Click click) {
      this.player = player;
      this.menu = menu;
      this.click = click;
   }

   public static HandlerList getHandlerList() {
      return handlerList;
   }
}
