package com.dcgames.pillars.util.menu.click;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class Click {
   private final ClickType type;
   private final int slot;
   private final ItemStack item;

   public ClickType getType() {
      return this.type;
   }

   public int getSlot() {
      return this.slot;
   }

   public ItemStack getItem() {
      return this.item;
   }

   public Click(ClickType type, int slot, ItemStack item) {
      this.type = type;
      this.slot = slot;
      this.item = item;
   }
}
