package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.Utils;

public class CMDBan implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ban")) {
			if(!sender.hasPermission("intristicmc.core.ban")) {
				Utils.sendNoPermissionMessage(sender, null, "intristicmc.core.ban");
				return true;
			}
			
			// Check all the arguments and get/set the reason.
			String reason = "";
			if(args.length == 0) {
				MessageManager.sendSenderMessage(sender, "Usage: /" + label + " <player> [reason]");
			} else if(args.length == 1) {
				reason = "You have been banned!";
			} else if(args.length >= 2) {
				StringBuilder reasonSb = new StringBuilder();
				for(int i = 1; i < args.length; i++) {
					if(i == args.length - 1) {
						reasonSb.append(args[i]);
					} else {
						reasonSb.append(args[i] + " ");
					}
				}
				reason = StringEscapeUtils.escapeJava(reasonSb.toString());
			}
			
			// Get the player and assign it to the correct variable.
			OfflinePlayer targetOffline = null;
			Player targetOnline = null;
			if(Bukkit.getPlayer(args[0]) != null) {
				targetOnline = Bukkit.getPlayer(args[0]);
			} else {
				targetOffline = Bukkit.getOfflinePlayer(args[0]);
			}
			
			// Kick and ban the player.
			if(targetOffline != null && targetOnline == null) {
				try {
					String checkSql = "SELECT * FROM bans WHERE uuid = '" + targetOffline.getUniqueId() + "'";
					ResultSet rs = MySQLHandler.returnStatement().executeQuery(checkSql);
					if(rs.next() && rs.getInt("is_pardoned") == 0) {
						MessageManager.sendSenderMessage(sender, "&c" + targetOffline.getName() + " is already banned!");
						return true;
					} else if(rs.next() && rs.getInt("is_pardoned") == 1) {
						String sql = "INSERT INTO bans (username, uuid, reason, is_pardoned) VALUES ('" + targetOffline.getName() + "', '" + targetOffline.getUniqueId() + "', '" + reason + "', 0)";
						MySQLHandler.returnStatement().executeUpdate(sql);
						Utils.broadcastToStaff("&a" + sender.getName() + " banned " + targetOffline.getName() + " for \"" + reason + "\"");
						return true;
					} else if(!rs.next()){
						String sql = "INSERT INTO bans (username, uuid, reason, is_pardoned) VALUES ('" + targetOffline.getName() + "', '" + targetOffline.getUniqueId() + "', '" + reason + "', 0)";
						MySQLHandler.returnStatement().executeUpdate(sql);
						Utils.broadcastToStaff("&a" + sender.getName() + " banned " + targetOffline.getName() + " for \"" + reason + "\"");
						return true;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return true;
				}
			} else if(targetOnline != null && targetOffline == null) {
				MySQLHandler.connect();
				try {
					String checkSql = "SELECT * FROM bans WHERE uuid = '" + targetOnline.getUniqueId() + "'";
					ResultSet rs = MySQLHandler.returnStatement().executeQuery(checkSql);
					if(rs.next()) {
						MessageManager.sendSenderMessage(sender, "&c" + targetOnline.getName() + " is already banned!");
						return true;
					} else {
						String sql = "INSERT INTO bans (username, uuid, reason, is_pardoned) VALUES ('" + targetOnline.getName() + "', '" + targetOnline.getUniqueId() + "', '" + reason + "', 0)";
						MySQLHandler.returnStatement().executeUpdate(sql);
						targetOnline.kickPlayer(ChatColor.RED + "You have been banned for:\n\"" + reason + "\"");
						Utils.broadcastToStaff("&a" + sender.getName() + " banned " + targetOnline.getName() + " for \"" + reason + "\"");
						return true;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return true;
				}
			}
		}
		return true;
	}
}
