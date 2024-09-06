package com.dcgames.pillars.tasks;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.tasks.type.AblockalypseTask;
import com.dcgames.pillars.tasks.type.LavaTask;
import com.dcgames.pillars.util.BungeeUtil;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.chat.Msg;
import com.dcgames.pillars.game.GameData;
import com.dcgames.pillars.game.GameManager;
import com.dcgames.pillars.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Objects;

public class EndTask extends BukkitRunnable {

    private int launchedFireworks;

    public EndTask() {
        runTaskTimer(Fortune.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        GameData data = GameManager.getData();

        data.setEndTime(data.getEndTime() - 1);

        new LavaTask().cancel();
        new AblockalypseTask().cancel();

        if(Settings.BUNGEECORD) {
            if(data.getEndTime() == 2) {
                Bukkit.getOnlinePlayers().forEach(player -> BungeeUtil.sendPlayer(player, Settings.FALLBACK_SERVER));
            }
        }

        if(data.getEndTime() <= 0) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            cancel();
            return;
        }

        if(launchedFireworks++ <= 5) {
            Fortune.getInstance().getPlayerManager().getListOfAlivePlayers().stream().map(d -> Bukkit.getPlayer(d.getUuid())).filter(Objects::nonNull).forEach(player -> {
                Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                fireworkMeta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.ORANGE).withFade(Color.YELLOW).build());
                fireworkMeta.setPower(3);
                firework.setFireworkMeta(fireworkMeta);
            });
        }

        if(Arrays.asList(15, 10, 5, 4, 3, 2, 1).contains(data.getEndTime())) {
            Msg.sendMessage(CC.SECONDARY + "Oyun " + CC.PRIMARY + data.getEndTime() + CC.SECONDARY + " saniye içinde sona ercek.");
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F));
        }
    }
}
