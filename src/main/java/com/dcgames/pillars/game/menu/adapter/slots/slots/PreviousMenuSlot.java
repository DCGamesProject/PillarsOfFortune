package com.dcgames.pillars.game.menu.adapter.slots.slots;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.menu.adapter.Menu;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import com.dcgames.pillars.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PreviousMenuSlot extends Slot {
    private final Fortune plugin = Fortune.getInstance();

    @Override
    public ItemStack getItem(Player player) {
        ItemBuilder item = new ItemBuilder(Material.ARROW);
        Menu lastMenu = plugin.getMenuManager().getLastOpenedMenus().get(player.getUniqueId());
        if (lastMenu == null) {
            item.name("&cClose");
        } else {
            item.name("&cGo Back");
        }
        return item.build();
    }

    @Override
    public int getSlot() {
        return 39;
    }

    @Override
    public int[] getSlots() {
        return new int[]{41};
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        Menu lastMenu = plugin.getMenuManager().getLastOpenedMenus().get(player.getUniqueId());
        if (lastMenu == null) {
            player.closeInventory();
        } else {
            lastMenu.open(player);
        }
    }
}
