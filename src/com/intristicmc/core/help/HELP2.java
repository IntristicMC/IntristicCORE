package com.intristicmc.core.help;

import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;

public class HELP2 {

	public static void helpPage2(Player p) {
		MessageManager.sendMessage(false, p, "&8&m--------&r &cIntristic&4MC &cCommands &8&m--------");
		MessageManager.sendMessage(false, p, "&4mute&7: &c/mute [player]");
		MessageManager.sendMessage(false, p, "&4nick&7: &c/nick <nickname> [player]");
		MessageManager.sendMessage(false, p, "&4ping&7: &c/ping [player]");
		MessageManager.sendMessage(false, p, "&4reply&7: &c/reply <message>");
		MessageManager.sendMessage(false, p, "&4report&7: &c/report <player> <reason>");
		MessageManager.sendMessage(false, p, "&4seen&7: &c/seen <player>");
	}
}
