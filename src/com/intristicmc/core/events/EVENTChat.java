package com.intristicmc.core.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.temputils.DateUtil;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class EVENTChat implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		try {
			ResultSet chatmoders = MySQLHandler.returnStatement().executeQuery("SELECT * FROM main");
			if(chatmoders.next()) {
				String chatmode = chatmoders.getString("chatmode");
				if(chatmode.equalsIgnoreCase("restricted")) {
					if(!e.getPlayer().hasPermission("intristicmc.core.chatmode.restricted.bypass")) {
						MessageManager.sendMessage(true, e.getPlayer(), "&7You do not have permission to talk whilst the chat is in &crestricted &7mode!");
						e.setCancelled(true);
						return;
					}
				}
			}
			
			ResultSet permMuteSet = MySQLHandler.returnStatement().executeQuery("SELECT * FROM muted_players WHERE uuid = '" + e.getPlayer().getUniqueId() + "'");
			while(permMuteSet.next()) {
				String reason = permMuteSet.getString("reason"); // Get the reason why they are muted.
				int is_pardoned = permMuteSet.getInt("is_pardoned"); // Get the value of "is_pardoned". Will be used later.
				if(is_pardoned != 1) { // Checks if is_pardoned is not equal to 1
					e.setCancelled(true); // Cancels the chat event.
					MessageManager.sendMessage(true, e.getPlayer(), "&cYou are muted. The reason you were muted is because: \"" + reason + "\"!"); // Sends the user a reason why the event was cancelled.
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
						MessageManager.sendMessage(true, e.getPlayer(), "&cYou are muted for " + endOfMute + ". The reason you were muted is because: \"" + reason + "\"!"); // Sends the user a reason why the event was cancelled.
						return; // Stops the code right here...
					}
				}
			}

			String prefix = PermissionsEx.getUser(e.getPlayer()).getPrefix();
			
			String admin = "&4&l";
			String srmod = "&4";
			String dev = "&c&l";
			String mod = "&c";
			
			String chatColor = "";
			ResultSet rs = MySQLHandler.returnStatement().executeQuery("SELECT * FROM login WHERE uuid = '" + e.getPlayer().getUniqueId() + "'");
			if(rs.next()) {
				if(rs.getString("chatcolor") == null || rs.getString("chatcolor").equalsIgnoreCase("")) {
					if(prefix.equalsIgnoreCase(admin) || prefix.equalsIgnoreCase(srmod) || prefix.equalsIgnoreCase(dev) || prefix.equalsIgnoreCase(mod)) {
						chatColor = "&b";
					} else {
						chatColor = "&f";
					}
				} else {
					chatColor = rs.getString("chatcolor");
				}
			}
			
			String name = null;
			if(e.getPlayer().getCustomName() == null) {
				name = e.getPlayer().getName();
			} else {
				name = e.getPlayer().getCustomName();
			}
			if(!e.getPlayer().hasPermission("intristicmc.core.chat.color")) {
				e.setFormat(ChatColor.translateAlternateColorCodes('&', prefix + name + "&r&8: &r" + chatColor) + e.getMessage());
			} else {
				e.setFormat(ChatColor.translateAlternateColorCodes('&', prefix + name + "&r&8: &r" + chatColor + e.getMessage()));				
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
