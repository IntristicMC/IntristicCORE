package com.intristicmc.core.miscellaneous;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageManager {
	public static void sendPlayerMessage(Player p, String m) {
		p.sendMessage(m);
	}
	
	public static void sendServerBroadcast(String m) {
		Bukkit.getServer().broadcastMessage(m);
	}
}
