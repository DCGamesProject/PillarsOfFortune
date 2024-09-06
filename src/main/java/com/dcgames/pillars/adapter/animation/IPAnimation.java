package com.dcgames.pillars.adapter.animation;

import com.dcgames.pillars.Fortune;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class IPAnimation {

    @Getter
    public static String animation;

    public static void init() {
        List<String> loadings = Arrays.asList("&7www.rahmicanmc.com","&9discord.gg/zVaeFcJfm3");
        int[] b = {0};
        runTimer(() -> {
            if (b[0] == loadings.size()) b[0] = 0;
            animation = loadings.get(b[0]++);
        }, 0L, (long) 5 * 30L);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        Fortune.getInstance().getServer().getScheduler().runTaskTimer(Fortune.getInstance(), runnable, delay, timer);
    }
}
