package com.intristicmc.core.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.intristicmc.core.miscellaneous.MySQLHandler;

public class EVENTPlayerLogon implements Listener {

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		MySQLHandler.connect();
		try {
			String sql = "SELECT * FROM bans WHERE uuid = '" + e.getPlayer().getUniqueId() + "'";
			ResultSet rs = MySQLHandler.returnStatement().executeQuery(sql);
			if(rs.next()) {
				String reason = rs.getString("reason");
				if(rs.getLong("time") != 0) {
					long time = rs.getLong("time");
					long current = System.currentTimeMillis();
					long endOfBan = time + current;
					if(endOfBan > current) {
						MySQLHandler.returnStatement().executeUpdate("DELETE FROM bans WHERE id = " + rs.getInt("id"));
						return;
					} else {
						String seconds = String.valueOf(time * 1000);
						String minutes = String.valueOf(Long.parseLong(seconds) * 60);
						String hours = String.valueOf(Long.parseLong(minutes) * 60);
						String days = String.valueOf(Long.parseLong(hours) * 24);
						String date = days + " days, " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds"; 
						e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You have been banned for:\n\"" + reason + "\"!\nYou have " + date + " left!");
						return;
					}
				}
				e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You have been banned for:\n\"" + reason + "\"!");
			} else {
				return;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			MySQLHandler.closeStatement();
			MySQLHandler.closeConnection();
		}
	}
}
