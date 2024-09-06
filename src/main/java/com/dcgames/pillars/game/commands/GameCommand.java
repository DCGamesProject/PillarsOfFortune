package com.dcgames.pillars.game.commands;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.GameState;
import com.dcgames.pillars.game.GameData;
import com.dcgames.pillars.game.GameManager;
import com.dcgames.pillars.util.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

public class GameCommand extends BaseCommand {

    private final Fortune plugin;

    public GameCommand() {
        super("panel", "fortune.staff", false);
        this.plugin = Fortune.getInstance();
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        GameData gameData = GameManager.getData();
        if (args.length < 1) {
            player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------------");
            player.sendMessage(CC.translate("&a/panel start &7(Oyunu manuel olarak başlat)"));
            player.sendMessage(CC.translate("&a/panel setspawn"));
            player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------------");
            return;
        }
        switch (args[0]) {
            case "setspawn": {
                if (gameData.getGameState() != GameState.LOBBY) {
                    player.sendMessage(CC.translate("&cBu yalnızca lobideyken kullanılabilir."));
                    return;
                }
                plugin.getLocationManager().setLocation("Spawn", player.getLocation());
                player.sendMessage(CC.GREEN + "Spawn başarıyla ayarlandı.");
                break;
            }
            case "start": {
                if (GameManager.getData().getGameState().equals(GameState.LOBBY)) {
                    if (GameManager.getData().getLobbyTime() <= 6) {
                        player.sendMessage(CC.RED + "Oyun zaten başlıyor!");
                        return;
                    }
                    /*if (8 >= Bukkit.getOnlinePlayers().size()) {
                        player.sendMessage(CC.RED + "Oyuna başlamak için 8 oyuncuya ihtiyacınız var.");
                        return;
                    }*/
                    GameManager.getData().setCanStartCountdown(true);
                    player.sendMessage(CC.GREEN + "Geri sayımı 5 saniyeye düşürdünüz.");
                    GameManager.getData().setLobbyTime(6);
                } else {
                    player.sendMessage(CC.RED + "Şu an bu komudu kullanamazsın!");
                }
                break;
            }
            default: {
                player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------------");
                player.sendMessage(CC.translate("&a/panel start &7(Oyunu manuel olarak başlat)"));
                player.sendMessage(CC.translate("&a/panel setspawn"));
                player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------------");
            }
        }
    }
}

