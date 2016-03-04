package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.UUIDFetcher;
import com.intristicmc.core.miscellaneous.Utils;

public class CMDMute implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("mute")) {
			// Check if the player has the permission, if they don't, tell them they don't
			if(!sender.hasPermission("intristicmc.core.mute")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.mute");
			}
			
			// Check the arguments and get reason
			String reason = "";
			if(args.length == 0) {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command. &cUsage: /" + label + " <player> [reason]");
				return true;
			} else if(args.length == 1) {
				reason = "You have been muted by a staff member!";
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
			
			
			// Get the player
			Player target = null;
			OfflinePlayer offlineTarget = null;
			UUID uuid = null;
			try {
				if(UUIDFetcher.getUUIDOf(args[0]) == null) {
					MessageManager.sendMessage(true, sender, "&c" + args[0] + " could not be identified!");
					return true;
				} else {
					uuid = UUIDFetcher.getUUIDOf(args[0]);
					if(Bukkit.getPlayer(uuid) == null) {
						offlineTarget = Bukkit.getOfflinePlayer(uuid);
					} else {
						target = Bukkit.getPlayer(uuid);
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
				boolean alreadyBanned = false;
				if(target != null) {
					ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM muted_players WHERE uuid = '" + target.getUniqueId() + "'");
					while(rs.next()) {
						if(rs.getInt("is_pardoned") == 0) {
							alreadyBanned = true;
						}
					}
				} else if(offlineTarget != null) {
					ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM muted_players WHERE uuid = '" + offlineTarget.getUniqueId() + "'");
					alreadyBanned = false;
					while(rs.next()) {
						if(rs.getInt("is_pardoned") == 0) {
							alreadyBanned = true;
						}
					}
				}
				if(!alreadyBanned) {
					String sql = "INSERT INTO muted_players (`dateOfMute`, `username`, `uuid`, `punisher`, `reason`) VALUES ('" + System.currentTimeMillis() + "', '" + target.getName() + "', '" + target.getUniqueId() + "', '" + sender.getName() + "', '" + reason + "')";
					MySQLHandler.returnStatement().executeUpdate(sql);
					MessageManager.broadcastToStaff(sender.getName() + " muted " + target.getName() + " for \"" + reason + "\"!");
					MessageManager.sendMessage(true, target, sender.getName() + " muted &4you &cfor \"" + reason + "\"!");
					return true;
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
