package com.intristicmc.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.UUIDFetcher;
import com.intristicmc.core.miscellaneous.Utils;
import com.intristicmc.core.miscellaneous.temputils.DateUtil;

public class CMDHistory implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("history")) {
			if(!sender.hasPermission("intristicmc.core.viewhistory")) {
				Utils.sendNoPermissionMessage(sender, null);
				return true;
			}
			
			if(args.length == 0 || args.length > 1) {
				MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command. &cUsage: /" + label + " <player>");
				return true;
			}
			
			UUID uuid = null;
			String name = null;
			try {
				if(UUIDFetcher.getUUIDOf(args[0]) == null) {
					MessageManager.sendMessage(true, sender, "&c" + args[0] + " could not be found!");
					return true;
				} else {
					uuid = UUIDFetcher.getUUIDOf(args[0]);
				}
				if(Bukkit.getPlayer(uuid) == null) {
					name = Bukkit.getOfflinePlayer(uuid).getName();
				} else {
					name = Bukkit.getPlayer(uuid).getName();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			ResultSet rs = null;
			boolean banFound = false;
			boolean kickFound = false;
			boolean muteFound = false;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm");
			Calendar cal = Calendar.getInstance();
			
			MessageManager.sendMessage(false, sender, "&7&m-------&r " + name + "&c's Punishment History &r&7&m-------");
			try {
				// Permanent bans
				rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM bans WHERE uuid = '" + uuid + "'");
				while(rs.next()) {
					banFound = true;
					String reason = rs.getString("reason");
					String punisher = rs.getString("punisher");
					long dateOfBanMillis = rs.getLong("dateOfBan");
					cal.setTimeInMillis(dateOfBanMillis);
					String dateOfBanString = sdf.format(cal.getTime());
					if(rs.getInt("is_pardoned") == 1) {
						if(sender.hasPermission("intristicmc.core.viewhistory.punisher")) {
							MessageManager.sendMessage(false, sender, "&a&m" + dateOfBanString + " &l- BAN - &r&a&m " + punisher + " " + reason + " permanent");
						} else {
							MessageManager.sendMessage(false, sender, "&a&m" + dateOfBanString + " &l- BAN - &r&a&m "  + reason + " permanent");
						}
					} else {
						if(sender.hasPermission("intristicmc.core.viewhistory.punisher")) {
							MessageManager.sendMessage(false, sender, "&c" + dateOfBanString + " &7&l&m-&r &c&lBAN &7&m-&r " + punisher + " &7&m-&r " + reason + " &7&m-&r &c&lDuration: &r&cpermanent");
						} else {
							MessageManager.sendMessage(false, sender, "&c" + dateOfBanString + " &7&l&m-&r &c&lBAN &7&m-&r " + reason + " &7&m-&r &c&lDuration: &r&cpermanent");
						}
					}
				}
				
				// Kicks
				rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM kicks WHERE uuid = '" + uuid + "'");
				while(rs.next()) {
					kickFound = true;
					String reason = rs.getString("reason");
					String punisher = rs.getString("punisher");
					long dateOfKickMillis = rs.getLong("dateOfKick");
					cal.setTimeInMillis(dateOfKickMillis);
					String dateOfKickString = sdf.format(cal.getTime());
					if(sender.hasPermission("intristicmc.core.viewhistory.punisher")) {
						MessageManager.sendMessage(false, sender, "&c" + dateOfKickString + " &7&l&m-&r &c&lKICK &7&m-&r " + punisher + " &7&m-&r " + reason);
					} else {
						MessageManager.sendMessage(false, sender, "&c" + dateOfKickString + " &7&l&m-&r &c&lKICK &7&m-&r " + reason);
					}
				}
				
				// Permanent mutes
				rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM muted_players WHERE uuid = '" + uuid + "'");
				while(rs.next()) {
					muteFound = true;
					String reason = rs.getString("reason");
					String punisher = rs.getString("punisher");
					long dateOfMuteMillis = rs.getLong("dateOfMute");
					cal.setTimeInMillis(dateOfMuteMillis);
					String dateOfMuteString = sdf.format(cal.getTime());
					if(rs.getInt("is_pardoned") == 1) {
						if(sender.hasPermission("intristicmc.core.viewhistory.punisher")) {
							MessageManager.sendMessage(false, sender, "&a&m" + dateOfMuteString + " &a&m-&r&a&m &lMUTE&r&a&m - " + punisher + " &a&m- " + reason + " &a&m- &lDuration: &r&a&mpermanent");
						} else {
							MessageManager.sendMessage(false, sender, "&a&m" + dateOfMuteString + " &a&m-&r&a&m &lMUTE&r&a&m &a&m- " + reason + " &a&m- &lDuration: &r&a&mpermanent");
						}
					} else {
						if(sender.hasPermission("intristicmc.core.viewhistory.punisher")) {
							MessageManager.sendMessage(false, sender, "&c" + dateOfMuteString + " &7&l&m-&r &c&lMUTE &7&m-&r " + punisher + " &7&m-&r " + reason + " &7&m-&r &c&lDuration: &r&cpermanent");
						} else {
							MessageManager.sendMessage(false, sender, "&c" + dateOfMuteString + " &7&l&m-&r &c&lMUTE &7&m-&r " + reason + " &7&m-&r &c&lDuration: &r&cpermanent");
						}
					}
				}
				
				// Temporary bans
				rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM tempbans WHERE uuid = '" + uuid + "'");
				while(rs.next()) {
					banFound = true;
					String reason = rs.getString("reason");
					String punisher = rs.getString("punisher");
					long dateOfBanMillis = rs.getLong("dateOfBan");
					long endOfBanMillis = rs.getLong("endOfBan");
					cal.setTimeInMillis(dateOfBanMillis);
					String dateOfBanString = sdf.format(cal.getTime());
					if(rs.getInt("is_pardoned") == 1) {
						if(sender.hasPermission("intristicmc.core.viewhistory.punisher")) {
							MessageManager.sendMessage(false, sender, "&a&m" + dateOfBanString + " &a&m-&r &a&lBAN &a&m-&r &a" + punisher + " &a&m-&r " + reason + " &a&m-&r &a&lDuration: &r&a" + DateUtil.getTempDiff(dateOfBanMillis, endOfBanMillis));
						} else {
							MessageManager.sendMessage(false, sender, "&a&m" + dateOfBanString + " &a&m-&r &a&lBAN &a&m-&r &a" + reason + " &c&l&m-&r &a&lDuration: &r&a" + DateUtil.getTempDiff(dateOfBanMillis, endOfBanMillis));
						}
					} else {
						if(sender.hasPermission("intristicmc.core.viewhistory.punisher")) {
							MessageManager.sendMessage(false, sender, "&c" + dateOfBanString + " &c&l&m-&r &c&lBAN &c&l&m-&r &c" + reason + " &c&l&m-&r &c&lDuration: &r&c" + DateUtil.getTempDiff(dateOfBanMillis, endOfBanMillis));
						} else {
							MessageManager.sendMessage(false, sender, "&c" + dateOfBanString + " &c&l&m-&r &c&lBAN &c&l&m-&r &c" + punisher + " &c&l&m-&r " + reason + " &c&l&m-&r &c&lDuration: &r&c" + DateUtil.getTempDiff(dateOfBanMillis, endOfBanMillis));
						}
					}
				}
				
				// Temporary mutes
				rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM tempmutes WHERE uuid = '" + uuid + "'");
				while(rs.next()) {
					muteFound = true;
					String reason = rs.getString("reason");
					String punisher = rs.getString("punisher");
					long dateOfMuteMillis = rs.getLong("dateOfMute");
					long endOfMuteMillis = rs.getLong("endOfMute");
					cal.setTimeInMillis(dateOfMuteMillis);
					String dateOfMuteString = sdf.format(cal.getTime());
					if(rs.getInt("is_pardoned") == 1) {
						if(sender.hasPermission("intristicmc.core.viewhistory.punisher")) {
							MessageManager.sendMessage(false, sender, "&a&m" + dateOfMuteString + " &a&m-&r &a&l&mMUTE &a&m-&r &a&m" + punisher + " &a&m-&r " + reason + " &a&m-&r &a&m&lDuration: &r&a&m" + DateUtil.getTempDiff(dateOfMuteMillis, endOfMuteMillis));
						} else {
							MessageManager.sendMessage(false, sender, "&a&m" + dateOfMuteString + " &a&m-&r &a&l&mMUTE &a&m-&r &a&m" + reason + " &c&l&m-&r &a&m&lDuration: &r&a&m" + DateUtil.getTempDiff(dateOfMuteMillis, endOfMuteMillis));
						}
					} else {
						if(sender.hasPermission("intristicmc.core.viewhistory.punisher")) {
							MessageManager.sendMessage(false, sender, "&c" + dateOfMuteString + " &c&l&m-&r &c&lMUTE &c&l&m-&r &c" + reason + " &c&l&m-&r &c&lDuration: &r&c" + DateUtil.getTempDiff(dateOfMuteMillis, endOfMuteMillis));
						} else {
							MessageManager.sendMessage(false, sender, "&c" + dateOfMuteString + " &c&l&m-&r &c&lMUTE &c&l&m-&r &c" + punisher + " &c&l&m-&r " + reason + " &c&l&m-&r &c&lDuration: &r&c" + DateUtil.getTempDiff(dateOfMuteMillis, endOfMuteMillis));
						}
					}
				}
				
				if(!banFound && !kickFound && !muteFound) {
					MessageManager.sendMessage(false, sender, "&cNo punishment data found for " + name);
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
