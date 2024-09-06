package com.dcgames.pillars.util;

import com.dcgames.pillars.Fortune;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class BungeeUtil {

	private BungeeUtil() {
		throw new RuntimeException("Cannot instantiate a utility class.");
	}

	public static void sendPlayer(Player player, String server){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try {
			dataOutputStream.writeUTF("Connect");
			dataOutputStream.writeUTF(server);
		} catch (IOException e) {
			e.printStackTrace();
		}

		player.sendPluginMessage(Fortune.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
	}

	public static void registerBungeecord() {
		if (!Bukkit.getMessenger().isOutgoingChannelRegistered(Fortune.getInstance(), "BungeeCord") && Fortune.getInstance().getConfig().getBoolean("bungeecord")) {
			Bukkit.getMessenger().registerOutgoingPluginChannel(Fortune.getInstance(), "BungeeCord");
		}
	}

}
