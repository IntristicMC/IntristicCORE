package com.intristicmc.core.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;

import com.intristicmc.core.help.HELP1;
import com.intristicmc.core.help.HELP2;
import com.intristicmc.core.help.HELP3;
import com.intristicmc.core.help.HELP4;
import com.intristicmc.core.miscellaneous.MessageManager;

public class EVENTCommandPreProccess implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		if(!e.isCancelled()) {
			Player p = e.getPlayer();
			String cmd = e.getMessage().split(" ")[0];
			HelpTopic topic = Bukkit.getServer().getHelpMap().getHelpTopic(cmd);
			if(topic == null) {
				MessageManager.sendMessage(true, p, "&7Your requested command ('&c" + cmd + "&7') doesn't exist in my memory! \nType \"&c/help&7\" to see a list of available commands.");
				e.setCancelled(true);
			} else if(cmd.toString().equalsIgnoreCase("/help") || cmd.toString().equalsIgnoreCase("/?")) {
				if(e.getMessage().contains("1")) {
					HELP1.helpPage1(p, false);
				} else if(e.getMessage().contains("2")) {
					HELP2.helpPage2(p);
				} else if(e.getMessage().contains("3")) {
					HELP3.helpPage3(p);
				} else if(e.getMessage().contains("4")) {
					HELP4.helpPage4(p);
				} else {
					HELP1.helpPage1(p, true);
				}
				e.setCancelled(true);
			}
		}
	}
}
