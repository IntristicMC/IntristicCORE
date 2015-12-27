package com.intristicmc.core.commands;

import java.sql.SQLException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.Utils;

import net.md_5.bungee.api.ChatColor;

public class CMDKick implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("kick")) {
			// Check if the player/sender has the correct permission.
			if(!sender.hasPermission("intristicmc.core.kick")) {
				Utils.sendNoPermissionMessage(sender, null);
				return true;
			}
			
			// Get and set the reason.
			String reason = "";
			if(args.length == 0) {
				MessageManager.sendSenderMessage(true, sender, "&7Incorrect usage for this command. &cUsage: /" + label + " <player> [reason]");
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
				MessageManager.sendSenderMessage(true, sender, "&c" + args[0] + " is not online!");
				return true;
			}
			String kickMessage = (
					"&7You were &4kicked &7from IntristicMC. \n" +
					"&7Reason: &c" + reason
					);
			target.kickPlayer(ChatColor.translateAlternateColorCodes('&', kickMessage));
			Utils.broadcastToStaff("&c" + target.getName() + " 78was kicked by &c" + sender.getName() + " &8for \"&c" + reason + "&8\"!");
			try {
				MySQLHandler.returnStatement().executeUpdate("INSERT INTO kicks(dateOfKick, username, uuid, punisher, reason) VALUES ('" + System.currentTimeMillis() + "', '" + target.getName() + "', '" + target.getUniqueId() + "', '" + sender.getName() + "', '" + reason + "')");
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
		return true;
	}
}
