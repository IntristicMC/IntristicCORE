package com.intristicmc.core.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.Utils;

public class CMDBan implements CommandExecutor {
	@SuppressWarnings({"deprecation" })
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ban")) {
			if(!sender.hasPermission("intristicmc.core.ban")) {
				MessageManager.sendSenderMessage(sender, Utils.getNoPermissionMessage("intristicmc.core.ban"));
				return true;
			}
			
			// Check all the arguments and get/set the reason.
			String reason = "";
			if(args.length == 0) {
				MessageManager.sendSenderMessage(sender, Utils.getPrefix() + " Usage: /" + label + " <player> <reason>");
			} else if(args.length == 1) {
				reason = "You have been banned!";
			} else if(args.length >= 2) {
				StringBuilder reasonSb = new StringBuilder();
				for(int i = 1; i < args.length; i++) {
					reasonSb.append(args[i]);
				}
				reason = reasonSb.toString();
			}
			
			// Get the player and assign it to the correct variable.
			OfflinePlayer targetOffline = null;
			Player targetOnline = null;
			if(Bukkit.getPlayer(args[0]) == null) {
				if(Bukkit.getOfflinePlayers().toString().contains(args[0])) {
					targetOffline = Bukkit.getOfflinePlayer(args[0]);
				}
			} else {
				targetOnline = Bukkit.getPlayer(args[0]);
			}
			
			// Kick and ban the player.
			if(targetOffline != null && targetOnline == null) {
				MySQLHandler.connect();
				try {
					MySQLHandler.returnStatement().executeQuery("INSERT INTO `bans`(username, reason) VALUES ('" + targetOffline.getName() + "', '" + reason + "'");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if(targetOnline != null && targetOffline == null) {
				try {
					MySQLHandler.returnStatement().executeQuery("INSERT INTO `bans`(username, reason) VALUES ('" + targetOnline.getName() + "', '" + reason + "'");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
