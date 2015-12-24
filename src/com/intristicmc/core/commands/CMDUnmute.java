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

public class CMDUnmute implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("unmute")) {
			if(!sender.hasPermission("intristicmc.core.unmute")) {
				Utils.sendNoPermissionMessage(sender, null, "intristicmc.core.unmute");
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
				if(permMuteSet.next()) {
					MySQLHandler.returnStatement().executeUpdate("UPDATE `muted_players` SET `is_pardoned` = '1' WHERE `id` = '" + permMuteSet.getInt("id") + "'");
					Utils.broadcastToStaff("&c" + sender.getName() + " &8unmuted &c" + name + "!");
					MessageManager.sendSenderMessage(Bukkit.getPlayer(name), "&cYou were unmuted by " + sender.getName() + "!");
					return true;
				}
				ResultSet tempMuteSet = MySQLHandler.returnStatement().executeQuery("SELECT * FROM tempmutes WHERE uuid = '" + uuid + "'");
				if(tempMuteSet.next()) {
					MySQLHandler.returnStatement().executeUpdate("UPDATE `tempmutes` SET `is_pardoned` = '1' WHERE `id` = '" + tempMuteSet.getInt("id") + "'");
					Utils.broadcastToStaff("&c" + sender.getName() + " &8unmuted &c" + name + "!");
					MessageManager.sendSenderMessage(Bukkit.getPlayer(name), "&cYou were unmuted by " + sender.getName() + "!");
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
