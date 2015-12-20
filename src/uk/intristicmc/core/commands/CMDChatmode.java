package uk.intristicmc.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CMDChatmode implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd,	String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("chatmode")) {
			
		}
		return true;
	}
}
