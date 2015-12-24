package com.intristicmc.core.commands;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.Utils;

import net.md_5.bungee.api.ChatColor;

public class CMDKick implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("kick")) {
			// Check if the player/sender has the correct permission.
			if(!sender.hasPermission("intristicmc.core.kick")) {
				Utils.sendNoPermissionMessage(sender, null, "intristicmc.core.kick");
				return true;
			}
			
			// Get and set the reason.
			String reason = "";
			if(args.length == 0) {
				MessageManager.sendSenderMessage(sender, Utils.getPrefix() + " &cUsage: /" + label + " <player> [reason]");
				return true;
			} else if(args.length == 1) {
				reason = "You were kicked by a staff member!";
			} else if(args.length >= 2) {
				StringBuilder sb = new StringBuilder();
				for(int i = 1; i < args.length; i++) {
					if(i == args.length - 1) {
						sb.append(args[i]);
					} else {
						sb.append(args[i] + " ");
					}
				}
				reason = StringEscapeUtils.escapeJava(sb.toString());
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				MessageManager.sendSenderMessage(sender, "&c" + args[0] + " is not online or has never joined the server!");
				return true;
			}
			target.kickPlayer(ChatColor.RED + "You were kicked for:\n\"" + reason + "\"");
			Utils.broadcastToStaff(target.getName() + " was kicked by " + sender.getName() + " for \"" + reason + "\"");
			return true;
		}
		return true;
	}
}
