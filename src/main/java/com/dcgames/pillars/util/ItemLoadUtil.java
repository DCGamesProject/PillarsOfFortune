package com.dcgames.pillars.util;

import com.dcgames.pillars.Fortune;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemLoadUtil {

    public static ItemStack getFromConfig(String name) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(Fortune.getInstance().getItems().getString(name.toUpperCase() + ".MATERIAL")));
        if (Fortune.getInstance().getItems().getInt(name.toUpperCase() + ".DATA") != 0) {
            int data = Fortune.getInstance().getItems().getInt(name.toUpperCase() + ".DATA");
            itemBuilder.data((short) data);
        }
        itemBuilder.name(Fortune.getInstance().getItems().getString(name.toUpperCase() + ".NAME"));
        if (Fortune.getInstance().getItems().getStringList(name.toUpperCase() + ".LORE").size() > 0)
            itemBuilder.lore(Fortune.getInstance().getItems().getStringList(name.toUpperCase() + ".LORE"));
        return itemBuilder.build();
    }

    public static int getSlotFromConfig(String name) {
        return Fortune.getInstance().getItems().getInt(name.toUpperCase() + ".SLOT");
    }

    public static void giveLobbyItems(Player player) {
        player.getInventory().setItem(getSlotFromConfig("STATS"), getFromConfig("STATS"));
        player.getInventory().setItem(getSlotFromConfig("SELECTOR"), getFromConfig("SELECTOR"));
        if (Settings.BUNGEECORD) {
            player.getInventory().setItem(getSlotFromConfig("LEAVE"), getFromConfig("LEAVE"));
        }
    }

    public static void giveSpectatorItems(Player player) {
        player.getInventory().setItem(getSlotFromConfig("SPECTATE-MENU"), getFromConfig("SPECTATE-MENU"));
        player.getInventory().setItem(getSlotFromConfig("RANDOM-TELEPORT"), getFromConfig("RANDOM-TELEPORT"));
        player.getInventory().setItem(getSlotFromConfig("BETTER-VIEW"), getFromConfig("BETTER-VIEW"));
        player.updateInventory();
    }
}
