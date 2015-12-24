package com.intristicmc.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.intristicmc.core.commands.CMDBan;
import com.intristicmc.core.commands.CMDChatmode;
import com.intristicmc.core.commands.CMDKick;
import com.intristicmc.core.commands.CMDMute;
import com.intristicmc.core.commands.CMDTempBan;
import com.intristicmc.core.commands.CMDTempMute;
import com.intristicmc.core.commands.CMDUnban;
import com.intristicmc.core.commands.CMDUnmute;
import com.intristicmc.core.events.EVENTChat;
import com.intristicmc.core.events.EVENTPlayerLogon;
import com.intristicmc.core.miscellaneous.MySQLHandler;

public class IntristicCORE extends JavaPlugin {
	public void onEnable() {
		// Connect to the database.
		MySQLHandler.connect();
		
		// Register Events
		Bukkit.getServer().getPluginManager().registerEvents(new EVENTPlayerLogon(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EVENTChat(), this);
		
		// Set the appropriate CommandExecutors.
		getCommand("chatmode").setExecutor(new CMDChatmode());
		getCommand("ban").setExecutor(new CMDBan());
		getCommand("kick").setExecutor(new CMDKick());
		getCommand("mute").setExecutor(new CMDMute());
		getCommand("tempban").setExecutor(new CMDTempBan());
		getCommand("tempmute").setExecutor(new CMDTempMute());
		getCommand("unban").setExecutor(new CMDUnban());
		getCommand("unmute").setExecutor(new CMDUnmute());
	}
	
	public void onDisable() {
		// Close the statement
		MySQLHandler.closeStatement();
		// Close the connection
		MySQLHandler.closeConnection();
	}
}