package com.dcgames.pillars.adapter;

import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class AssembleThread
extends Thread {
    private Assemble assemble;

    AssembleThread(Assemble assemble) {
        this.assemble = assemble;
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.tick();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            try {
                AssembleThread.sleep(this.assemble.getTicks() * 50L);
                continue;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void tick() {
        for (Player player : this.assemble.getPlugin().getServer().getOnlinePlayers()) {
            try {
                List<String> newLines;
                AssembleBoard board = this.assemble.getBoards().get(player.getUniqueId());
                if (board == null) continue;
                Scoreboard scoreboard = board.getScoreboard();
                Objective objective = board.getObjective();
                if (scoreboard == null || objective == null) continue;
                String title = ChatColor.translateAlternateColorCodes((char)'&', (String)this.assemble.getAdapter().getTitle(player));
                if (!objective.getDisplayName().equals(title)) {
                    objective.setDisplayName(title);
                }
                if ((newLines = this.assemble.getAdapter().getLines(player)) == null || newLines.isEmpty()) {
                    board.getEntries().forEach(AssembleBoardEntry::remove);
                    board.getEntries().clear();
                } else {
                    if (this.assemble.getAdapter().getLines(player).size() > 15) {
                        newLines = this.assemble.getAdapter().getLines(player).subList(0, 15);
                    }
                    if (!this.assemble.getAssembleStyle().isDecending()) {
                        Collections.reverse(newLines);
                    }
                    if (board.getEntries().size() > newLines.size()) {
                        for (int i = newLines.size(); i < board.getEntries().size(); ++i) {
                            AssembleBoardEntry entry = board.getEntryAtPosition(i);
                            if (entry == null) continue;
                            entry.remove();
                        }
                    }
                    int cache = this.assemble.getAssembleStyle().getStartNumber();
                    for (int i = 0; i < newLines.size(); ++i) {
                        AssembleBoardEntry entry = board.getEntryAtPosition(i);
                        String line = ChatColor.translateAlternateColorCodes((char)'&', (String)newLines.get(i));
                        if (entry == null) {
                            entry = new AssembleBoardEntry(board, line, i);
                        }
                        entry.setText(line);
                        entry.setup();
                        entry.send(this.assemble.getAssembleStyle().isDecending() ? cache-- : cache++);
                    }
                }
                if (player.getScoreboard() == scoreboard || this.assemble.isHook()) continue;
                player.setScoreboard(scoreboard);
            }
            catch (Exception e) {
                throw new AssembleException("There was an error updating " + player.getName() + "'s scoreboard.");
            }
        }
    }
}

