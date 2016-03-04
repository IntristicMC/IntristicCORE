package com.intristicmc.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.Utils;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CMDPing implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ping")) {
			if(!sender.hasPermission("intristicmc.core.ping")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.ping");
				return true;
			}
			
			if(args.length == 0) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					MessageManager.sendMessage(true, sender, "&7Your ping is: " + Utils.getPlayerPing(p) + "ms!");
					return true;
				} else {
					MessageManager.sendMessage(true, sender, "&cYou must specify a player!");
					return true;
				}
			} else if(args.length == 1) {
				if(!sender.hasPermission("intristicmc.core.ping.others")) {
					Utils.sendNoPermissionMessage(sender, "intristicmc.core.ping.others");
					return true;
				}
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					MessageManager.sendMessage(true, sender, "&c" + args[0] + " is not online!");
					return true;
				}
				CraftPlayer cp = (CraftPlayer) target;
				EntityPlayer ep = cp.getHandle();
				String targetPrefix = PermissionsEx.getUser(target).getPrefix();
				MessageManager.sendMessage(true, sender, targetPrefix + target.getName() + "&r&7's ping is: " + ep.ping + "ms!");
				return true;
			} else {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " [player]");
				return true;
			}
		}
		return true;
	}
}
