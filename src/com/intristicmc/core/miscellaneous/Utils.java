package com.intristicmc.core.miscellaneous;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Utils {
	public static Plugin getInstance() {
		return Bukkit.getPluginManager().getPlugin("IntristicCORE");
	}
	
	public static String getPrefix() {
        String s = "&8[&cIntristic&4CORE&8] &r";
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }
	
	public static void sendNoPermissionMessage(CommandSender sender, String node) {
        String s = getPrefix() + "&cYou're lacking the permission node: " + node;
        s = ChatColor.translateAlternateColorCodes('&', s);
        if(sender != null) {
        	sender.sendMessage(s);
        }
    }
	
	public static int getPlayerPing(Player player) {
		try {
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer");
			Object converted = craftPlayer.cast(player);
			Method handle = converted.getClass().getMethod("getHandle", new Class[0]);
			Object entityPlayer = handle.invoke(converted, new Object[0]);
			Field pingField = entityPlayer.getClass().getField("ping");
			return pingField.getInt(entityPlayer);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}
	
	public static String getServerVersion()
	  {
	    Pattern brand = Pattern.compile("(v|)[0-9][_.][0-9][_.][R0-9]*");
	    String pkg = Bukkit.getServer().getClass().getPackage().getName();
	    String version = pkg.substring(pkg.lastIndexOf('.') + 1);
	    if (!brand.matcher(version).matches()) {
	      version = "";
	    }
	    return version;
	  }
	
	public static void sendPlayerHeaderFooter(Player player) {
		String header = "&c&lYou are playing on &r&cIntristic&4MC&c&l!";
		String footer = "&cplay.intristicmc.com";
		TabListChanger.setHeaderAndFooter(player, ChatColor.translateAlternateColorCodes('&', header), ChatColor.translateAlternateColorCodes('&', footer));
	}
}
