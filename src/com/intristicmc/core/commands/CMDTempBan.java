package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

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

public class CMDTempBan implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("tempban")) {
			// Check if the player/sender has the correct permissions, tell them if they don't
			if(!sender.hasPermission("intristicmc.core.tempban")) {
				Utils.sendNoPermissionMessage(sender, null, "intristicmc.core.tempban");
			}
			
			// Check the arguments and get reason.
			String reason = "";
			if(args.length == 0 || args.length < 3) {
				MessageManager.sendSenderMessage(sender, "&cUsage: /" + label + " <player> <#> <y|m|d|h|m|s> [reason]");
				return true;
			} else if(args.length == 3) {
				reason = "You have been tempbanned by a staff member!";
			} else if(args.length >= 4) {
				StringBuilder sb = new StringBuilder();
				for(int i = 4; i < args.length; i++) {
					if(i == args.length - 1) {
						sb.append(args[i]);
					} else {
						sb.append(args[i] + " ");
					}
				}
				reason = sb.toString();
			}
			
			// Get ban time.
			long time = 0;
			if(args[2].equalsIgnoreCase("y")) {
				time = TimeUnit.DAYS.toMillis(Long.parseLong("31536000000") * Long.parseLong(args[1]));
			} else if(args[2].equalsIgnoreCase("m")) {
				time = TimeUnit.DAYS.toMillis(Long.parseLong("2592000000") * Long.parseLong(args[1]));
			} else if(args[2].equalsIgnoreCase("d")) {
				time = TimeUnit.DAYS.toMillis(Long.parseLong(args[1]));
			} else if(args[2].equalsIgnoreCase("h")) {
				time = TimeUnit.HOURS.toMillis(Long.parseLong(args[1]));
			} else if(args[2].equalsIgnoreCase("m")) {
				time = TimeUnit.MINUTES.toMillis(Long.parseLong(args[1]));
			} else if(args[2].equalsIgnoreCase("s")) {
				time = TimeUnit.SECONDS.toMillis(Long.parseLong(args[1]));
			}
			
			// Get the player (online/offline)
			Player targetOnline = null;
			OfflinePlayer targetOffline = null;
			if(Bukkit.getPlayer(args[0]) != null) {
				targetOnline = Bukkit.getPlayer(args[0]);
			} else {
				targetOffline = Bukkit.getOfflinePlayer(args[0]);
			}
			
			MySQLHandler.connect();
			if(targetOnline != null && targetOffline == null) {
				try {
					ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM bans WHERE username = '" + targetOnline.getName() + "'");
					if(rs.next()) {
						MessageManager.sendSenderMessage(sender, "&c" + targetOnline.getName() + " is already banned!");
						return true;
					} else {
						String sql = "INSERT INTO bans(username, uuid, timeOfBan, time, reason) VALUES ('" + targetOnline.getName() + "', '" + targetOnline.getUniqueId() + "', " + System.currentTimeMillis() + ", " + time + ", '" + reason + "')";
						MySQLHandler.returnStatement().executeUpdate(sql);
						targetOnline.kickPlayer(ChatColor.RED + "You were temp-banned for " + TimeUnit.DAYS.convert(time, TimeUnit.DAYS) + " for:\n\"" + reason + "\"");
						Utils.broadcastToStaff("&7" + sender.getName() + " banned " + targetOnline.getName() + " for " + String.valueOf(time) + " for: \"" + reason + "\"");
						return true;
					}
				} catch(SQLException e) {
					e.printStackTrace();
				} finally {
					MySQLHandler.closeStatement();
					MySQLHandler.closeConnection();
				}
			} else if(targetOffline != null && targetOnline == null) {
				try {
					ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM bans WHERE username = '" + targetOffline.getName() + "'");
					if(rs.next()) {
						MessageManager.sendSenderMessage(sender, "&c" + targetOffline.getName() + " is already banned!");
						return true;
					} else {
						String sql = "INSERT INTO bans(username, uuid, timeOfBan, time, reason) VALUES ('" + targetOffline.getName() + "', '" + targetOffline.getUniqueId() + "', " + System.currentTimeMillis() + ", " + time + ", '" + reason + "')";
						MySQLHandler.returnStatement().executeUpdate(sql);
						Utils.broadcastToStaff("&7" + sender.getName() + " banned " + targetOffline.getName() + " for " + String.valueOf(time) + " for: \"" + reason + "\"");
						return true;
					}
				} catch(SQLException e) {
					e.printStackTrace();
				} finally {
					MySQLHandler.closeStatement();
					MySQLHandler.closeConnection();
				}
			}
		}
		return true;
	}
}
