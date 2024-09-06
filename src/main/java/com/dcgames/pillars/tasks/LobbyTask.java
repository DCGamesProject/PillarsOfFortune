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

public class LobbyTask extends BukkitRunnable {

    private final Fortune plugin = Fortune.getInstance();

    public LobbyTask() {
        runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        GameData data = GameManager.getData();

        String loading = data.getLoading();
        data.setLoading(loading.equals("") ? "." : loading.equals(".") ? ".." : loading.equals("..") ? "..." : loading.equals("...") ? "" : "");

        if(data.isCanStartCountdown()) {
            data.setLobbyTime(data.getLobbyTime() - 1);
        }

        if (Bukkit.getOnlinePlayers().size() >= 2) {
            data.setCanStartCountdown(true);
        }

        if (Bukkit.getOnlinePlayers().size() < 2) {
            data.setCanStartCountdown(false);
        }

        if(data.getLobbyTime() <= 0) {
            plugin.getGameManager().onStarting();
            cancel();
            return;
        }

        if (Arrays.asList(10, 5, 4, 3, 2, 1).contains(data.getLobbyTime())) {
            Bukkit.getOnlinePlayers().stream().sorted(Comparator.comparing(Player::getName)).forEach(p -> p.sendTitle(CC.translate("&aIşınlanmaya"), CC.translate("&e" + data.getLobbyTime()), 10, 70, 20));
        }

        if(Arrays.asList(10, 5, 4, 3, 2, 1).contains(data.getLobbyTime())) {
            Msg.sendMessage(CC.SECONDARY + "Işınlanmaya " + CC.PRIMARY + data.getLobbyTime() + CC.SECONDARY + " saniye kaldı.");
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F));
        }
    }
}
