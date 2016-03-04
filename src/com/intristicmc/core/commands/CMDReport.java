package com.intristicmc.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CMDReport implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("report")) {
			if(args.length == 0 || args.length == 1) {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " <player> <reason>");
				return true;
			}
			
			StringBuilder sb = new StringBuilder();
			for(int i = 1; i < args.length; i++) {
				if(i == args.length - 1) {
					sb.append(args[i]);
				} else {
					sb.append(args[i]).append(" ");
				}
			}
			String reason = sb.toString();
			
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				MessageManager.sendMessage(true, sender, "&c" + args[0] + " is not online!");
				return true;
			}
			
			String reporterPrefix = PermissionsEx.getUser(sender.getName()).getPrefix();
			String targetPrefix = PermissionsEx.getUser(target).getPrefix();
			MessageManager.broadcastToStaff(reporterPrefix + sender.getName() + " &r&7reported " + targetPrefix + target.getName() + " &r&7for reason: \"&c" + reason + "&7\"");
			MessageManager.sendMessage(true, sender, "&7Thanks for your report on player: " + targetPrefix + target.getName() + " &7!");
			return true;
		}
		return true;
	}
}
