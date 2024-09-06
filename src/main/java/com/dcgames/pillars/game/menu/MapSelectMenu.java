package com.dcgames.pillars.game.menu;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.menu.adapter.Menu;
import com.dcgames.pillars.game.menu.adapter.slots.Slot;
import com.dcgames.pillars.game.type.MapType;
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
public class MapSelectMenu extends Menu {

    @Override
    public List<Slot> getSlots(Player player) {

        List<Slot> slots = new ArrayList<>();

        slots.add(new RisingLava());
        slots.add(new Portals());
        slots.add(new Ablockalypse());
        slots.add(new FragileBlocks());
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

    private static class FragileBlocks extends Slot {

        private final String count = Math.min(Math.round(MapType.FRAGILE_BLOCKS.getVotes() * 100.0F / Bukkit.getOnlinePlayers().size()), 100) + "%";

        private final MapType type = MapType.FRAGILE_BLOCKS;
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
            ItemBuilder item = new ItemBuilder(Material.COBBLESTONE);
            item.name(CC.PRIMARY + type.getName());
            item.lore(lore);

            return item.build();
        }


        @Override
        public int getSlot() {
            return 17;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if(plugin.getVoteManager().hasVotedMapType(player)) {
                plugin.getVoteManager().handleRemoveMapType(player, type);
                return;
            }

            plugin.getVoteManager().handleAddMapTypeVote(player, type);
        }
    }

    private static class Ablockalypse extends Slot {

        private final String count = Math.min(Math.round(MapType.ABLOCKALYPSE.getVotes() * 100.0F / Bukkit.getOnlinePlayers().size()), 100) + "%";

        private final MapType type = MapType.ABLOCKALYPSE;
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
            ItemBuilder item = new ItemBuilder(Material.OBSIDIAN);
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
            if(plugin.getVoteManager().hasVotedMapType(player)) {
                plugin.getVoteManager().handleRemoveMapType(player, type);
                return;
            }

            plugin.getVoteManager().handleAddMapTypeVote(player, type);
        }
    }

    private static class Portals extends Slot {

        private final String count = Math.min(Math.round(MapType.PORTALS.getVotes() * 100.0F / Bukkit.getOnlinePlayers().size()), 100) + "%";

        private final MapType type = MapType.PORTALS;
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
            ItemBuilder item = new ItemBuilder(Material.END_PORTAL_FRAME);
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
            if(plugin.getVoteManager().hasVotedMapType(player)) {
                plugin.getVoteManager().handleRemoveMapType(player, type);
                return;
            }

            plugin.getVoteManager().handleAddMapTypeVote(player, type);
        }
    }

    private static class RisingLava extends Slot {

        private final String count = Math.min(Math.round(MapType.RISING_LAVA.getVotes() * 100.0F / Bukkit.getOnlinePlayers().size()), 100) + "%";

        private final MapType type = MapType.RISING_LAVA;
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
            ItemBuilder item = new ItemBuilder(Material.LAVA_BUCKET);
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
            if(plugin.getVoteManager().hasVotedMapType(player)) {
                plugin.getVoteManager().handleRemoveMapType(player, type);
                return;
            }

            plugin.getVoteManager().handleAddMapTypeVote(player, type);
        }
    }

    private static class Normal extends Slot {
        private final String count = Math.min(Math.round(MapType.NORMAL.getVotes() * 100.0F / Bukkit.getOnlinePlayers().size()), 100) + "%";

        private final MapType type = MapType.NORMAL;
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
            return 9;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if(plugin.getVoteManager().hasVotedMapType(player)) {
                plugin.getVoteManager().handleRemoveMapType(player, type);
                return;
            }

            plugin.getVoteManager().handleAddMapTypeVote(player, type);
        }
    }
}
