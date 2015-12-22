package com.intristicmc.core.miscellaneous;

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
        String s = "&8[&cIntristic&4CORE&8]";
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }
	
	public static void sendNoPermissionMessage(CommandSender sender, Player p, String node) {
        String s = getPrefix() + " &cYou're lacking the permission node: " + node;
        s = ChatColor.translateAlternateColorCodes('&', s);
        if(sender != null) {
        	sender.sendMessage(s);
        } else if(p != null) {
        	p.sendMessage(s);
        }
    }
	
	public static void broadcastToStaff(String m) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.hasPermission("intristicmc.core.viewstaffbroadcast")) {
				m = ChatColor.translateAlternateColorCodes('&', m);
				MessageManager.sendPlayerMessage(p, m);
			}
		}
	}
}
