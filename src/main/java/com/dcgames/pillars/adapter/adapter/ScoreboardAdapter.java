package com.dcgames.pillars.adapter.adapter;

import com.dcgames.pillars.Fortune;
import com.dcgames.pillars.adapter.AssembleAdapter;
import com.dcgames.pillars.adapter.animation.IPAnimation;
import com.dcgames.pillars.game.GameData;
import com.dcgames.pillars.game.GameManager;
import com.dcgames.pillars.game.player.PlayerData;
import com.dcgames.pillars.game.type.MapType;
import com.dcgames.pillars.util.chat.CC;
import com.dcgames.pillars.util.StringUtil;
import com.dcgames.pillars.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter implements AssembleAdapter {

   @Override
   public String getTitle(final Player player) {
      return CC.B_PRIMARY + "Şans Sütunları";
   }

   @Override
   public List<String> getLines(Player player) {
      List<String> board = new ArrayList<>();
      PlayerData playerData = Fortune.getInstance().getPlayerManager().getPlayerData(player.getUniqueId());
      GameData data = GameManager.getData();
      String gameTime = TimeUtil.niceTime(GameManager.getData().getGameTime());
      board.add(CC.GRAY + CC.STRIKE_THROUGH + "--------------------");
      switch (GameManager.getData().getGameState()) {
         case LOBBY: {
            board.add(CC.SECONDARY + "Oyuncular: " + CC.PRIMARY + Bukkit.getOnlinePlayers().size());
            if (Bukkit.getOnlinePlayers().size() < 8) {
               board.add(CC.SECONDARY + "Oyuncular bekleniyor" + GameManager.getData().getLoading());
               break;
            }
            if (data.isCanStartCountdown()) {
               board.add(CC.PRIMARY + StringUtil.niceTime(data.getLobbyTime()) + CC.SECONDARY + " saniye içinde başlıyor" + CC.SECONDARY + GameManager.getData().getLoading());
               break;
            }
            break;
         }
         case STARTING: {
            board.add(CC.SECONDARY + "Oyuncular: " + CC.PRIMARY + Bukkit.getOnlinePlayers().size());
            board.add(CC.PRIMARY + StringUtil.niceTime(data.getStartingTime()) + CC.SECONDARY + " saniye içinde başlıyor" + CC.SECONDARY + GameManager.getData().getLoading());
            break;
         }
         case PLAYING: {
            board.add(CC.SECONDARY + "Oyun Süresi: " + CC.PRIMARY + gameTime);
            board.add(CC.SECONDARY + "Geriye kalan: " + CC.PRIMARY + Fortune.getInstance().getPlayerManager().getAlivePlayers() + "/" + data.getInitial());
            board.add(CC.SECONDARY + "İzliyeciler: " + CC.PRIMARY + Fortune.getInstance().getPlayerManager().getSpectators());
            if (playerData.isAlive()) {
               board.add(CC.SECONDARY + "Öldürme: " + CC.PRIMARY + playerData.getGameKills());
            }
            board.add(" ");
            board.add(CC.SECONDARY + "Sınır: " + CC.PRIMARY + data.getBorder());
            if (!GameManager.getData().isLavaEnded() && GameManager.getData().getMapType() == MapType.RISING_LAVA) {
               board.add(CC.SECONDARY + "Lavın yükselmesine: " + CC.PRIMARY + StringUtil.niceTime(data.getLavaTime()));
            }
            break;
         }
         case WINNER: {
            board.add(CC.SECONDARY + "Kazanan: " + CC.PRIMARY + data.getWinner());
            break;
         }
      }
      board.add(" ");
      board.add(CC.RESET + CC.GRAY + IPAnimation.animation);
      board.add(CC.RESET + CC.GRAY + CC.STRIKE_THROUGH + "--------------------");
      return board;
   }
}
