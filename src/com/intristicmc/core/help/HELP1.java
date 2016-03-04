package com.intristicmc.core.help;

import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;

public class HELP1 {

	public static void helpPage1(Player p, boolean showHeader) {
		MessageManager.sendMessage(false, p, "&8&m--------&r &cIntristic&4MC &cCommands &8&m--------");
		if(showHeader) {
			MessageManager.sendMessage(false, p, "&7Type /help [page number] to view more commands!");
		}
		MessageManager.sendMessage(false, p, "&4ban&7: &c/ban <player> [reason]");
		MessageManager.sendMessage(false, p, "&4chatmode&7: &c/chatmode <restricted|normal>");
		MessageManager.sendMessage(false, p, "&4fly&7: &c/fly [player]");
		MessageManager.sendMessage(false, p, "&4kick&7: &c/kick <player> [reason]");
		MessageManager.sendMessage(false, p, "&4maintenance&7: &c/maintenance&7");
		MessageManager.sendMessage(false, p, "&4msg&7: &c/msg <player> <message>");
	}
}
