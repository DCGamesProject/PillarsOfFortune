package com.dcgames.pillars.adapter.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AssembleBoardDestroyEvent
extends Event
implements Cancellable {
    public static HandlerList handlerList = new HandlerList();
    private Player player;
    private boolean cancelled = false;

    public AssembleBoardDestroyEvent(Player player) {
        this.player = player;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

