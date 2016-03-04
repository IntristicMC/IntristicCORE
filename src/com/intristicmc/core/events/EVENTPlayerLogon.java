package com.intristicmc.core.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.temputils.DateUtil;

public class EVENTPlayerLogon implements Listener {
	
	@EventHandler
	public void onPlayerLogon(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		
		MySQLHandler.connect();
		
		try {
			ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM login WHERE uuid = '" + p.getUniqueId() + "'");
			if(rs.next()) {
				MySQLHandler.returnStatement().executeUpdate("UPDATE login SET username = '" + p.getName() + "', lastlogin = '" + System.currentTimeMillis() + "' WHERE uuid = '" + p.getUniqueId() + "'");
			} else {
				String insertSql = "INSERT INTO `login` (`username`, `uuid`, `lastlogin`) VALUES ('" + p.getName() + "', '" + p.getUniqueId() + "', '" + System.currentTimeMillis() + "');";
				MySQLHandler.returnStatement().executeUpdate(insertSql);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM main");
			if(rs.next()) {
				if(rs.getString("maintenance").equalsIgnoreCase("on") && !e.getPlayer().hasPermission("intristicmc.core.maintenance.bypass")) {
					String message = (
							"&4Intristic&cMC &7is currently in &4maintenance &7mode! \n" +
							"&7Please try again later."
							);
					e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', message));
					return;
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		if(Bukkit.getServer().hasWhitelist()) {
			String message = (
					"&7You are not whitelisted on &4Intristic&cMC&7! \n" +
					"&7Please try again later."
					);
			if(!e.getPlayer().isWhitelisted()) {
				e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', message));
				return;
			}
		}
		
		String permBanSql = "SELECT * FROM bans WHERE uuid = '" + p.getUniqueId() + "'";
		String tempBanSql = "SELECT * FROM tempbans WHERE uuid = '" + p.getUniqueId() + "'";
		try {
			ResultSet permBanSet = MySQLHandler.returnStatement().executeQuery(permBanSql);
			while(permBanSet.next()) {
				String reason = permBanSet.getString("reason");
				int is_pardoned = permBanSet.getInt("is_pardoned");
				if(is_pardoned == 1) {
					e.allow();
					permBanSet.close();
					return;
				} else {
					String kickMessage = (
							"&7You are &4banned &7from IntristicMC. \n" +
							"&7Ban Expiry: &cnever&7. \n" +
							"&7Reason: &c" + reason
							);
					e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', kickMessage));
					permBanSet.close();
					return;
				}
			}
			ResultSet tempBanSet = MySQLHandler.returnStatement().executeQuery(tempBanSql);
			while(tempBanSet.next()) {
				String reason = tempBanSet.getString("reason");
				long endOfBanMillis = tempBanSet.getLong("endOfBan");
				String endOfBan = DateUtil.formatDateDiff(endOfBanMillis);
				int is_pardoned = tempBanSet.getInt("is_pardoned");
				if(is_pardoned == 1) {
					e.allow();
					tempBanSet.close();
					return;
				} else {
					if(System.currentTimeMillis() > endOfBanMillis) {
						e.allow();
						tempBanSet.close();
						return;
					} else if(System.currentTimeMillis() < endOfBanMillis) {
						String kickMessage = (
								"&7You are &4banned &7from IntristicMC. \n" +
								"&7Your ban will expire in &c" + endOfBan + "&7." +
								"&7Reason: " + reason
								);
						e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', kickMessage));
						tempBanSet.close();
						return;
					}
					return;
				}
			}
			e.allow();
			return;
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
}