package com.dcgames.pillars.util.menu;

import com.dcgames.pillars.util.menu.button.Button;
import com.dcgames.pillars.util.menu.button.event.ButtonClickEvent;
import com.dcgames.pillars.util.menu.click.Click;
import com.dcgames.pillars.util.menu.event.MenuClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;

public class MenuListener implements Listener {
   @EventHandler
   public void onInventoryClick(InventoryClickEvent event) {
      Player player = (Player)event.getWhoClicked();
      Menu menu = Menu.getByUuid(player.getUniqueId());
      if (event.getClickedInventory() != null) {
         if (!(event.getClickedInventory() instanceof PlayerInventory)) {
            if (menu != null) {
               event.setCancelled(true);
               Bukkit.getServer().getPluginManager().callEvent(new MenuClickEvent(player, menu, new Click(event.getClick(), event.getSlot(), event.getCurrentItem())));
            }
         }
      }
   }

   @EventHandler
   public void onInventoryClose(InventoryCloseEvent event) {
      Player player = (Player)event.getPlayer();
      Menu menu = Menu.getByUuid(player.getUniqueId());
      if (menu != null) {
         menu.close();
         menu.close(player);
      }
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      Menu menu = Menu.getByUuid(player.getUniqueId());
      if (menu != null) {
         menu.close();
      }
   }

   @EventHandler
   public void onMenuClick(MenuClickEvent event) {
      Player player = event.getPlayer();
      Menu menu = event.getMenu();
      Button button = menu.getButtonBySlot(event.getClick().getSlot());
      if (event.getClick().getItem() != null && event.getClick().getItem().hasItemMeta() && event.getClick().getItem().getItemMeta().hasDisplayName()) {
         if (button != null && button.getItem() != null && button.getItem().hasItemMeta() && button.getItem().getItemMeta().hasDisplayName()) {
            if (button.getItem().getItemMeta().getDisplayName().equals(event.getClick().getItem().getItemMeta().getDisplayName())) {
               Bukkit.getServer().getPluginManager().callEvent(new ButtonClickEvent(player, button, menu, event.getClick()));
            }
         }
      }
   }

   @EventHandler
   public void onButtonClick(ButtonClickEvent event) {
      event.getButton().onClick(event);
      event.getMenu().update();
   }
}
