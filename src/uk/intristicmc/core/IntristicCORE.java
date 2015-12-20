package uk.intristicmc.core;

import org.bukkit.plugin.java.JavaPlugin;

import uk.intristicmc.core.commands.CMDChatmode;

public class IntristicCORE extends JavaPlugin {
	public void onEnable() {
		getCommand("chatmode").setExecutor(new CMDChatmode());
	}
}