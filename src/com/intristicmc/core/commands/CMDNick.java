package com.intristicmc.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.Utils;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CMDNick implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("nick")) {
			if(!sender.hasPermission("intristicmc.core.nick")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.nick");
				return true;
			}
			
			if(args.length == 0 || args.length > 2) {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " <nickname> [player] \n" +
						"&7Note: Colors are not translated for nicknames, they stay as your rank colour(s).");
				return true;
			} else if(args.length == 1) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(args[0].equalsIgnoreCase("off")) {
						p.setCustomName(p.getName());
						MessageManager.sendMessage(true, sender, "&7You changed your nickname to: " + p.getName());
						return true;
					}
					if(args[0].length() > 16) {
						MessageManager.sendMessage(true, sender, "&cNicknames can only be 16 characters or less!");
						return true;
					} else {
						p.setCustomName(args[0]);
						p.setCustomNameVisible(true);
						MessageManager.sendMessage(true, sender, "&7You set your nickname to: " + args[0]);
						return true;
					}
				} else {
					MessageManager.sendMessage(true, sender, "&cYou must either specify a player!");
					return true;
				}
			} else if(args.length == 2) {
				if(!sender.hasPermission("intristicmc.core.nick.others")) {
					Utils.sendNoPermissionMessage(sender, "intristicmc.core.nick.others");
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					MessageManager.sendMessage(true, sender, "&c" + args[1] + " is not online!");
					return true;
				}
				if(args[0].equalsIgnoreCase("off")) {
					target.setCustomName(target.getName());
					String targetPrefix = PermissionsEx.getUser(target).getPrefix();
					String senderPrefix = PermissionsEx.getUser(sender.getName()).getPrefix();
					MessageManager.sendMessage(true, sender, "&7You changed " + targetPrefix + target.getName() + "'s nickname to: " + target.getName());
					MessageManager.sendMessage(true, target, senderPrefix + sender.getName() + " &r&7changed your nickname to: " + target.getName());
					return true;
				}
				if(args[0].length() > 16) {
					MessageManager.sendMessage(true, sender, "&cNicknames can only be 16 characters or less!");
					return true;
				} else {
					target.setCustomName(args[0]);
					target.setCustomNameVisible(true);
					String targetPrefix = PermissionsEx.getUser(target).getPrefix();
					String senderPrefix = PermissionsEx.getUser(sender.getName()).getPrefix();
					MessageManager.sendMessage(true, sender, "&7You set " + targetPrefix + target.getName() + "&r&7's to " + args[0]);
					MessageManager.sendMessage(true, target, senderPrefix + sender.getName() + " &r&7changed your nickname to " + args[0]);
					return true;
				}
			}
		}
		return true;
	}
}
