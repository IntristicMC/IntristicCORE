package com.intristicmc.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.intristicmc.core.miscellaneous.Utils;

public class CMDBan implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ban")) {
			if(!sender.hasPermission("intristicmc.core.ban")) {
				sender.sendMessage(Utils.getNoPermissionMessage("intristicmc.core.ban"));
				return true;
			}
		}
		return true;
	}
}
