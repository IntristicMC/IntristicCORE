package com.intristicmc.core;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.intristicmc.core.commands.CMDBan;
import com.intristicmc.core.commands.CMDChatColour;
import com.intristicmc.core.commands.CMDChatmode;
import com.intristicmc.core.commands.CMDClearChat;
import com.intristicmc.core.commands.CMDFly;
import com.intristicmc.core.commands.CMDHistory;
import com.intristicmc.core.commands.CMDKick;
import com.intristicmc.core.commands.CMDMaintenance;
import com.intristicmc.core.commands.CMDMsg;
import com.intristicmc.core.commands.CMDMute;
import com.intristicmc.core.commands.CMDNick;
import com.intristicmc.core.commands.CMDPing;
import com.intristicmc.core.commands.CMDReply;
import com.intristicmc.core.commands.CMDReport;
import com.intristicmc.core.commands.CMDSeen;
import com.intristicmc.core.commands.CMDStaff;
import com.intristicmc.core.commands.CMDTempBan;
import com.intristicmc.core.commands.CMDTempMute;
import com.intristicmc.core.commands.CMDUnban;
import com.intristicmc.core.commands.CMDUnmute;
import com.intristicmc.core.events.EVENTChat;
import com.intristicmc.core.events.EVENTCommandPreProccess;
import com.intristicmc.core.events.EVENTPlayerJoin;
import com.intristicmc.core.events.EVENTPlayerLogon;
import com.intristicmc.core.events.EVENTPlayerQuit;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.Utils;

public class IntristicCORE extends JavaPlugin {
	// Create the replyMap
	public static HashMap<String, String> replyMap = new HashMap<String, String>();
	
	@Override
	public void onEnable() {
		// Connect to the database.
		MySQLHandler.connect();
		// Clear the replyMap
		replyMap.clear();
		
		// Register Events
		Bukkit.getServer().getPluginManager().registerEvents(new EVENTPlayerLogon(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EVENTPlayerQuit(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EVENTPlayerJoin(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EVENTChat(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EVENTCommandPreProccess(), this);
		
		// Set the appropriate CommandExecutors.
		getCommand("chatmode").setExecutor(new CMDChatmode());
		getCommand("ban").setExecutor(new CMDBan());
		getCommand("kick").setExecutor(new CMDKick());
		getCommand("maintenance").setExecutor(new CMDMaintenance());
		getCommand("mute").setExecutor(new CMDMute());
		getCommand("tempban").setExecutor(new CMDTempBan());
		getCommand("tempmute").setExecutor(new CMDTempMute());
		getCommand("unban").setExecutor(new CMDUnban());
		getCommand("unmute").setExecutor(new CMDUnmute());
		getCommand("history").setExecutor(new CMDHistory());
		getCommand("clearchat").setExecutor(new CMDClearChat());
		getCommand("chatcolour").setExecutor(new CMDChatColour());
		getCommand("msg").setExecutor(new CMDMsg());
		getCommand("reply").setExecutor(new CMDReply());
		getCommand("report").setExecutor(new CMDReport());
		getCommand("seen").setExecutor(new CMDSeen());
		getCommand("fly").setExecutor(new CMDFly());
		getCommand("nick").setExecutor(new CMDNick());
		getCommand("ping").setExecutor(new CMDPing());
		getCommand("staff").setExecutor(new CMDStaff());
		
		// Update the TabList for all online users.
		for(Player p : Bukkit.getOnlinePlayers()) {
			Utils.sendPlayerHeaderFooter(p);
		}
	}
	
	public void onDisable() {
		// Close the statement
		MySQLHandler.closeStatement();
		// Close the connection
		MySQLHandler.closeConnection();
		// Clear the replyMap
		replyMap.clear();
	}
}