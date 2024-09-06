package com.dcgames.pillars.tasks.type;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.GameData;
import com.dcgames.pillars.game.GameManager;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.chat.Msg;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class LavaTask extends BukkitRunnable {

    public LavaTask() {
        runTaskTimer(Fortune.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        GameData data = GameManager.getData();
        World world = Bukkit.getWorld("arena");
        if (world == null) {
            return;
        }

        data.setLavaTime(data.getLavaTime() - 1);

        if (data.getLavaTime() <= 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', "&eLav yükseldi şu anki katman: &6" + data.getLavaLevel())));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }

            int startX = -125;
            int startZ = -125;
            int size = 250;

            for (int x = startX; x < startX + size; x++) {
                for (int z = startZ; z < startZ + size; z++) {
                    Location location = new Location(world, x, data.getLavaLevel(), z);
                    Block block = location.getBlock();
                    if (block.getType() == Material.AIR || block.getType() == Material.WATER || block.getType() == Material.GRASS || block.getType() == Material.TALL_GRASS || block.getType() == Material.SEAGRASS || block.getType() == Material.TALL_SEAGRASS ||block.getType() == Material.CAVE_AIR) {
                        block.setType(Material.LAVA);
                    }
                }
            }

            if (data.getLavaLevel() == 85) {
                data.setLavaEnded(true);
                cancel();
            }

            if (data.getLavaLevel() < 85) {
                data.updateLava();
            }

            data.setLavaTime(20);
        }
    }
}
