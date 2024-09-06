package com.dcgames.pillars.tasks.type;

import com.dcgames.pillars.Fortune;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class PortalsTask extends BukkitRunnable {

    private final World arenaWorld;
    private final int minX = -50;
    private final int maxX = 50;
    private final int minZ = -50;
    private final int maxZ = 50;
    public static Location topPortalLocation;
    public static Location bottomPortalLocation;

    public PortalsTask() {
        this.arenaWorld = Bukkit.getWorld("arena");
        runTaskTimer(Fortune.getInstance(),  0, 20 * 30);
    }

    @Override
    public void run() {
        if (arenaWorld == null) {
            return;
        }

        topPortalLocation = getRandomLocation();
        bottomPortalLocation = getRandomLocation();

        createPortal(topPortalLocation);
        createPortal(bottomPortalLocation);
    }

    private Location getRandomLocation() {
        int x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        int z = ThreadLocalRandom.current().nextInt(minZ, maxZ + 1);
        int y = ThreadLocalRandom.current().nextInt(0, 85);
        return new Location(arenaWorld, x, y, z);
    }

    private void createPortal(Location location) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                location.clone().add(x, 0, z).getBlock().setType(Material.END_PORTAL_FRAME);
            }
        }
    }
}
