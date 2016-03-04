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

public class CMDReply implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("reply")) {
			if(!(sender instanceof Player)) {
				MessageManager.sendMessage(true, sender, "&cYou must be an in-game player to run this command!");
				return true;
			}
			Player p = (Player) sender;
			
			if(!sender.hasPermission("intristicmc.core.message")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.message");
				return true;
			}
			
			String message = null;
			if(args.length == 0) {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " <message>");
			} else if(args.length >= 1) {
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < args.length; i++) {
					if(i == args.length - 1) {
						sb.append(args[i]);
					} else {
						sb.append(args[i]).append(" ");
					}
				}
				message = sb.toString();
			}
			HashMap<String, String> replyMap = IntristicCORE.replyMap;
			if(!replyMap.containsKey(p.getName())) {
				MessageManager.sendMessage(true, p, "&cYou have no-one to reply to!");
				return true;
			}
			Player r = Bukkit.getPlayer(replyMap.get(p.getName()));
			
			String targetPrefix = PermissionsEx.getUser(r).getPrefix();
			String senderPrefix = PermissionsEx.getUser(p).getPrefix();
			
			MessageManager.sendMessage(false, p, senderPrefix + "You &8&l-> &r" + targetPrefix + r.getName() + "&r&8: &r" + message);
			MessageManager.sendMessage(false, r, senderPrefix + p.getName() + " &8&l-> &r" + targetPrefix + "You&r&8: &r" + message);
		}
		return true;
	}
}
