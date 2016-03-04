package com.intristicmc.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.Utils;

public class CMDClearChat implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("clearchat")) {
			if(!sender.hasPermission("intristicmc.core.clearchat")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.clearchat");
				return true;
			}
			
			if(args.length == 0) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					for(int i = 0; i < 101; i++) {
						MessageManager.sendMessage(false, p, "");
					}
				}
			} else if(args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					MessageManager.sendMessage(true, sender, "&c" + args[0] + " is not online!");
					return true;
				}
				
				for(int i = 0; i < 101; i++) {
					MessageManager.sendMessage(false, target, "");
				}
			} else if(args.length > 1) {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " [player]");
				return true;
			}
		}
		return true;
	}
}
