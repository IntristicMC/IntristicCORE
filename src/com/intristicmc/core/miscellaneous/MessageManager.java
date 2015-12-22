package com.intristicmc.core.miscellaneous;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MessageManager {
	public static void sendPlayerMessage(Player p, String m) {
		m = ChatColor.translateAlternateColorCodes('&', m);
		p.sendMessage(Utils.getPrefix() + " " + m);
	}
	
	public static void sendSenderMessage(CommandSender sender, String m) {
		m = ChatColor.translateAlternateColorCodes('&', m);
		sender.sendMessage(Utils.getPrefix() + " " + m);
	}
	
	public static void sendServerBroadcast(String m) {
		Bukkit.getServer().broadcastMessage(Utils.getPrefix() + " " + m);
	}
}
