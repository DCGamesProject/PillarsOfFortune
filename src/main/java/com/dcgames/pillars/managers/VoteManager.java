package com.dcgames.pillars.managers;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.player.PlayerData;
import com.dcgames.pillars.game.type.GameType;
import com.dcgames.pillars.game.type.MapType;
import com.dcgames.pillars.util.chat.CC;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class VoteManager {

    public MapType getHighestMapTypeVote() {
        MapType highestType = MapType.NORMAL;
        int highestVote = 0;
        for (MapType type : MapType.values()) {
            int votes = type.getVotes();
            if (votes > highestVote) {
                highestType = type;
                highestVote = votes;
            }
        }
        return highestType;
    }

    public void handleAddMapTypeVote(Player player, MapType type) {
        PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
        if (playerData != null) {
            type.setVotes(type.getVotes() + 1);
            player.sendMessage(CC.translate("&fYou have voted for &c" + type.getName() + "&f."));
            playerData.setLastMapTypeVoted(type);
        }
    }

    public void handleRemoveMapType(Player player, MapType newType) {
        PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
        if (playerData == null) return;

        MapType lastVotedType = playerData.getLastMapTypeVoted();
        if (lastVotedType == newType) {
            player.sendMessage(CC.translate("&cYou already voted for &c&l&n" + lastVotedType.getName() + "&c style."));
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            return;
        }

        if (lastVotedType != null) {
            lastVotedType.setVotes(lastVotedType.getVotes() - 1);
        }
        handleAddMapTypeVote(player, newType);
    }

    public boolean hasVotedMapType(Player player) {
        PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
        return playerData != null && playerData.getLastMapTypeVoted() != null;
    }

    public GameType getHighestGameTypeVote() {
        GameType highestType = GameType.NORMAL;
        int highestVote = 0;
        for (GameType type : GameType.values()) {
            int votes = type.getVotes();
            if (votes > highestVote) {
                highestType = type;
                highestVote = votes;
            }
        }
        return highestType;
    }

    public void handleAddGameTypeVote(Player player, GameType type) {
        PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
        if (playerData != null) {
            type.setVotes(type.getVotes() + 1);
            player.sendMessage(CC.translate("&fYou have voted for &c" + type.getName() + "&f."));
            playerData.setLastGameTypeVoted(type);
        }
    }

    public void handleRemoveGameType(Player player, GameType newType) {
        PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
        if (playerData == null) return;

        GameType lastVotedType = playerData.getLastGameTypeVoted();
        if (lastVotedType == newType) {
            player.sendMessage(CC.translate("&cYou already voted for &c&l&n" + lastVotedType.getName() + "&c style."));
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            return;
        }

        if (lastVotedType != null) {
            lastVotedType.setVotes(lastVotedType.getVotes() - 1);
        }
        handleAddGameTypeVote(player, newType);
    }

    public boolean hasVotedGameType(Player player) {
        PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
        return playerData != null && playerData.getLastGameTypeVoted() != null;
    }
}
