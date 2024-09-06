package com.dcgames.pillars.util.menu.button;

import com.dcgames.pillars.util.menu.button.event.ButtonClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Button {
   private final ItemStack item;

   public abstract void onClick(ButtonClickEvent buttonClickEvent);

   public ItemStack getItem() {
      return this.item;
   }

   public Button(ItemStack item) {
      this.item = item;
   }
}
