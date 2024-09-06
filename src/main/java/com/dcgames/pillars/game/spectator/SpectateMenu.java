package com.dcgames.pillars.game.spectator;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.game.player.PlayerData;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.ItemBuilder;
import com.dcgames.pillars.util.menu.Menu;
import com.dcgames.pillars.util.menu.button.Button;
import com.dcgames.pillars.util.menu.button.event.ButtonClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpectateMenu extends Menu {

   private int page;

   public SpectateMenu(Player viewer) {
      super(viewer, "İzleyici Menüsü", 4);
      this.page = 1;
   }

   @Override
   protected void setup() {
      surround(BACKGROUND);
      Comparator<PlayerData> comparator = Comparator.comparingInt(PlayerData::getGameKills);
      List<PlayerData> players = new ArrayList<>();

      for (Player online : Bukkit.getWorld("arena").getPlayers()) {
         if (!Fortune.getInstance().getPlayerManager().getPlayerData(online.getUniqueId()).isSpectator()) {
            players.add(Fortune.getInstance().getPlayerManager().getPlayerData(online.getUniqueId()));
         }
      }

      players.sort(comparator.reversed());
      int max = (int) Math.ceil(players.size() / 14.0D);
      int minIndex = (int) ((this.page - 1) * 14.0D);
      int maxIndex = (int) (this.page * 14.0D);

      if (this.page != 1) {
         set(getSize() - 9, new Button(new ItemBuilder(Material.ARROW)
                 .name(CC.YELLOW + "Önceki Sayfa")
                 .lore(Collections.singletonList(CC.GRAY + "Sayfaya geri dön " + (this.page - 1) + "."))
                 .build()) {
            @Override
            public void onClick(ButtonClickEvent event) {
               SpectateMenu.this.page--;
               SpectateMenu.this.update();
            }
         });
      }

      if (this.page + 1 <= max) {
         set(getSize() - 1, new Button(new ItemBuilder(Material.ARROW)
                 .name(CC.YELLOW + "Sonraki Sayfa")
                 .lore(Collections.singletonList(CC.GRAY + "Sayfaya devam et " + (this.page + 1) + "."))
                 .build()) {
            @Override
            public void onClick(ButtonClickEvent event) {
               SpectateMenu.this.page++;
               SpectateMenu.this.update();
            }
         });
      }

      players.forEach(player -> {
         int index = players.indexOf(player);
         if (index >= minIndex && index < maxIndex) {
            index -= (int) (14.0D * (this.page - 1)) - 9;
            int finalIndex = index + ((index > 22) ? 5 : ((index > 15) ? 3 : 1));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(CC.GRAY + "» " + CC.WHITE + "Öldürme: " + CC.YELLOW + player.getGameKills());
            lore.add("");
            lore.add(CC.WHITE + "Işınlanmak için tıklayın.");

            set(finalIndex, new Button(new ItemBuilder(Material.PLAYER_HEAD)
                    .name(player.getName())
                    .lore(lore)
                    .build()) {
               @Override
               public void onClick(ButtonClickEvent event) {
                  Player target = Bukkit.getPlayer(player.getUuid());
                  if (target != null) {
                     event.getPlayer().teleport(target);
                  }
               }
            });
         }
      });
   }
}
