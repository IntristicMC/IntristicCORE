package com.intristicmc.core.help;

import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;

public class HELP3 {

	public static void helpPage3(Player p) {
		MessageManager.sendMessage(false, p, "&8&m--------&r &cIntristic&4MC &cCommands &8&m--------");
		MessageManager.sendMessage(false, p, "&4staff&7: &c/staff <message>");
		MessageManager.sendMessage(false, p, "&4tempban&7: &c/tempban <player> <#y|month|d|h|minutes|s> [reason]");
		MessageManager.sendMessage(false, p, "&4tempmute&7: &c/tempmute <player> <#y|month|d|h|minutes|s> [reason]");
		MessageManager.sendMessage(false, p, "&4unban&7: &c/unban <player>");
		MessageManager.sendMessage(false, p, "&4unmute&7: &c/unmute <player>");
		MessageManager.sendMessage(false, p, "&4history&7: &c/history <player>");
	}
}
