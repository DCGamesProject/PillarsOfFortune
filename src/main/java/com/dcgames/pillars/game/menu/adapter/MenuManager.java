package com.dcgames.pillars.game.menu.adapter;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MenuManager {
    public Map<UUID, Menu> openedMenus = new HashMap<>();
    public Map<UUID, Menu> lastOpenedMenus = new HashMap<> ();
    public Map<UUID, Menu> openedMenuTing = new HashMap<>();
}

