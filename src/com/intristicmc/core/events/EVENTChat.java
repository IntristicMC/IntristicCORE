package com.intristicmc.core.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;

public class EVENTChat implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		MySQLHandler.connect();
		try {
			ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM muted_players WHERE username = '" + e.getPlayer().getName() + "'");
			if(rs.next()) {
				e.setCancelled(true);
				MessageManager.sendPlayerMessage(e.getPlayer(), "&cYou are muted!");
				return;
			} else {
				e.setFormat("<" + e.getPlayer().getName() + "> " + e.getMessage());
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			MySQLHandler.closeStatement();
			MySQLHandler.closeConnection();
		}
	}
}
