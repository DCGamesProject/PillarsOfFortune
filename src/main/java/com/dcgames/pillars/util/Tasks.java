package com.dcgames.pillars.util;

import com.dcgames.pillars.Fortune;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.Callable;

public class Tasks {

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(Fortune.getInstance(), runnable);
    }

    public static void runLater(Runnable runnable, long later) {
        Bukkit.getScheduler().runTaskLater(Fortune.getInstance(), runnable, later);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Fortune.getInstance(), runnable);
    }

    public static void runLaterAsync(Runnable runnable, long later) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Fortune.getInstance(), runnable, later);
    }

    public static BukkitTask runTaskLater(Runnable run, long delay) {
        return Bukkit.getServer().getScheduler().runTaskLater(Fortune.getInstance(), run, delay);
    }

    public static BukkitTask runTaskTimer(Runnable run, long start, long repeat) {
        return Bukkit.getServer().getScheduler().runTaskTimer(Fortune.getInstance(), run, start, repeat);
    }

    public static BukkitTask runTaskTimer(Runnable run, long repeat) {
        return Bukkit.getServer().getScheduler().runTaskTimer(Fortune.getInstance(), run, 0, repeat);
    }

    public static BukkitTask runTaskTimerAsync(Runnable run, long start, long repeat) {
        return Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Fortune.getInstance(), run, start, repeat);
    }

    public static BukkitTask runTaskTimerAsync(Runnable run, long repeat) {
        return Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Fortune.getInstance(), run, 0, repeat);
    }

    public static int scheduleTask(Runnable run, long delay) {
        return Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Fortune.getInstance(), run, delay);
    }

    public static BukkitTask runTask(Runnable run) {
        if (!Fortune.getInstance().isEnabled()) {
            return null;
        }
        return Bukkit.getServer().getScheduler().runTask(Fortune.getInstance(), run);
    }

    public static <T> T runTaskSync(Callable<T> run) throws Exception {
        return Bukkit.getScheduler().callSyncMethod(Fortune.getInstance(), run).get();
    }

    public static int runTaskNextTick(Runnable run) {
        if (!Fortune.getInstance().isEnabled()) {
            run.run();
            return 0;
        }
        return scheduleTask(run, 1);
    }

    public static void runTaskAsync(Runnable run) {
        if (!Fortune.getInstance().isEnabled()) {
            run.run();
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Fortune.getInstance(), run);
    }

    public static void runTaskLaterAsync(Runnable run, long delay) {
        if (!Fortune.getInstance().isEnabled()) {
            run.run();
            return;
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(Fortune.getInstance(), run, delay);
    }

    public static void catchNonAsyncThread() {
        if (Bukkit.getServer().isPrimaryThread()) {
            throw new IllegalStateException("Illegal call on main thread");
        }
    }

}

