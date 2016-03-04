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

public class CMDUnmute implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("unmute")) {
			if(!sender.hasPermission("intristicmc.core.unmute")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.unmute");
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
				if(Bukkit.getPlayer(uuid) == null) {
					name = Bukkit.getOfflinePlayer(uuid).getName();
				} else {
					name = Bukkit.getPlayer(uuid).getName();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
				ResultSet permMuteSet = MySQLHandler.returnStatement().executeQuery("SELECT * FROM muted_players WHERE uuid = '" + uuid + "'");
				while(permMuteSet.next()) {
					if(permMuteSet.getInt("is_pardoned") == 0) {
						MySQLHandler.returnStatement().executeUpdate("UPDATE `muted_players` SET `is_pardoned` = '1' WHERE `id` = '" + permMuteSet.getInt("id") + "'");
						MessageManager.broadcastToStaff("&c" + sender.getName() + " &7unmuted &c" + name + "!");
						MessageManager.sendMessage(true, Bukkit.getPlayer(name), "&cYou were unmuted by " + sender.getName() + "!");
						return true;
					}
				}
				ResultSet tempMuteSet = MySQLHandler.returnStatement().executeQuery("SELECT * FROM tempmutes WHERE uuid = '" + uuid + "'");
				while(tempMuteSet.next()) {
					if(tempMuteSet.getInt("is_pardoned") == 0) {
						MySQLHandler.returnStatement().executeUpdate("UPDATE `tempmutes` SET `is_pardoned` = '1' WHERE `id` = '" + tempMuteSet.getInt("id") + "'");
						MessageManager.broadcastToStaff("&c" + sender.getName() + " &7unmuted &c" + name + "!");
						MessageManager.sendMessage(true, Bukkit.getPlayer(name), "&cYou were unmuted by " + sender.getName() + "!");
						return true;
					}
				}
				MessageManager.sendMessage(true, sender, "&c" + args[0] + " isn't muted!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
