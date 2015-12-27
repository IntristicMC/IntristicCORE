package com.intristicmc.core.events;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.intristicmc.core.IntristicCORE;

public class EVENTPlayerQuit implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Player p = e.getPlayer();
		HashMap<String, String> replyMap = IntristicCORE.replyMap;
		
		if(replyMap.containsKey(p.getName()) || replyMap.containsValue(p.getName())) {
			replyMap.remove(p.getName());
		}
	}
}
