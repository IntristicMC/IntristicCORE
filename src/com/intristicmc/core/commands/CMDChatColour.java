package com.intristicmc.core.commands;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.Utils;

public class CMDChatColour implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("chatcolour")) {
			if(!(sender instanceof Player)) {
				MessageManager.sendMessage(true, sender, "&cYou must be an in-game player to run this command!");
				return true;
			}
			Player p = (Player) sender;
			if(!sender.hasPermission("intristicmc.core.chatcolour")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.chatcolour");
				return true;
			}
			if(args.length == 0 || args.length > 1) {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " <colour(s)>");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("null") || args[0].equalsIgnoreCase("none")) {
				try {
					MySQLHandler.returnStatement().executeUpdate("UPDATE login SET chatcolor = '' WHERE uuid = '" + p.getUniqueId() + "'");
					MessageManager.sendMessage(true, sender, "&cYou returned your chat color back to default!");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				try {
					MySQLHandler.returnStatement().executeUpdate("UPDATE login SET chatcolor = '" + args[0] + "' WHERE uuid = '" + p.getUniqueId() + "'");
					MessageManager.sendMessage(true, sender, "&cYou changed your chat color to: " + args[0] + " &r&c!");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
