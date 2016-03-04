package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.UUIDFetcher;
import com.intristicmc.core.miscellaneous.Utils;
import com.intristicmc.core.miscellaneous.temputils.DateUtil;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CMDSeen implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("seen")) {
			if(!sender.hasPermission("intristicmc.core.seen")) {
				Utils.sendNoPermissionMessage(sender, "intristicmc.core.seen");
				return true;
			}
			if(args.length == 0 || args.length > 1) {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " <player>");
				return true;
			}
			
			String targetUuid = UUIDFetcher.getUUIDOf(args[0]).toString();
			UUID targetUUID = UUIDFetcher.getUUIDOf(args[0]);
			Player target = Bukkit.getPlayer(targetUUID);
			if(target != null) {
				String targetPrefix = PermissionsEx.getUser(target.getName()).getPrefix();
				MessageManager.sendMessage(true, sender, "&7" + targetPrefix + target.getName() + " &r&7is currently &aonline&7!");
				return true;
			}
			try {
				ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM login WHERE uuid = '" + targetUuid + "'");
				if(rs.next()) {
					long lastSeen = rs.getLong("lastlogin");
					String lastSeenString = DateUtil.formatDateDiff(lastSeen);
					MessageManager.sendMessage(true, sender, "&7" + args[0] + " was last seen " + lastSeenString + " ago!");
					return true;
				} else {
					MessageManager.sendMessage(true, sender, "&c" + args[0] + " has never joined this server!");
					return true;
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}