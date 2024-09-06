package com.dcgames.pillars.tasks;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.GameData;
import com.dcgames.pillars.game.GameManager;
import com.dcgames.pillars.game.GameState;
import com.dcgames.pillars.game.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class GiveItemTask extends BukkitRunnable {

    private final Fortune plugin = Fortune.getInstance();
    private final Set<Material> disallowed = EnumSet.of(
            Material.BEDROCK, Material.END_PORTAL_FRAME, Material.END_PORTAL,
            Material.NETHER_PORTAL, Material.WITHER_SKELETON_SKULL, Material.COMMAND_BLOCK, Material.COMMAND_BLOCK_MINECART, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.WITHER_SKELETON_WALL_SKULL,
            Material.EGG, Material.DRAGON_EGG, Material.CREEPER_SPAWN_EGG, Material.ENCHANTED_GOLDEN_APPLE,
            Material.SPAWNER, Material.LAVA, Material.LAVA_BUCKET, Material.BARRIER, Material.WATER
    );

    public GiveItemTask() {
        runTaskTimer(plugin, 0L, 80L);
    }

    @Override
    public void run() {
        if(Fortune.getInstance().getPlayerManager().getAlivePlayers() <= 1) {
            return;
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            if(!GameManager.getData().getGameState().equals(GameState.PLAYING)) {
                return;
            }

            PlayerData data = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
            if(!data.isSpectator()) {
               player.getInventory().addItem(getRandomItem());
            }
        });
    }

    private ItemStack getRandomItem() {
        List<Material> allowedMaterials = new ArrayList<>();
        for (Material material : Material.values()) {
            if (!disallowed.contains(material) && material.isItem()) {
                allowedMaterials.add(material);
            }
        }

        if (allowedMaterials.isEmpty()) {
            return new ItemStack(Material.AIR, 1);
        }

        Material material = allowedMaterials.get(ThreadLocalRandom.current().nextInt(allowedMaterials.size()));
        return new ItemStack(material, 1);
    }
}


