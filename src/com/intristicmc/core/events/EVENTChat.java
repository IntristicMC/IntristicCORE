package com.intristicmc.core.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.temputils.DateUtil;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class EVENTChat implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		MySQLHandler.connect();
		
		try {
			ResultSet permMuteSet = MySQLHandler.returnStatement().executeQuery("SELECT * FROM muted_players WHERE uuid = '" + e.getPlayer().getUniqueId() + "'");
			while(permMuteSet.next()) {
				String reason = permMuteSet.getString("reason"); // Get the reason why they are muted.
				int is_pardoned = permMuteSet.getInt("is_pardoned"); // Get the value of "is_pardoned". Will be used later.
				if(is_pardoned != 1) { // Checks if is_pardoned is not equal to 1
					e.setCancelled(true); // Cancels the chat event.
					MessageManager.sendPlayerMessage(true, e.getPlayer(), "&cYou are muted. The reason you were muted is because: \"" + reason + "\"!"); // Sends the user a reason why the event was cancelled.
					return; // Stops the code right here...
				}
			}
			ResultSet tempMuteSet = MySQLHandler.returnStatement().executeQuery("SELECT * FROM tempmutes WHERE uuid = '" + e.getPlayer().getUniqueId() + "'");
			while(tempMuteSet.next()) {
				String reason = tempMuteSet.getString("reason"); // Get the reason why they were muted.
				int is_pardoned = tempMuteSet.getInt("is_pardoned"); // Get the value of "is_pardoned". Will be used later.
				long endOfMuteMillis = tempMuteSet.getLong("endOfMute"); // Get the time (in millis) of how long they were muted for.
				String endOfMute = DateUtil.formatDateDiff(endOfMuteMillis); // Format the time between now and the endOfMuteMillis.
				if(is_pardoned != 1) { // Checks if is_pardoned is not equal to 1 and checks if the current time (in mills) is smaller than the endOfMuteMillis.
					if(System.currentTimeMillis() < endOfMuteMillis) {
						e.setCancelled(true); // Cancels the chat event.
						MessageManager.sendPlayerMessage(true, e.getPlayer(), "&cYou are muted for " + endOfMute + ". The reason you were muted is because: \"" + reason + "\"!"); // Sends the user a reason why the event was cancelled.
						return; // Stops the code right here...
					}
				}
			}
			
			
			
			String prefix = PermissionsEx.getUser(e.getPlayer()).getPrefix();
			String name = null;
			if(e.getPlayer().getCustomName() == null) {
				name = e.getPlayer().getName();
			} else {
				name = e.getPlayer().getCustomName();
			}
			e.setFormat(ChatColor.translateAlternateColorCodes('&', prefix + name + "&r&8: &r" + e.getMessage()));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		if(!e.isCancelled()) {
			Player p = e.getPlayer();
			String cmd = e.getMessage().split(" ")[0];
			HelpTopic topic = Bukkit.getServer().getHelpMap().getHelpTopic(cmd);
			if(topic == null) {
				MessageManager.sendPlayerMessage(true, p, "&7Your requested command ('&c" + cmd + "&7') doesn't exist in my memory! \nType \"&c/help&7\" to see a list of available commands.");
				e.setCancelled(true);
			}
		}
	}
}
