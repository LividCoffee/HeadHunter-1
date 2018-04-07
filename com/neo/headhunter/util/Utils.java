package com.neo.headhunter.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.text.DecimalFormat;
import java.util.Random;

public final class Utils {
	public static final String  CFG =   "config.yml";
	public static final String  DAT =   "database.db";
	public static final String  MSG =   "messages.yml";
	public static final String  MOB =   "mobhunter.yml";
	public static final String  MDB =   "mhdb.yml";
	public static final String  SGN =   "signs.yml";
	public static final String  TMP =   "temp.yml";
	
	private static final String DELIM = ":";
	
	private static final DecimalFormat MONEY = new DecimalFormat("0.00");
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	
	public static String f(String source, String hunter, String target, double value, int amount) {
		source = source.replace("{HUNTER}", hunter);
		source = source.replace("{TARGET}", target);
		source = source.replace("{VALUE}", Utils.toMoney(value));
		source = source.replace("{AMOUNT}", String.valueOf(amount));
		return Utils.color(source);
	}
	
	public static String parseLocation(Location loc) {
		String result = loc.getWorld().toString();
		result += (DELIM + loc.getBlockX());
		result += (DELIM + loc.getBlockY());
		result += (DELIM + loc.getBlockZ());
		return result;
	}
	
	public static Location readLocation(String tag) {
		if(tag == null || tag.isEmpty())
			return null;
		String[] parts = tag.split(DELIM);
		if (parts.length == 4) {
			World world = Bukkit.getWorld(parts[0]);
			if (world == null)
				return null;
			try {
				double x = Double.valueOf(parts[1]);
				double y = Double.valueOf(parts[2]);
				double z = Double.valueOf(parts[3]);
				return new Location(world, x, y, z);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public static boolean isPluginEnabled(String plugin) {
		return Bukkit.getPluginManager().isPluginEnabled(plugin);
	}
	
	public static double round(double d) {
		return Math.round(d * 100.0) / 100.0;
	}
	
	public static String toMoney(double value) {
		return MONEY.format(value);
	}
	
	public static String color(String toColor) {
		return ChatColor.translateAlternateColorCodes('&', toColor);
	}
	
	public static String strip(String toStrip) {
		return ChatColor.stripColor(toStrip);
	}
	
	public static boolean isMoney(String num) {
		return num.matches("[1-9][0-9]*[.][0-9]{2}");
	}
	
	public static boolean isPercentage(String num) {
		return num.matches("[1-9][0-9]*([.][0-9]*)?[%]");
	}
	
	public static boolean isInteger(String num) {
		return num.matches("[0-9]+");
	}
	
	public static boolean isDecimal(String num) {
		return num.matches("[0-9]*[.][0-9]{1,2}") || num.matches("[0-9]+[.][0-9]{0,2}");
	}
	
	public static boolean isNumeric(String num) {
		return isInteger(num) || isDecimal(num);
	}
	
	public static Random RANDOM() {
		return RANDOM;
	}
}
