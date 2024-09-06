package com.dcgames.pillars.game.menu;

import com.dcgames.pillars.game.menu.adapter.Menu;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TypeSelectorMenu extends Menu {

    @Override
    public List<Slot> getSlots(Player player) {

        List<Slot> slots = new ArrayList<>();

        slots.add(new GameSelect());
        slots.add(new MapSelect());

        for (int i = 0; i < 27; i++) {
            if (!Slot.hasSlot(slots, i)) {
                slots.add(Slot.getGlass(i));
            }
        }

        return slots;
    }

    @Override
    public String getName(Player player) {
        return CC.translate("");
    }

    @Override
    public void onOpen(Player player) {
        //InventoryTitleUpdate.sendInventoryTitle(player, CC.translate(Fortune.getInstance().getMenus().getString("STATS.TITLE").replace("<player>", player.getName())));
    }

    private static class MapSelect extends Slot {

        @Override
        public ItemStack getItem(Player player) {
            //LORE
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(CC.YELLOW + "Click to open map mode select.");
            lore.add("");

            // ITEM
            ItemBuilder item = new ItemBuilder(Material.PISTON);
            item.name(CC.PRIMARY + "Map Mode");
            item.lore(lore);

            return item.build();
        }


        @Override
        public int getSlot() {
            return 14;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            new MapSelectMenu().open(player);
        }
    }

    private static class GameSelect extends Slot {

        @Override
        public ItemStack getItem(Player player) {
            //LORE
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(CC.YELLOW + "Click to open game mode select.");
            lore.add("");

            // ITEM
            ItemBuilder item = new ItemBuilder(Material.LEVER);
            item.name(CC.PRIMARY + "Game Mode");
            item.lore(lore);

            return item.build();
        }

        @Override
        public int getSlot() {
            return 12;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            new GameSelectMenu().open(player);
        }
    }
}
