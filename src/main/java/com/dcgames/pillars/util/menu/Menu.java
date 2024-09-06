package com.dcgames.pillars.util.menu;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.util.ItemBuilder;
import com.dcgames.pillars.util.Tasks;
import com.dcgames.pillars.util.menu.button.Button;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

@Getter
@Setter
public abstract class Menu {

   public static final ItemStack BACKGROUND;
   private static final Map<UUID, Menu> menus;
   private final Player viewer;
   private final String title;
   private final int rows;
   private boolean closeByPlayer;
   private boolean async;
   private final Inventory inventory;
   private final Map<Integer, Button> buttons;
   private int updateTaskId;

   public Menu(Player viewer, String title, int rows) {
      this.closeByPlayer = true;
      this.updateTaskId = -1;
      this.viewer = viewer;
      this.title = title;
      this.rows = rows;
      this.buttons = new HashMap<>();
      this.inventory = Bukkit.createInventory(null, rows * 9, title);
      if (getByUuid(viewer.getUniqueId()) != null) {
         getByUuid(viewer.getUniqueId()).close();
      }

      menus.put(viewer.getUniqueId(), this);
   }

   public Menu(Player viewer, String title, int rows, long updateInterval) {
      this(viewer, title, rows);
      this.updateTaskId = Bukkit.getScheduler().runTaskTimer(Fortune.getInstance(), this::update, 0L, updateInterval).getTaskId();
   }

   public static Menu getByUuid(UUID uuid) {
      return menus.get(uuid);
   }

   public Button getButtonBySlot(int slot) {
      return this.buttons.get(slot);
   }

   protected void add(ItemStack item) {
      int slot = this.inventory.firstEmpty();
      if (slot != -1) {
         this.inventory.addItem(item);
      }
   }

   protected void set(int slot, ItemStack item) {
      this.inventory.setItem(slot, item);
   }

   protected void add(Button button) {
      int slot = this.inventory.firstEmpty();
      if (slot != -1) {
         this.inventory.setItem(slot, button.getItem());
         this.buttons.put(slot, button);
      }
   }

   protected void set(int slot, Button button) {
      this.inventory.setItem(slot, button.getItem());
      this.buttons.put(slot, button);
   }

   protected void fill(ItemStack item) {
      IntStream.range(0, this.inventory.getSize()).filter((slot) -> this.inventory.getItem(slot) == null).forEach((slot) -> {
         this.inventory.setItem(slot, item);
      });
   }

   protected void surround(ItemStack item) {
      IntStream.range(0, this.inventory.getSize()).filter((slot) -> this.inventory.getItem(slot) == null).filter((slot) -> slot < 9 || slot > this.inventory.getSize() - 9 || isDivisible(slot, 9) || isDivisible(slot + 1, 9)).forEach((slot) -> {
         this.inventory.setItem(slot, item);
      });
   }

   public static boolean isDivisible(int dividend, int divisor) {
      return dividend % divisor == 0;
   }

   protected int getSize() {
      return this.inventory.getSize();
   }

   protected void clear() {
      this.inventory.clear();
   }

   public void open() {
      if (this.getViewer() != null && this.getViewer().isOnline()) {
         if (this.async) {
            Tasks.runAsync(this::setup);
         } else {
            this.setup();
         }

         this.viewer.openInventory(this.inventory);
      }
   }

   public void close(Player player) {
   }

   public void close() {
      if (this.updateTaskId != -1) {
         Bukkit.getScheduler().cancelTask(this.updateTaskId);
      }

      if (this.isCloseByPlayer()) {
         menus.remove(this.viewer.getUniqueId(), this);
         this.viewer.closeInventory();
      }
   }

   public void update() {
      this.inventory.clear();
      if (this.async) {
         Tasks.runAsync(() -> {
            this.setup();
            this.viewer.updateInventory();
         });
      } else {
         this.setup();
         this.viewer.updateInventory();
      }

   }

   protected abstract void setup();

   static {
      BACKGROUND = (new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)).name(" ").durability(7).build();
      menus = new HashMap<>();
   }
}
