package com.dcgames.pillars.util;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	public static final long PERMANENT = Long.MAX_VALUE;
	private static final ThreadLocal<DecimalFormat> SECONDS = ThreadLocal.withInitial(() -> new DecimalFormat("0.#"));
	private static final ThreadLocal<DecimalFormat> TRAILING = ThreadLocal.withInitial(() -> new DecimalFormat("0"));

	public static String formatDuration(long input) {
		return DurationFormatUtils.formatDurationWords(input, true, true);
	}

	public static long getDuration(String input) {
		input = input.toLowerCase();
		if (Character.isLetter(input.charAt(0))) {
			return Long.MAX_VALUE;
		} else {
			long result = 0L;
			StringBuilder number = new StringBuilder();

			for(int i = 0; i < input.length(); ++i) {
				char c = input.charAt(i);
				if (Character.isDigit(c)) {
					number.append(c);
				} else {
					String str = number.toString();
					if (Character.isLetter(c) && !str.isEmpty()) {
						result += convert(Integer.parseInt(str), c);
						number = new StringBuilder();
					}
				}
			}

			return result;
		}
	}

	private static long convert(int value, char charType) {
		switch(charType) {
			case 'M':
				return (long)value * TimeUnit.DAYS.toMillis(30L);
			case 'd':
				return (long)value * TimeUnit.DAYS.toMillis(1L);
			case 'h':
				return (long)value * TimeUnit.HOURS.toMillis(1L);
			case 'm':
				return (long)value * TimeUnit.MINUTES.toMillis(1L);
			case 's':
				return (long)value * TimeUnit.SECONDS.toMillis(1L);
			case 'w':
				return (long)value * TimeUnit.DAYS.toMillis(7L);
			case 'y':
				return (long)value * TimeUnit.DAYS.toMillis(365L);
			default:
				return -1L;
		}
	}

	public static String niceTime(int i) {
		int r = i * 1000;
		int sec = r / 1000 % 60;
		int min = r / '\uea60' % 60;
		int h = r / 3600000 % 24;
		return (h > 0 ? (h < 10 ? "0" : "") + h + ":" : "") + (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec);
	}

	public static String niceTime(long millis, boolean milliseconds) {
		return niceTime(millis, milliseconds, true);
	}

	public static String niceTime(long duration, boolean milliseconds, boolean trail) {
		return milliseconds && duration < TimeUnit.MINUTES.toMillis(1L) ? ((DecimalFormat)(trail ? TRAILING : SECONDS).get()).format((double)duration * 0.001D) + 's' : DurationFormatUtils.formatDuration(duration, (duration >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");
	}
}
