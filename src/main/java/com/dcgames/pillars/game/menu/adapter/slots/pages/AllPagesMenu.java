package com.dcgames.pillars.game.menu.adapter.slots.pages;

import com.dcgames.pillars.game.menu.adapter.MultiPageMenu;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AllPagesMenu extends MultiPageMenu {
    private final MultiPageMenu menu;

    @Override
    public void onClose(Player player) {
    }

    @Override
    public String getPagesTitle(Player player) {
        return CC.translate("&7Jump to a page");
    }

    @Override
    public void onOpen(Player player) {
    }


    @Override
    public List<Slot> getSwitchableSlots(Player player) {
        List<Slot> slots = new ArrayList<>();

        for (int i = 0; i < menu.getPages(player); i++) {
            slots.add(new PageButton(i));
        }

        return slots;
    }

    @Override
    public List<Slot> getEveryMenuSlots(Player player) {
        List<Slot> slots = new ArrayList<>();
        return slots;
    }

    @AllArgsConstructor
    private class PageButton extends Slot {
        private final int page;

        @Override
        public ItemStack getItem(Player player) {
            ItemBuilder item = new ItemBuilder(Material.WHITE_WOOL);
            item.name("&a&lPage " + page);
            item.durability(3);
            item.addLoreLine("&7");
            item.addLoreLine("&aJump to page " + page + ".");
            return item.build();
        }


        @Override
        public int getSlot() {
            return 0;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            menu.changePage(player, page - menu.getPage());
        }
    }
}