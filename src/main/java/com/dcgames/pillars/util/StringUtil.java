package com.dcgames.pillars.util;

import com.dcgames.pillars.util.chat.CC;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StringUtil {
   public static final char NICE_CHAR = '●';
   public static final char NICE_CHAR2 = '➥';
   public static final char HEART = '❤';
   public static final String NO_PERMISSION;
   public static final String FOR_PLAYER_ONLY;
   public static final String NO_PLAYER_FOUND;
   public static final String INTEGER_NOT_VALID;
   public static final String LOAD_ERROR_2;
   private static ThreadLocal<DecimalFormat> seconds = ThreadLocal.withInitial(() -> new DecimalFormat("0.#"));
   private static ThreadLocal<DecimalFormat> trailing = ThreadLocal.withInitial(() -> new DecimalFormat("0"));

   public static String formatInteger(int value) {
      return String.format("%,d", value);
   }

   public static String niceBuilder(Collection<String> collection) {
      return niceBuilder(collection, ", ", " and ", ".");
   }
   public static String niceTime(int i) {
      int r = i * 1000;
      int sec = r / 1000 % 60;
      int min = r / 60000 % 60;
      int h = r / 3600000 % 24;
      return (h > 0 ? (h < 10 ? "0" : "") + h + ":" : "") + (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec);
   }

   public static String niceTime(long millis, boolean milliseconds) {
      return niceTime(millis, milliseconds, true);
   }

   public static String niceTime(long duration, boolean milliseconds, boolean trail) {
      if(milliseconds && duration < TimeUnit.MINUTES.toMillis(1)) {
         return (trail ? trailing : seconds).get().format((double) duration * 001) + 's';
      }

      return DurationFormatUtils.formatDuration(duration, (duration >= TimeUnit.HOURS.toMillis(1) ? "HH:" : "") + "mm:ss");
   }
   public static String toNiceString(String string) {
      string = ChatColor.stripColor(string).replace('_', ' ').toLowerCase();

      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < string.toCharArray().length; i++) {
         char c = string.toCharArray()[i];
         if (i > 0) {
            char prev = string.toCharArray()[i - 1];
            if (prev == ' ' || prev == '[' || prev == '(') {
               if (i == string.toCharArray().length - 1 || c != 'x' ||
                       !Character.isDigit(string.toCharArray()[i + 1])) {
                  c = Character.toUpperCase(c);
               }
            }
         } else {
            if (c != 'x' || !Character.isDigit(string.toCharArray()[i + 1])) {
               c = Character.toUpperCase(c);
            }
         }
         sb.append(c);
      }

      return sb.toString();
   }
   public static String niceBuilder(Collection<String> collection, String color) {
      return niceBuilder(collection, color + ", ", color + " and ", color + '.');
   }
   public static String[] niceLore(String text, ChatColor color) {
      int dif = 32;
      String first = String.valueOf(color), second = String.valueOf(color), third = String.valueOf(color);

      if(!(text.length() > dif)) {
         return new String[] { color + text };
      }

      if(text.length() > dif * 2) {
         first += text.substring(0, dif - 1);
         second += text.substring(dif, dif * 2);
         third += text.substring(dif * 2, text.length());

         return new String[] { first, second, third };
      } else if(text.length() > dif) {
         first += text.substring(0, dif - 1);
         second += text.substring(dif - 1, text.length());

         return new String[] { first, second };
      }

      return new String[0];
   }
   public static String niceBuilder(Collection<String> collection, String delimiter, String and, String dot) {
      if (collection != null && !collection.isEmpty()) {
         List<String> contents = new ArrayList(collection);
         String last = null;
         if (contents.size() > 1) {
            last = (String)contents.remove(contents.size() - 1);
         }

         StringBuilder builder = new StringBuilder();

         String name;
         for(Iterator iterator = contents.iterator(); iterator.hasNext(); builder.append(name)) {
            name = (String)iterator.next();
            if (builder.length() > 0) {
               builder.append(delimiter);
            }
         }

         if (last != null) {
            builder.append(and).append(last);
         }

         return builder.append(dot != null ? dot : "").toString();
      } else {
         return "";
      }
   }

   public static int generateRandomNumber(int min, int max) {
      return min + (int)(Math.random() * (double)(max - min + 1));
   }

   static {
      NO_PERMISSION = CC.RED + "İzinin yok.";
      FOR_PLAYER_ONLY = CC.RED + "Bu komutu yalnızca oyuncular gerçekleştirebilir.";
      NO_PLAYER_FOUND = CC.RED + "'<player>' adında bir oyuncu bulunamadı.";
      INTEGER_NOT_VALID = CC.RED + "<source> isn't a valid number.";
      LOAD_ERROR_2 = CC.RED + "Error found while loading your data. (2)\n\nTry again later or contact a staff member.";
   }
}
