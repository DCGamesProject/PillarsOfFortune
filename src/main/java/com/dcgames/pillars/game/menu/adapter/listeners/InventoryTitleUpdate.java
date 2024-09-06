package com.dcgames.pillars.game.menu.adapter.listeners;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InventoryTitleUpdate {
    private static Method m_Player_GetHandle;
    private static Method m_PlayerConnection_sendPacket;
    private static Method m_CraftChatMessage_fromString;
    private static Method m_EntityPlayer_updateInventory;
    private static Field f_EntityPlayer_playerConnection;
    private static Field f_EntityPlayer_activeContainer;
    private static Field f_Container_windowId;
    private static Constructor<?> c_PacketOpenWindow;
    private static String nms_version;

    public static void sendInventoryTitle(Player player, String title) {
        Preconditions.checkNotNull(player, "player");
        try {
            sendInventoryTitle0(player, title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendInventoryTitle0(Player player, String title) throws Exception {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (inventory == null) {
            return;
        }
        if (m_Player_GetHandle == null) {
            m_Player_GetHandle = player.getClass().getMethod("getHandle");
        }
        Object nms_EntityPlayer = m_Player_GetHandle.invoke(player);
        if (f_EntityPlayer_playerConnection == null) {
            f_EntityPlayer_playerConnection = nms_EntityPlayer.getClass().getField("playerConnection");
        }
        Object nms_PlayerConnection = f_EntityPlayer_playerConnection.get(nms_EntityPlayer);
        if (f_EntityPlayer_activeContainer == null) {
            f_EntityPlayer_activeContainer = nms_EntityPlayer.getClass().getField("activeContainer");
        }
        Object nms_Container = f_EntityPlayer_activeContainer.get(nms_EntityPlayer);
        if (f_Container_windowId == null) {
            f_Container_windowId = nms_Container.getClass().getField("windowId");
        }
        int windowId = f_Container_windowId.getInt(nms_Container);

        sendPacket18(nms_PlayerConnection, nms_EntityPlayer, nms_Container, windowId, inventory, title);
    }

    private static void sendPacket18(Object nms_playerConnection, Object nms_EntityPlayer, Object nms_Container, int windowId, Inventory inventory, String title) throws Exception {
        if (c_PacketOpenWindow == null) {
            c_PacketOpenWindow = findNmsClass("net.minecraft.server." + getNmsVersion() + ".PacketPlayOutOpenWindow")
                    .getConstructor(int.class, String.class, findNmsClass("net.minecraft.server." + getNmsVersion() + ".IChatBaseComponent"), int.class);
        }


        int size = 0;
        String id = null;

        switch (inventory.getType()) {
            case ANVIL:
                id = "minecraft:anvil";
                break;
            case BEACON:
                id = "minecraft:beacon";
                break;
            case BREWING:
                id = "minecraft:brewing_stand";
                break;
            case DISPENSER:
                id = "minecraft:dispenser";
                break;
            case DROPPER:
                id = "minecraft:dropper";
                break;
            case ENCHANTING:
                id = "minecraft:enchanting_table";
                break;
            case CHEST:
                id = "minecraft:chest";
                size = inventory.getSize();
                break;
            case FURNACE:
                id = "minecraft:furnace";
                break;
            case HOPPER:
                id = "minecraft:hopper";
                break;
            case MERCHANT:
                id = "minecraft:villager";
                size = 3;
                break;
            case WORKBENCH:
                id = "minecraft:crafting_table";
                break;
            default:
                return;
        }

        if (m_CraftChatMessage_fromString == null) {
            m_CraftChatMessage_fromString = findCrbClass("org.bukkit.craftbukkit." + getNmsVersion() + ".util.CraftChatMessage")
                    .getMethod("fromString", String.class);
        }

        if (m_EntityPlayer_updateInventory == null) {
            m_EntityPlayer_updateInventory = nms_EntityPlayer.getClass().getMethod("updateInventory", findNmsClass("net.minecraft.server." + getNmsVersion() + ".Container"));
        }

        Object nms_title = m_CraftChatMessage_fromString.invoke(null, title);

        Object nms_packet = c_PacketOpenWindow.newInstance(windowId, id, nms_title, size);

        sendPacket(nms_playerConnection, nms_packet);

        m_EntityPlayer_updateInventory.invoke(nms_EntityPlayer, nms_Container);
    }

    private static void sendPacket(Object playerConnection, Object packet) throws Exception {
        if (m_PlayerConnection_sendPacket == null) {
            m_PlayerConnection_sendPacket = playerConnection.getClass().getMethod("sendPacket", findNmsClass("net.minecraft.server." + getNmsVersion() + ".Packet"));
        }
        m_PlayerConnection_sendPacket.invoke(playerConnection, packet);
    }

    private static String getNmsVersion() {
        if (nms_version == null) {
            nms_version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        }
        return nms_version;
    }

    private static Class<?> findNmsClass(String name) throws Exception {
        return Class.forName(name);
    }

    private static Class<?> findCrbClass(String name) throws Exception {
        return Class.forName(name);
    }
}
