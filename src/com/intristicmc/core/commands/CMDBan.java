package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.UUIDFetcher;
import com.intristicmc.core.miscellaneous.Utils;

import net.md_5.bungee.api.ChatColor;

public class CMDBan implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ban")) {
			if(!sender.hasPermission("intristicmc.core.ban")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.ban");
				return true;
			}
			
			// Check all the arguments and get/set the reason.
			String reason = "";
			if(args.length == 0) {
				MessageManager.sendSenderMessage(true, sender, "&7Incorrect usage for this command. &cUsage: /" + label + " <player> [reason]");
				return true;
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
			UUID uuid = null;
			String name = null;
			try {
				uuid = UUIDFetcher.getUUIDOf(args[0]);
				if(Bukkit.getPlayer(args[0]) == null) {
					name = Bukkit.getOfflinePlayer(uuid).getName();
				} else {
					name = Bukkit.getPlayer(uuid).getName();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			// Kick and ban the player.
			ResultSet rs = null;
			boolean alreadyBanned = false;
			try {
				rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM bans WHERE uuid = '" + uuid + "'");
				while(rs.next()) {
					if(rs.getInt("is_pardoned") == 0) {
						alreadyBanned = true;
					}
				}
				if(!alreadyBanned) {
					rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM tempbans WHERE uuid = '" + uuid + "'");
					while(rs.next()) {
						if(rs.getInt("is_pardoned") == 0) {
							alreadyBanned = true;
						}
					}
				}
				if(alreadyBanned) {
					MessageManager.sendSenderMessage(true, sender, "&c" + args[0] + " is already banned!");
					return true;
				} else if(!alreadyBanned) {
					String sql = "INSERT INTO bans(dateOfBan, username, uuid, punisher, reason) VALUES (" + System.currentTimeMillis() + ", '" + name + "', '" + uuid + "', '" + sender.getName() + "', '" + reason + "')";
					MySQLHandler.returnStatement().executeUpdate(sql);
					if(Bukkit.getPlayer(name) != null) {
						String kickMessage = (
								"&7You were &4banned &7from IntristicMC. \n" +
								"&7Expiry Date: &cnever \n" +
								"&7Reason: &c" + reason
								);
						Bukkit.getPlayer(name).kickPlayer(ChatColor.translateAlternateColorCodes('&', kickMessage));
					}
					Utils.broadcastToStaff("&7" + sender.getName() + " has &cbanned &7" + name + " for: &c" + reason);
					return true;
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
