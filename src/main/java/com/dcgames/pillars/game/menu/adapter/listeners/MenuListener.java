package com.dcgames.pillars.game.menu.adapter.listeners;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.menu.adapter.Menu;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {

    private final Fortune plugin = Fortune.getInstance();

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Menu menu = this.plugin.getMenuManager().getOpenedMenus().get(player.getUniqueId());
        if (menu == null) {
            return;
        }
        event.setCancelled(true);
        if (event.getSlot() != event.getRawSlot()) {
            return;
        }
        if (!menu.hasSlot(event.getSlot())) {
            return;
        }
        Slot slot = menu.getSlot(event.getSlot());
        slot.onClick(player, event.getSlot(), event.getClick());
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        Menu menu = this.plugin.getMenuManager().getOpenedMenus().get(player.getUniqueId());
        if (menu == null) {
            return;
        }
        menu.onClose(player);
        this.plugin.getMenuManager().openedMenuTing.remove(player.getUniqueId());
        this.plugin.getMenuManager().getOpenedMenus().remove(player.getUniqueId());
    }
}

