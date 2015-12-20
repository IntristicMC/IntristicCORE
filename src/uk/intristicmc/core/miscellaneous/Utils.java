package uk.intristicmc.core.miscellaneous;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Utils {

	public static Plugin getInstance() {
		return Bukkit.getPluginManager().getPlugin("IntristicCORE");
	}
}
