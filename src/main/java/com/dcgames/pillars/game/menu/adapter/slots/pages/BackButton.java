package com.dcgames.pillars.game.menu.adapter.slots.pages;

import com.dcgames.pillars.game.menu.adapter.Menu;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@AllArgsConstructor
public class BackButton extends Slot {

    private final Menu back;
    private final int slot;

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        if (back != null) {
            Slot.playNeutral(player);
            this.back.open(player);
        }
    }

    @Override
    public ItemStack getItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Go Back");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public int getSlot() {
        return slot;
    }
}
