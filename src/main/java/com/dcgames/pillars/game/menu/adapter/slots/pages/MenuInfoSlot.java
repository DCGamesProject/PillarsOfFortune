package com.dcgames.pillars.game.menu.adapter.slots.pages;

import com.dcgames.pillars.game.menu.adapter.MultiPageMenu;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import com.dcgames.pillars.util.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class MenuInfoSlot extends Slot {
    private final MultiPageMenu menu;

    @Override
    public ItemStack getItem(Player player) {
        ItemBuilder item = new ItemBuilder(Material.BOOK);
        item.name("&7Page: &f" + menu.getPage() + "&7/&f" + menu.getPages(player));
        return item.build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        new ViewAllPagesMenu(menu).open(player);
    }

    @Override
    public int getSlot() {
        return 4;
    }
}
