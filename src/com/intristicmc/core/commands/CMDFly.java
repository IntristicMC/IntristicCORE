package com.intristicmc.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.Utils;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CMDFly implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("fly")) {
			if(!sender.hasPermission("intristicmc.core.fly")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.fly");
				return true;
			}
			
			if(args.length == 0) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(p.getAllowFlight()) {
						p.setAllowFlight(false);
						MessageManager.sendPlayerMessage(true, p, "&7You &cdisabled &7fly mode!");
						return true;
					} else {
						p.setAllowFlight(true);
						MessageManager.sendPlayerMessage(true, p, "&7You &aenabled &7fly mode!");
						return true;
					}
				} else {
					MessageManager.sendSenderMessage(true, sender, "&cYou must specify a player to toggle fly mode for!");
					return true;
				}
			} else if(args.length == 1) {
				if(!sender.hasPermission("intristicmc.core.fly.others")) {
					Utils.sendNoPermissionMessage(sender, "intristicmc.core.fly.others");
					return true;
				}
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					MessageManager.sendSenderMessage(true, sender, "&c" + args[0] + " isn't online!");
					return true;
				}
				if(target.getAllowFlight()) {
					target.setAllowFlight(false);
					String targetPrefix = PermissionsEx.getUser(target).getPrefix();
					String senderPrefix = PermissionsEx.getUser(sender.getName()).getPrefix();
					MessageManager.sendSenderMessage(true, sender, "&7You &cdisabled &7fly mode for " + targetPrefix + target.getName() + "&7!");
					MessageManager.sendPlayerMessage(true, target, senderPrefix + sender.getName() + " &cdisabled &7fly mode for " + targetPrefix + " you");
					return true;
				} else {
					target.setAllowFlight(true);
					String targetPrefix = PermissionsEx.getUser(target).getPrefix();
					String senderPrefix = PermissionsEx.getUser(sender.getName()).getPrefix();
					MessageManager.sendSenderMessage(true, sender, "&7You &aenabled &7fly mode for " + targetPrefix + target.getName() + "&7!");
					MessageManager.sendPlayerMessage(true, target, senderPrefix + sender.getName() + " &r&7changed your nickname to " + args[0]);
					return true;
				}
			} else {
				MessageManager.sendSenderMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " [player]");
				return true;
			}
		}
		return true;
	}
}
