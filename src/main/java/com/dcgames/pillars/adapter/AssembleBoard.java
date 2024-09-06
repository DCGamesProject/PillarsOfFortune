package com.dcgames.pillars.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dcgames.pillars.adapter.event.AssembleBoardCreatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class AssembleBoard {
    private final List<AssembleBoardEntry> entries = new ArrayList<AssembleBoardEntry>();
    private final List<String> identifiers = new ArrayList<String>();
    private final UUID uuid;
    private Assemble assemble;

    public AssembleBoard(Player player, Assemble assemble) {
        this.uuid = player.getUniqueId();
        this.assemble = assemble;
        this.setup(player);
    }

    public Scoreboard getScoreboard() {
        Player player = Bukkit.getPlayer((UUID)this.getUuid());
        if (this.getAssemble().isHook() || player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
            return player.getScoreboard();
        }
        return Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public Objective getObjective() {
        Scoreboard scoreboard = this.getScoreboard();
        if (scoreboard.getObjective("Assemble") == null) {
            Objective objective = scoreboard.registerNewObjective("Assemble", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(this.getAssemble().getAdapter().getTitle(Bukkit.getPlayer((UUID)this.getUuid())));
            return objective;
        }
        return scoreboard.getObjective("Assemble");
    }

    private void setup(Player player) {
        Scoreboard scoreboard = this.getScoreboard();
        player.setScoreboard(scoreboard);
        this.getObjective();
        AssembleBoardCreatedEvent createdEvent = new AssembleBoardCreatedEvent(this);
        Bukkit.getPluginManager().callEvent(createdEvent);
    }

    public AssembleBoardEntry getEntryAtPosition(int pos) {
        if (pos >= this.entries.size()) {
            return null;
        }
        return this.entries.get(pos);
    }

    public String getUniqueIdentifier(int position) {
        String identifier = AssembleBoard.getRandomChatColor(position) + (Object)ChatColor.WHITE;
        while (this.identifiers.contains(identifier)) {
            identifier = identifier + AssembleBoard.getRandomChatColor(position) + (Object)ChatColor.WHITE;
        }
        if (identifier.length() > 16) {
            return this.getUniqueIdentifier(position);
        }
        this.identifiers.add(identifier);
        return identifier;
    }

    private static String getRandomChatColor(int position) {
        return ChatColor.values()[position].toString();
    }

    public List<AssembleBoardEntry> getEntries() {
        return this.entries;
    }

    public List<String> getIdentifiers() {
        return this.identifiers;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Assemble getAssemble() {
        return this.assemble;
    }
}

