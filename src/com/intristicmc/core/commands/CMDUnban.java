package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.evilmidget38.UUIDFetcher;
import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.Utils;

public class CMDUnban implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("unban")) {
			if(!sender.hasPermission("intristicmc.core.unban")) {
				Utils.sendNoPermissionMessage(sender, null, "intristicmc.core.unban");
				return true;
			}
			
			if(args.length == 0 || args.length > 1) {
				MessageManager.sendSenderMessage(sender, "&cUsage: /" + label + " <player>");
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
				if(permBanSet.next()) {
					MySQLHandler.returnStatement().executeUpdate("UPDATE `bams` SET `is_pardoned` = '1' WHERE `id` = '" + permBanSet.getInt("id") + "'");
					Utils.broadcastToStaff("&c" + sender.getName() + " &8unbanned &c" + name + "!");
					return true;
				}
				ResultSet tempBanSet = MySQLHandler.returnStatement().executeQuery("SELECT * FROM tempbans WHERE uuid = '" + uuid + "'");
				if(tempBanSet.next()) {
					MySQLHandler.returnStatement().executeUpdate("UPDATE `tempbans` SET `is_pardoned` = '1' WHERE `id` = '" + tempBanSet.getInt("id") + "'");
					Utils.broadcastToStaff("&c" + sender.getName() + " &8unbanned &c" + name + "!");
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
