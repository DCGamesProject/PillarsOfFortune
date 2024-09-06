package com.dcgames.pillars.tasks.type.mode;

import com.dcgames.pillars.Fortune;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ShuffleModeTask extends BukkitRunnable {

    public ShuffleModeTask() {
        runTaskTimer(Fortune.getInstance(), 0, 20 * 30);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(!Fortune.getInstance().getPlayerManager().getPlayer(player.getName()).isSpectator()) {
                Random random = new Random();
                ItemStack[] items = new ItemStack[9];
                for (int i = 0; i < items.length; i++) {
                    items[i] = new ItemStack(Material.values()[random.nextInt(Material.values().length)]);
                }
                player.getInventory().setContents(items);
                player.sendMessage("Your hotbar has been shuffled!");
            }
        }
    }
}
