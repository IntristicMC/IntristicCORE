package com.intristicmc.core.events;

import java.sql.ResultSet;
import java.sql.SQLException;

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