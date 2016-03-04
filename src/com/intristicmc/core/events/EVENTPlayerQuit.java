package com.intristicmc.core.events;

import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.intristicmc.core.IntristicCORE;
import com.intristicmc.core.miscellaneous.MySQLHandler;

public class EVENTPlayerQuit implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage(null);
		HashMap<String, String> replyMap = IntristicCORE.replyMap;
		
		if(replyMap.containsKey(p.getName()) || replyMap.containsValue(p.getName())) {
			replyMap.remove(p.getName());
		}
		
		try {
			MySQLHandler.returnStatement().executeUpdate("UPDATE login SET lastlogin = '" + System.currentTimeMillis() + "' WHERE uuid = '" + e.getPlayer().getUniqueId() + "'");
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
}
