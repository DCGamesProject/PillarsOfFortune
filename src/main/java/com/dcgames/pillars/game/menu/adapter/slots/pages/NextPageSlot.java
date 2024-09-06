package com.dcgames.pillars.game.menu.adapter.slots.pages;

import com.dcgames.pillars.game.menu.adapter.MultiPageMenu;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class NextPageSlot extends Slot {
    private final MultiPageMenu multiPageMenu;

    @Override
    public ItemStack getItem(Player player) {
        ItemBuilder item = new ItemBuilder(Material.ARROW);
        item.name("&7Next Page");
        return item.build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        if (this.multiPageMenu.getPage() < this.multiPageMenu.getPages(player)) {
            this.multiPageMenu.changePage(player, 1);
        } else {
            player.sendMessage(CC.translate("&cYou're on the last page of the menu!"));
        }
    }

    @Override
    public int getSlot() {
        return 5;
    }
}
