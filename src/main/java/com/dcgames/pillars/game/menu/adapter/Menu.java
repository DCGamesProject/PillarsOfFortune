package com.dcgames.pillars.game.menu.adapter;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import com.dcgames.pillars.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Menu {
    public Fortune plugin = Fortune.getInstance();

    @Getter
    private List<Slot> slots = new ArrayList<>();
    @Setter
    @Getter
    private boolean updateInTask = false;

    public abstract List<Slot> getSlots(Player player);

    public abstract String getName(Player player);

    public void open(Player player) {
        Menu previous = plugin.getMenuManager().getOpenedMenus().get(player.getUniqueId());
        if (previous != null) {
            previous.onClose(player);
            plugin.getMenuManager().getOpenedMenus().remove(player.getUniqueId());
        }

        plugin.getMenuManager().openedMenuTing.put(player.getUniqueId(), this);

        this.slots = this.getSlots(player);
        String title = this.getName(player);

        if (title.length() > 32) title = title.substring(0, 32);
        title = CC.translate(title);

        if (player.getOpenInventory() != null) {
            player.closeInventory();
        }

        Inventory inventory = Bukkit.createInventory(player, this.getInventorySize(this.slots), title);

        this.slots.forEach(slot -> {
            inventory.setItem(slot.getSlot(), slot.getItem(player));

            if (slot.getSlots() != null) {
                Arrays.stream(slot.getSlots()).forEach(extra -> {
                    inventory.setItem(extra, slot.getItem(player));
                });
            }
        });

        player.openInventory(inventory);
        plugin.getMenuManager().getOpenedMenus().put(player.getUniqueId(), this);
        this.onOpen(player);
    }

    public void update(Player player) {
        if (!plugin.getMenuManager().openedMenuTing.containsKey(player.getUniqueId())) return;
        this.slots = this.getSlots(player);
        String title = this.getName(player);

        if (title.length() > 32) title = title.substring(0, 32);
        title = CC.translate(title);

        boolean passed = false;
        Inventory inventory = null;
        Menu currentlyOpenedMenu = plugin.getMenuManager().getOpenedMenus().get(player.getUniqueId());
        Inventory current = player.getOpenInventory().getTopInventory();
        String title2 = player.getOpenInventory().getTopInventory().toString();

        try {
            // Attempt to access the title using reflection due to API limitations in 1.16.5
            Method getTitleMethod = current.getClass().getMethod("getTitle");
            title2 = (String) getTitleMethod.invoke(current);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (currentlyOpenedMenu != null && CC.translate(currentlyOpenedMenu.getName(player)).equals(title2)/* && current.getSize() == this.getInventorySize(this.slots)*/) {
            inventory = current;
            passed = true;
        }

        if (inventory == null) {
            inventory = Bukkit.createInventory(player, this.getInventorySize(this.slots), title);
        }

        /**
         * TemporaryInventory
         * Used to prevent item flickering because 'inventory' is live player inventory
         */
        Inventory temporaryInventory = Bukkit.createInventory(player, inventory.getSize(), title2);

        if (slots == null) return;

        this.slots.forEach(slot -> {
            temporaryInventory.setItem(slot.getSlot(), slot.getItem(player));

            if (slot.getSlots() != null) {
                Arrays.stream(slot.getSlots()).forEach(extra -> {
                    temporaryInventory.setItem(extra, slot.getItem(player));
                });
            }
        });

        plugin.getMenuManager().getOpenedMenus().remove(player.getUniqueId());
        plugin.getMenuManager().getOpenedMenus().put(player.getUniqueId(), this);

        inventory.setContents(temporaryInventory.getContents());
        if (passed) {
            player.updateInventory();
        } else {
            player.openInventory(inventory);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cOpened new inventory"));
        }

        this.onOpen(player);
    }

    private int getInventorySize(List<Slot> slots) {
        int highest = 0;
        if (!slots.isEmpty()) {
            highest = slots.stream().sorted(Comparator.comparingInt(Slot::getSlot).reversed()).map(Slot::getSlot).collect(Collectors.toList()).get(0);
        }
        for (Slot slot : slots) {
            if (slot.getSlots() != null) {
                for (int i = 0; i < slot.getSlots().length; i++) {
                    if (slot.getSlots()[i] > highest) {
                        highest = slot.getSlots()[i];
                    }
                }
            }
        }
        return (int) (Math.ceil((highest + 1) / 9D) * 9D);
    }

    public boolean hasSlot(int value) {
        return this.slots.stream()
                .filter(slot -> slot.getSlot() == value || slot.getSlots() != null
                        && Arrays.stream(slot.getSlots()).anyMatch(i -> i == value))
                .findFirst().orElse(null) != null;
    }

    public Slot getSlot(int value) {
        return this.slots.stream()
                .filter(slot -> slot.getSlot() == value || slot.getSlots() != null
                        && Arrays.stream(slot.getSlots()).anyMatch(i -> i == value))
                .findFirst().orElse(null);
    }

    public void onOpen(Player player) {

    }

    public void onClose(Player player) {
        plugin.getMenuManager().getLastOpenedMenus().remove(player.getUniqueId());
        plugin.getMenuManager().getLastOpenedMenus().put(player.getUniqueId(), this);
    }
}
