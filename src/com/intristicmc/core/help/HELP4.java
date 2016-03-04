package com.intristicmc.core.help;

import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;

public class HELP4 {

	public static void helpPage4(Player p) {
		MessageManager.sendMessage(false, p, "&8&m--------&r &cIntristic&4MC &cCommands &8&m--------");
		MessageManager.sendMessage(false, p, "&4clearchat&7: &c/clearchat [player]");
		MessageManager.sendMessage(false, p, "&4chatcolour&7: &c/chatcolour <colours>");
	}
}
