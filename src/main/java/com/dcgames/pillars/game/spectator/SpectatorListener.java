package com.dcgames.pillars.game.spectator;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;

public class SpectatorListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (isSpectator(player)) {
			if (event.getSlotType() == InventoryType.SlotType.OUTSIDE ||
					(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) &&
							(event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.SWAP_WITH_CURSOR)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
		if (event.getTarget() instanceof Player && isSpectator((Player) event.getTarget())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player && isSpectator((Player) event.getTarget())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onHangingPlace(HangingPlaceEvent event) {
		if (event.getEntity() instanceof ItemFrame && isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player && isSpectator((Player) event.getEntity())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && isSpectator((Player) event.getEntity())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && isSpectator((Player) event.getDamager())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onVehicleEnter(VehicleEnterEvent event) {
		if (event.getEntered() instanceof Player && isSpectator((Player) event.getEntered())) {
			event.setCancelled(true);
		}
	}

	private boolean isSpectator(Player player) {
		return Fortune.getInstance().getPlayerManager().getPlayer(player.getName()).isSpectator();
	}
}
