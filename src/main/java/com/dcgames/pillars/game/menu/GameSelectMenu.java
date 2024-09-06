package com.dcgames.pillars.game.menu;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.menu.adapter.Menu;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import com.dcgames.pillars.game.type.GameType;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.ItemBuilder;
import com.dcgames.pillars.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GameSelectMenu extends Menu {

    @Override
    public List<Slot> getSlots(Player player) {

        List<Slot> slots = new ArrayList<>();

        slots.add(new Shuffle());
        slots.add(new Swapper());
        slots.add(new Normal());

        for (int i = 0; i < 27; i++) {
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

    private static class Swapper extends Slot {

        private final String count = Math.min(Math.round(GameType.SWAPPER.getVotes() * 100.0F / Bukkit.getOnlinePlayers().size()), 100) + "%";

        private final GameType type = GameType.SWAPPER;
        private final Fortune plugin = Fortune.getInstance();

        @Override
        public ItemStack getItem(Player player) {
            //LORE
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(CC.GRAY + StringUtil.NICE_CHAR + CC.GREEN + " Votes " + CC.GRAY + count);
            lore.add("");
            lore.add(CC.GRAY + type.getDescreption());
            lore.add("");
            lore.add(CC.YELLOW + "Click to vote.");

            // ITEM
            ItemBuilder item = new ItemBuilder(Material.ENDER_PEARL);
            item.name(CC.PRIMARY + type.getName());
            item.lore(lore);

            return item.build();
        }


        @Override
        public int getSlot() {
            return 15;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if(plugin.getVoteManager().hasVotedGameType(player)) {
                plugin.getVoteManager().handleRemoveGameType(player, type);
                return;
            }

            plugin.getVoteManager().handleAddGameTypeVote(player, type);
        }
    }

    private static class Shuffle extends Slot {

        private final String count = Math.min(Math.round(GameType.SHUFFLE.getVotes() * 100.0F / Bukkit.getOnlinePlayers().size()), 100) + "%";

        private final GameType type = GameType.SHUFFLE;
        private final Fortune plugin = Fortune.getInstance();

        @Override
        public ItemStack getItem(Player player) {
            //LORE
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(CC.GRAY + StringUtil.NICE_CHAR + CC.GREEN + " Votes " + CC.GRAY + count);
            lore.add("");
            lore.add(CC.GRAY + type.getDescreption());
            lore.add("");
            lore.add(CC.YELLOW + "Click to vote.");

            // ITEM
            ItemBuilder item = new ItemBuilder(Material.FILLED_MAP);
            item.name(CC.PRIMARY + type.getName());
            item.lore(lore);

            return item.build();
        }


        @Override
        public int getSlot() {
            return 13;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if(plugin.getVoteManager().hasVotedGameType(player)) {
                plugin.getVoteManager().handleRemoveGameType(player, type);
                return;
            }

            plugin.getVoteManager().handleAddGameTypeVote(player, type);
        }
    }

    private static class Normal extends Slot {
        private final String count = Math.min(Math.round(GameType.NORMAL.getVotes() * 100.0F / Bukkit.getOnlinePlayers().size()), 100) + "%";

        private final GameType type = GameType.NORMAL;
        private final Fortune plugin = Fortune.getInstance();

        @Override
        public ItemStack getItem(Player player) {
            //LORE
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(CC.GRAY + StringUtil.NICE_CHAR + CC.GREEN + " Votes " + CC.GRAY + count);
            lore.add("");
            lore.add(CC.GRAY + type.getDescreption());
            lore.add("");
            lore.add(CC.YELLOW + "Click to vote.");

            // ITEM
            ItemBuilder item = new ItemBuilder(Material.EMERALD);
            item.name(CC.PRIMARY + type.getName());
            item.lore(lore);

            return item.build();
        }

        @Override
        public int getSlot() {
            return 11;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if(plugin.getVoteManager().hasVotedGameType(player)) {
                plugin.getVoteManager().handleRemoveGameType(player, type);
                return;
            }

            plugin.getVoteManager().handleAddGameTypeVote(player, type);
        }
    }
}
