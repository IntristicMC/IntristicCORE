package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.Utils;

public class CMDMaintenance implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("maintenance")) {
			if(!sender.hasPermission("intristicmc.core.maintenace")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.maintenance");
				return true;
			}
			try {
				ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM main");
				if(rs.next()) {
					if(rs.getString("maintenance").equalsIgnoreCase("off")) {
						MySQLHandler.returnStatement().executeUpdate("UPDATE main SET maintenance = 'on'");
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(!p.hasPermission("intristicmc.core.maintenance.bypass")) {
								String kickMessage = (
										"&7You were kicked from &cIntristic&4MC. \n" +
										"&7Reason: &cIntristic&4MC &7just entered maintenance mode."
										);
								p.kickPlayer(ChatColor.translateAlternateColorCodes('&', kickMessage));
							}
						}
						MessageManager.sendMessage(true, sender, "&7You toggled maintenance mode to &aon&7!");
						MessageManager.sendMessage(true, sender, "&7All players who are not allowed to be on the server whilst maintenance mode is active have been kicked!");
						return true;
					} else if(rs.getString("maintenance").equalsIgnoreCase("on")) {
						MySQLHandler.returnStatement().executeUpdate("UPDATE main SET maintenance = 'off'");
						MessageManager.sendMessage(true, sender, "&7You toggled maintenance mode to &coff&7!");
						return true;
					}
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
