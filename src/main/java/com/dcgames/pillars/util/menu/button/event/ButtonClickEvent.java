package com.dcgames.pillars.util.menu.button.event;

import com.dcgames.pillars.util.menu.Menu;
import com.dcgames.pillars.util.menu.button.Button;
import com.dcgames.pillars.util.menu.click.Click;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ButtonClickEvent extends Event {
   private static final HandlerList handlerList = new HandlerList();
   private final Player player;
   private final Button button;
   private final Menu menu;
   private final Click click;

   public HandlerList getHandlers() {
      return handlerList;
   }

   public Player getPlayer() {
      return this.player;
   }

   public Button getButton() {
      return this.button;
   }

   public Menu getMenu() {
      return this.menu;
   }

   public Click getClick() {
      return this.click;
   }

   public ButtonClickEvent(Player player, Button button, Menu menu, Click click) {
      this.player = player;
      this.button = button;
      this.menu = menu;
      this.click = click;
   }

   public static HandlerList getHandlerList() {
      return handlerList;
   }
}
