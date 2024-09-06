package com.dcgames.pillars.tasks;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.GameManager;
import com.dcgames.pillars.game.type.GameType;
import com.dcgames.pillars.game.type.MapType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class WorldGenTask extends BukkitRunnable {

    private World world;

    private boolean isGenerating = false;

    private final GameManager gameManager;

    public WorldGenTask(GameManager gameManager) {
        this.deleteDirectory(new File("arena"));
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if(this.isGenerating) {
            return;
        }

        this.generateNewWorld();
    }

    private void generateNewWorld() {
        this.isGenerating = true;

        WorldCreator worldCreator = new WorldCreator("arena");
        worldCreator.generator(new FlatWorldGenerator());
        worldCreator.generateStructures(false);

        try {
            this.world = Bukkit.createWorld(worldCreator);
        } catch (Exception e) {
            Bukkit.getLogger().info("World NPE when trying to generate map.");
            Bukkit.getServer().unloadWorld(this.world, false);

            this.deleteDirectory(new File("arena"));

            this.isGenerating = false;
            return;
        }

        int waterCount = 0;

        Bukkit.getLogger().info("Loaded a new world.");
        boolean flag = false;
        for (int i = -100; i <= 100; ++i) {
            boolean isInvalid = false;
            for (int j = -100; j <= 100; j++) {
                boolean isCenter = i >= -50 && i <= 50 && j >= -50 && j <= 50;
                if(isCenter) {
                    Block block = this.world.getHighestBlockAt(i, j).getLocation().add(0, -1, 0).getBlock();
                    if( block.getType() == Material.WATER || block.getType() == Material.LAVA) {
                        ++waterCount;
                    }
                }

                if(waterCount >= 2000) {
                    Bukkit.getLogger().info("Invalid center, too much water/lava. (" + waterCount + ")");
                    isInvalid = true;
                    break;
                }
            }

            if(isInvalid) {
                flag = true;
                break;
            }
        }
        if(flag) {
            Bukkit.getServer().unloadWorld(this.world, false);
            this.deleteDirectory(new File("arena"));
            this.isGenerating = false;
            return;
        } else {
            Bukkit.getLogger().info("Found a good seed (" + this.world.getSeed() + ").");
            this.cancel();
        }

        File lock = new File("arena", "gen.lock");
        try {
            lock.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            return;
        }
        world.setPVP(true);
        gameManager.onLoadChunks();
        Fortune.getInstance().setWorldProperties();
        GameManager.getData().setMapType(MapType.NORMAL);
        GameManager.getData().setGameType(GameType.NORMAL);
    }

    public static class FlatWorldGenerator extends ChunkGenerator {

        @Override
        public @NotNull ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
            ChunkData chunkData = createChunkData(world);

            generateGrassCircle(world, chunkX, chunkZ, chunkData);
            addWaterfall(world, chunkX, chunkZ, chunkData);
            addTree(world, chunkX, chunkZ, chunkData);
            return chunkData;
        }
    }

    private static void generateGrassCircle(World world, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) {
        int centerX = chunkX * 16 + 8;
        int centerZ = chunkZ * 16 + 8;
        int radius = 25;
        int centerY = 49;

        for (int x = centerX - radius - 1; x <= centerX + radius + 1; x++) {
            for (int z = centerZ - radius - 1; z <= centerZ + radius + 1; z++) {
                chunkData.setBlock(x, centerY, z, Material.AIR);
            }
        }

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= radius) {
                    chunkData.setBlock(centerX + x, centerY, centerZ + z, Material.GRASS_BLOCK);
                }
            }
        }
    }

    private static void addWaterfall(World world, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) {
        int centerX = chunkX * 16 + 8;
        int centerZ = chunkZ * 16 + 8;

        int baseX = centerX;
        int baseY = 49;
        int baseZ = centerZ;
        int width = 5;
        int height = 10;

        for (int x = -width / 2; x <= width / 2; x++) {
            for (int z = -width / 2; z <= width / 2; z++) {
                for (int y = 0; y <= height; y++) {
                    chunkData.setBlock(baseX + x, baseY + y, baseZ + z, Material.STONE);
                }
            }
        }

        for (int x = -width / 2 + 1; x <= width / 2 - 1; x++) {
            for (int z = -width / 2 + 1; z <= width / 2 - 1; z++) {
                chunkData.setBlock(baseX + x, baseY + height, baseZ + z, Material.WATER);
            }
        }
    }

    private static void addTree(World world, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) {
        int baseX = chunkX * 16 + 8 + 20;
        int baseY = 49;
        int baseZ = chunkZ * 16 + 8 + 20;

        int trunkHeight = 6;

        for (int y = 0; y < trunkHeight; y++) {
            chunkData.setBlock(baseX, baseY + y, baseZ, Material.OAK_LOG);
        }

        for (int x = -2; x <= 2; x++) {
            for (int y = trunkHeight; y <= trunkHeight + 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (!(x == 0 && z == 0 && y == trunkHeight)) {
                        chunkData.setBlock(baseX + x, baseY + y, baseZ + z, Material.OAK_LEAVES);
                    }
                }
            }
        }
    }

    private boolean deleteDirectory(File path) {
        if(path.exists()) {
            File[] files = path.listFiles();
            if(files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return (path.delete());
    }
}
