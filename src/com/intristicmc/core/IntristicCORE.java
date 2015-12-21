package com.intristicmc.core;

import org.bukkit.plugin.java.JavaPlugin;

import com.intristicmc.core.commands.CMDBan;
import com.intristicmc.core.commands.CMDChatmode;

public class IntristicCORE extends JavaPlugin {
	public void onEnable() {
		getCommand("chatmode").setExecutor(new CMDChatmode());
		getCommand("ban").setExecutor(new CMDBan());
	}
}