package com.intristicmc.core.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.intristicmc.core.miscellaneous.Utils;

public class EVENTPlayerJoin implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Utils.sendPlayerHeaderFooter(e.getPlayer());
		e.setJoinMessage(null);
	}
}
