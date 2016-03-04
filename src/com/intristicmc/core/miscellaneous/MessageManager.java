package com.intristicmc.core.miscellaneous;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MessageManager {

	public static void sendMessage(boolean prefix, CommandSender sender, String m) {
		m = ChatColor.translateAlternateColorCodes('&', m);
		if(prefix) {
			sender.sendMessage(Utils.getPrefix() + " " + m);
		} else {
			sender.sendMessage(m);
		}
	}
	
	public static void sendServerBroadcast(String m) {
		Bukkit.getServer().broadcastMessage(Utils.getPrefix() + " " + m);
	}
	
	public static void broadcastToStaff(String m) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.hasPermission("intristicmc.core.viewstaffbroadcast")) {
				m = ChatColor.translateAlternateColorCodes('&', m);
				MessageManager.sendMessage(true, p, m);
			}
		}
	}
}
