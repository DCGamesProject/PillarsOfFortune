package com.dcgames.pillars.tasks;

import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.chat.Msg;
import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.GameData;
import com.dcgames.pillars.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Comparator;

public class StartingTask extends BukkitRunnable {

    private final Fortune plugin = Fortune.getInstance();

    public StartingTask() {
        runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        GameData data = GameManager.getData();

        String loading = data.getLoading();
        data.setLoading(loading.equals("") ? "." : loading.equals(".") ? ".." : loading.equals("..") ? "..." : loading.equals("...") ? "" : "");

        data.setStartingTime(data.getStartingTime() - 1);

        if(data.getStartingTime() <= 0) {
            cancel();
            plugin.getGameManager().onStart();
            return;
        }

        if (Arrays.asList(10, 5, 4, 3, 2, 1).contains(data.getStartingTime())) {
            Bukkit.getOnlinePlayers().stream().sorted(Comparator.comparing(Player::getName)).forEach(p -> p.sendTitle(CC.translate("&aOyun başlıyor!"), CC.translate("&e" + data.getStartingTime()), 10, 70, 20));
        }

        if(Arrays.asList(15, 10, 5, 4, 3, 2, 1).contains(data.getStartingTime())) {
            Msg.sendMessage(CC.SECONDARY + "Oyun " + CC.PRIMARY + data.getStartingTime() + CC.SECONDARY + " saniye içinde başlıyor.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        }
    }
}
