package com.intristicmc.core.miscellaneous;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;

public class Utils {
	public static Plugin getInstance() {
		return Bukkit.getPluginManager().getPlugin("IntristicCORE");
	}
	
	public static String getPrefix() {
		String s = "&8[&cIntristic&4CORE&8]";
		s = ChatColor.translateAlternateColorCodes('&', s);
		return s;
	}
	
	public static String getNoPermissionMessage(String node) {
		String s = getPrefix() + "&cYou're lacking the permission node: " + node;
		s = ChatColor.translateAlternateColorCodes('&', s);
		return s;
	}
}
