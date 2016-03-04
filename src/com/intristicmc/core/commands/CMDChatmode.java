package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.Utils;

public class CMDChatmode implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd,	String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("chatmode")) {
			if(!sender.hasPermission("intristicmc.core.chatmode")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.chatmode");
				return true;
			}
			if(args.length == 0) {
				if(!sender.hasPermission("intristicmc.core.chatmode.view")) {
					Utils.sendNoPermissionMessage(sender, "intristicmc.core.chatmode.view");
					return true;
				}
				try {
					ResultSet restrictedRS = MySQLHandler.returnStatement().executeQuery("SELECT * FROM main WHERE chatmode = 'restricted'");
					if(restrictedRS.next()) {
						MessageManager.sendMessage(true, sender, "&7The chatmode is currently set to &crestricted &7mode!");
						return true;
					}
					ResultSet normalRS = MySQLHandler.returnStatement().executeQuery("SELECT * FROM main WHERE chatmode = 'normal'");
					if(normalRS.next()) {
						MessageManager.sendMessage(true, sender, "&7The chatmode is currently in &anormal &7mode!");
						return true;
					}
				} catch(SQLException e) {
					e.printStackTrace();
					MessageManager.sendMessage(true, sender, "&cThere was an error during the execution of your request. Please check console.");
					return true;
				}
			} else if(args.length > 1) {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " <restricted|normal>");
				return true;
			}
			if(args[0].equalsIgnoreCase("restricted")) {
				if(!sender.hasPermission("intristicmc.core.chatmode.restricted")) {
					Utils.sendNoPermissionMessage(sender, "intristicmc.core.chatmode.restricted");
					return true;
				}
				try {
					MySQLHandler.returnStatement().executeUpdate("UPDATE main SET chatmode = 'restricted'");
					MessageManager.sendMessage(true, sender, "&7You successfully set the chatmode to &crestricted &7!");
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
					MessageManager.sendMessage(true, sender, "&cThere was an error during the execution of your request. Please check console.");
					return true;
				}
			} else if(args[0].equalsIgnoreCase("normal")) {
				if(!sender.hasPermission("intristicmc.core.chatmode.normal")) {
					Utils.sendNoPermissionMessage(sender, "intristicmc.core.chatmode.normal");
					return true;
				}
				try {
					MySQLHandler.returnStatement().executeUpdate("UPDATE main SET chatmode = 'normal'");
					MessageManager.sendMessage(true, sender, "&7You successfully set the chatmode to &anormal &7!");
					return true;
				} catch(SQLException e) {
					e.printStackTrace();
					MessageManager.sendMessage(true, sender, "&cThere was an error during the execution of your request. Please check console.");
					return true;
				}
			} else {
				MessageManager.sendMessage(true, sender, "&7Unknown option specified. &cUsage: /" + label + " <restricted|normal>");
				return true;
			}
		}
		return true;
	}
}
