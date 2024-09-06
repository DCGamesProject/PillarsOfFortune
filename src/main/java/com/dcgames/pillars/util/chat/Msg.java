package com.dcgames.pillars.util.chat;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class Msg {
    
    public static char HEART = '❤';

    public static void sendMessage(Clickable clickable) {
        Fortune.getInstance().getServer().getOnlinePlayers().forEach(clickable::sendToPlayer);
    }

    public static void sendMessage(Clickable clickable, Sound sound) {
        Fortune.getInstance().getServer().getOnlinePlayers().forEach((o) -> {
            clickable.sendToPlayer(o);
            o.playSound(o.getLocation(), sound, 1.0F, 1.0F);
        });
    }

    public static void sendMessage(String message) {
        Fortune.getInstance().getServer().getOnlinePlayers().forEach((o) -> {
            o.sendMessage(CC.translate(message));
        });
        logConsole(message);
    }

    public static void sendMessage(String message, Sound sound) {
        Fortune.getInstance().getServer().getOnlinePlayers().forEach((o) -> {
            o.sendMessage(CC.translate(message));
            o.playSound(o.getLocation(), sound, 1.0F, 1.0F);
        });
        logConsole(message);
    }

    public static void sendMessage(Clickable clickable, String rank, Sound sound) {
        Fortune.getInstance().getServer().getOnlinePlayers().stream().filter((o) -> Util.testPermission(o, rank)).forEach((o) -> {
            clickable.sendToPlayer(o);
            o.playSound(o.getLocation(), sound, 1.0F, 1.0F);
        });
    }

    public static void sendMessage(Clickable clickable, String rank) {
        Fortune.getInstance().getServer().getOnlinePlayers().stream().filter((o) -> Util.testPermission(o, rank)).forEach(clickable::sendToPlayer);
    }

    public static void sendMessage(String message, String rank, Sound sound) {
        Fortune.getInstance().getServer().getOnlinePlayers().stream().filter((o) -> Util.testPermission(o, rank)).forEach((o) -> {
            o.sendMessage(CC.translate(message));
            o.playSound(o.getLocation(), sound, 1.0F, 1.0F);
        });
        logConsole(message);
    }

    public static void sendMessage(String message, String rank) {
        Fortune.getInstance().getServer().getOnlinePlayers().stream().filter((o) -> Util.testPermission(o, rank)).forEach((o) -> {
            o.sendMessage(CC.translate(message));
        });
        logConsole(message);
    }

    public static void logConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(message));
    }
}
