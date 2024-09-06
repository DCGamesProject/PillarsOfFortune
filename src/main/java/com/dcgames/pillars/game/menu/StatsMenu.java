package com.dcgames.pillars.game.menu;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.menu.adapter.Menu;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import com.dcgames.pillars.game.player.PlayerData;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.ItemBuilder;
import com.dcgames.pillars.util.chat.Replacement;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class StatsMenu extends Menu {

    private final Player player;

    @Override
    public List<Slot> getSlots(Player player) {

        List<Slot> slots = new ArrayList<>();

        slots.add(new UHCMeetupStats());

        for (int i = 0; i < Fortune.getInstance().getMenus().getInt("STATS.SLOT"); i++) {
            if (!Slot.hasSlot(slots, i)) {
                slots.add(Slot.getGlass(i));
            }
        }

        return slots;
    }

    @Override
    public String getName(Player player) {
        return CC.translate("");
    }

    @Override
    public void onOpen(Player player) {
        //InventoryTitleUpdate.sendInventoryTitle(player, CC.translate(Fortune.getInstance().getMenus().getString("STATS.TITLE").replace("<player>", player.getName())));
    }

    private class UHCMeetupStats extends Slot {
        PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
        int dataKills = playerData.getKills();
        int dataDeaths = playerData.getDeaths();
        int dataHighestStreak = playerData.getKillStreak();
        int dataPlayed = playerData.getPlayed();
        int dataWins = playerData.getWins();

        @Override
        public ItemStack getItem(Player player) {
            ItemBuilder item = new ItemBuilder(Material.valueOf(Fortune.getInstance().getMenus().getString("STATS.MATERIAL")));
            item.name(Fortune.getInstance().getMenus().getString("STATS.ITEM-TITLE"));
            plugin.getMenus().getStringList("STATS.LORE").forEach(loreline -> {
                Replacement replacement = new Replacement(loreline);
                item.addLoreLine((CC.translate(replacement.toString().replace("<wins>", String.valueOf(dataWins)).replace("<played>", String.valueOf(dataPlayed)).replace("<kills>", String.valueOf(dataKills)).replace("<killstreak>", String.valueOf(dataHighestStreak)).replace("<deaths>", String.valueOf(dataDeaths)))));
            });
            return item.build();
        }

        @Override
        public int getSlot() {
            return Fortune.getInstance().getMenus().getInt("STATS.ITEM-SLOT");
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }
}
