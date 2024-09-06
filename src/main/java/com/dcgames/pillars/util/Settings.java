package com.dcgames.pillars.util;

import com.dcgames.pillars.Fortune;

public class Settings {

   public static boolean BUNGEECORD = Fortune.getInstance().getCoreConfig().getBoolean("BUNGEECORD.ENABLED");
   public static String FALLBACK_SERVER = Fortune.getInstance().getCoreConfig().getString("BUNGEECORD.FALLBACK-SERVER");

}
