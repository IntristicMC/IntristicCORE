package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.UUIDFetcher;
import com.intristicmc.core.miscellaneous.Utils;

public class CMDUnban implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("unban")) {
			if(!sender.hasPermission("intristicmc.core.unban")) {
				Utils.sendNoPermissionMessage(sender, null);
				return true;
			}
			
			if(args.length == 0 || args.length > 1) {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " <player>");
				return true;
			}
			
			UUID uuid = null;
			String name = null;
			try {
				uuid = UUIDFetcher.getUUIDOf(args[0]);
				name = Bukkit.getOfflinePlayer(uuid).getName();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
				ResultSet permBanSet = MySQLHandler.returnStatement().executeQuery("SELECT * FROM bans WHERE uuid = '" + uuid + "'");
				while(permBanSet.next()) {
					if(permBanSet.getInt("is_pardoned") == 0) {
						MySQLHandler.returnStatement().executeUpdate("UPDATE `bans` SET `is_pardoned` = '1' WHERE `id` = '" + permBanSet.getInt("id") + "'");
						MessageManager.broadcastToStaff("&c" + sender.getName() + " &7unbanned &c" + name + "!");
						return true;
					}
				}
				ResultSet tempBanSet = MySQLHandler.returnStatement().executeQuery("SELECT * FROM tempbans WHERE uuid = '" + uuid + "'");
				while(tempBanSet.next()) {
					if(tempBanSet.getInt("is_pardoned") == 0) {
						MySQLHandler.returnStatement().executeUpdate("UPDATE `tempbans` SET `is_pardoned` = '1' WHERE `id` = '" + tempBanSet.getInt("id") + "'");
						MessageManager.broadcastToStaff("&c" + sender.getName() + " &7unbanned &c" + name + "!");
						return true;
					}
				}
				MessageManager.sendMessage(true, sender, "&c" + name + " isn't banned!");
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
