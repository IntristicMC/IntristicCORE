package com.intristicmc.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.Utils;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CMDStaff implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("staff")) {
			if(!sender.hasPermission("intristicmc.core.staffchat")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.staffchat");
				return true;
			}
			
			if(args.length == 0) {
				MessageManager.sendSenderMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " <msg>");
				return true;
			}
			String prefix = "&8[&cSTAFF&8]";
			String senderPrefix = PermissionsEx.getUser(sender.getName()).getPrefix();
			
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < args.length; i++) {
				if(i == args.length - 1) {
					sb.append(args[i]);
				} else {
					sb.append(args[i]).append(" ");
				}
			}
			String message = sb.toString();
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.hasPermission("intristicmc.core.staffchat")) {
					MessageManager.sendPlayerMessage(false, p, prefix + " &r" + senderPrefix + sender.getName() + "&r: " + message);
				}
			}
		}
		return true;
	}
}
