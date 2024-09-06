package com.dcgames.pillars.adapter;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class AssembleBoardEntry {
    private final AssembleBoard board;
    private String text;
    private String identifier;
    private Team team;
    private int position;

    public AssembleBoardEntry(AssembleBoard board, String text, int position) {
        this.board = board;
        this.text = text;
        this.position = position;
        this.identifier = this.board.getUniqueIdentifier(position);
        this.setup();
    }

    public void setup() {
        Team team;
        Scoreboard scoreboard = this.board.getScoreboard();
        if (scoreboard == null) {
            return;
        }
        String teamName = this.identifier;
        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }
        if ((team = scoreboard.getTeam(teamName)) == null) {
            team = scoreboard.registerNewTeam(teamName);
        }
        if (team.getEntries() == null || team.getEntries().isEmpty() || !team.getEntries().contains(this.identifier)) {
            team.addEntry(this.identifier);
        }
        if (!this.board.getEntries().contains(this)) {
            this.board.getEntries().add(this);
        }
        this.team = team;
    }

    public void send(int position) {
        if (this.text.length() > 16) {
            String suffix;
            String prefix = this.text.substring(0, 16);
            if (prefix.charAt(15) == '\u00a7') {
                prefix = prefix.substring(0, 15);
                suffix = this.text.substring(15, this.text.length());
            } else if (prefix.charAt(14) == '\u00a7') {
                prefix = prefix.substring(0, 14);
                suffix = this.text.substring(14, this.text.length());
            } else {
                suffix = ChatColor.getLastColors((String)prefix).equalsIgnoreCase(ChatColor.getLastColors((String)this.identifier)) ? this.text.substring(16, this.text.length()) : ChatColor.getLastColors((String)prefix) + this.text.substring(16, this.text.length());
            }
            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }
            this.team.setPrefix(prefix);
            this.team.setSuffix(suffix);
        } else {
            this.team.setPrefix(this.text);
            this.team.setSuffix("");
        }
        Score score = this.board.getObjective().getScore(this.identifier);
        score.setScore(position);
    }

    public void remove() {
        this.board.getIdentifiers().remove(this.identifier);
        this.board.getScoreboard().resetScores(this.identifier);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}

