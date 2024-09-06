package com.dcgames.pillars.tasks;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.GameData;
import com.dcgames.pillars.game.GameManager;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimeTask extends BukkitRunnable {

    public GameTimeTask() {
        runTaskTimer(Fortune.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        GameData data = GameManager.getData();
        data.updateGameTime();
    }
}
