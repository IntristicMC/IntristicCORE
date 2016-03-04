package com.intristicmc.core.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.IntristicCORE;
import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.Utils;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CMDMsg implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("msg")) {
			if(!(sender instanceof Player)) {
				MessageManager.sendMessage(true, sender, "&cYou must be an in-game player to run this command!");
				return true;
			}
			Player p = (Player) sender;
			sender = null;
			
			if(!p.hasPermission("intristicmc.core.message")) {
				Utils.sendNoPermissionMessage(p, "intristicmc.core.message");
				return true;
			}
			
			String message = null;
			if(args.length == 0 || args.length == 1) {
				MessageManager.sendMessage(true, p, "&7Incorrect usage for this command! &cUsage: /" + label + " <player> <message>");
				return true;
			} else if(args.length >= 2) {
				StringBuilder messageSB = new StringBuilder();
				for(int i = 1; i < args.length; i++) {
					if(i == args.length) {
						messageSB.append(args[i]);
					} else {
						messageSB.append(args[i]).append(" ");
					}
				}
				message = messageSB.toString();
			}
			
			HashMap<String, String> replyMap = IntristicCORE.replyMap;
			// Key is sender | Value is receiver.
			
			if(Bukkit.getPlayer(args[0]) == null) {
				MessageManager.sendMessage(true, p, "&c" + args[0] + " is not online!");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			String targetPrefix = PermissionsEx.getUser(target).getPrefix();
			String senderPrefix = PermissionsEx.getUser(p).getPrefix();
			
			MessageManager.sendMessage(false, p, senderPrefix + "You &8&l-> &r" + targetPrefix + target.getName() + "&r&8:&r " + message);
			MessageManager.sendMessage(false, target, senderPrefix + p.getName() + " &8&l-> &r" + targetPrefix + "You&r&8:&r " + message);
			
			replyMap.put(p.getName(), target.getName());
			replyMap.put(target.getName(), p.getName());
		}
		return true;
	}
}
