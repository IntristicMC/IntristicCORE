package com.intristicmc.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.intristicmc.core.commands.CMDBan;
import com.intristicmc.core.commands.CMDChatmode;
import com.intristicmc.core.commands.CMDKick;
import com.intristicmc.core.commands.CMDMute;
import com.intristicmc.core.events.EVENTChat;
import com.intristicmc.core.events.EVENTPlayerLogon;

public class IntristicCORE extends JavaPlugin {
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new EVENTPlayerLogon(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EVENTChat(), this);
		getCommand("chatmode").setExecutor(new CMDChatmode());
		getCommand("ban").setExecutor(new CMDBan());
		getCommand("kick").setExecutor(new CMDKick());
		getCommand("mute").setExecutor(new CMDMute());
	}
}