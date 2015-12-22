package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.Utils;

public class CMDMute implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("mute")) {
			// Check if the player has the permission, if they don't, tell them they don't
			if(!sender.hasPermission("intristicmc.core.mute")) {
				Utils.sendNoPermissionMessage(sender, null, "intristicmc.core.mute");
			}
			
			// Check the arguments and get reason
			String reason = "";
			if(args.length == 0) {
				MessageManager.sendSenderMessage(sender, "&cUsage: /" + label + " <player> [reason]");
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
				reason = sb.toString();
			}
			
			// Get the player
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				MessageManager.sendSenderMessage(sender, "&c" + args[0] + " is not online or has never played on this server!");
				return true;
			}
			MySQLHandler.connect();
			try {
				ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM muted_players WHERE username = '" + target.getName() + "'");
				if(rs.next()) {
					MessageManager.sendSenderMessage(sender, "&c" + target.getName() + " is already muted!");
					return true;
				} else {
					String sql = "INSERT INTO muted_players (`username`, `uuid`, `reason`) VALUES ('" + target.getName() + "', '" + target.getUniqueId() + "', '" + reason + "')";
					MySQLHandler.returnStatement().executeUpdate(sql);
					Utils.broadcastToStaff(sender.getName() + " muted " + target.getName() + " for \"" + reason + "\"!");
				}
			} catch(SQLException e) {
				e.printStackTrace();
			} finally {
				MySQLHandler.closeStatement();
				MySQLHandler.closeConnection();
			}
		}
		return true;
	}
}
