package com.intristicmc.core.commands;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.intristicmc.core.miscellaneous.MessageManager;
import com.intristicmc.core.miscellaneous.MySQLHandler;
import com.intristicmc.core.miscellaneous.NameFetcher;
import com.intristicmc.core.miscellaneous.UUIDFetcher;
import com.intristicmc.core.miscellaneous.Utils;
import com.intristicmc.core.miscellaneous.temputils.DateUtil;
import com.intristicmc.core.miscellaneous.temputils.NumberUtil;
import com.intristicmc.core.miscellaneous.temputils.StringUtil;
 
public class CMDTempMute implements CommandExecutor {

    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("tempmute")) {
    		if(!sender.hasPermission("intristicmc.core.tempmute")) {
    			Utils.sendNoPermissionMessage(sender, null);
    		}
    		
    		if(args.length < 2) {
    			MessageManager.sendMessage(true, sender, "&7Incorrect usage for this command! &cUsage: /" + label + " <player> <#y|month|d|h|minute|s> [reason]");
    			return true;
    		}
    		
	        long timeStamp = 0L;
	        try {
	            if (!StringUtil.matches(args[1], "\\d{1,}(d|days|day|m|minute|minutes|min|hour|hours|h|month|months|year|years|y|s|seconds|second)")) {
	                MessageManager.sendMessage(true, sender, "�cInvalid time period.");
	                return true;
	            }
	            timeStamp = DateUtil.parseDateDiff(args[1], true);
	        } catch (Exception localException) {
	        }
	 
	        String uuid = null;
	        String name = null;
	        if (Bukkit.getPlayer(args[0]) == null) {
	        	try {
	        		uuid = UUIDFetcher.getUUIDOf(args[0]).toString().toLowerCase();
	        		name = NameFetcher.getNameFrom(uuid);
	        	} catch(Exception e) {
	        		e.printStackTrace();
	        	}
	        } else {
	        	try {
	        		uuid = UUIDFetcher.getUUIDOf(args[0]).toString().toLowerCase();
	        	} catch(Exception e) {
	        		e.printStackTrace();
	        	}
	        }
	        
	        StringBuilder reasonSB = new StringBuilder();
	        if(args.length >= 3) {
		        for (int i = 2; i < args.length; i++) {
		        	if(i == args.length - 1) {
		        		reasonSB.append(args[i]);
		        	} else {
		        		reasonSB.append(args[i]).append(" ");
		        	}
		        }
	        } else {
	        	reasonSB.append("You were tempmuted by a staff member!");
	        }
	        String reason = StringEscapeUtils.escapeJava(reasonSB.toString());
	        
	        MessageManager.sendMessage(true, Bukkit.getPlayer(args[0]), sender.getName() + " muted &4you &cfor " + this.format(args[1]) + " &8for: \"&c" + reason + "&8\"!");	 
	        ResultSet exists = null;
	        try {
	        	exists = MySQLHandler.returnStatement().executeQuery("SELECT * FROM tempmutes WHERE uuid = '" + uuid + "'");
	        	boolean alreadyBanned = false;
		        while(exists.next()) {
		        	if(exists.getInt("is_pardoned") == 0) {
		        		alreadyBanned = true;
		        		MessageManager.sendMessage(true, sender, "&c" + name + " is already banned!");
		        	}
		        }
		        if(!alreadyBanned) {
		        	String sql = "INSERT INTO tempmutes(dateOfMute, username, uuid, endOfMute, punisher, reason) VALUES ('" + System.currentTimeMillis() + "', '" + args[0] + "', '" + uuid + "', " + timeStamp + ", '" + sender.getName() + "', '" + reason + "')";
		        	MySQLHandler.returnStatement().executeUpdate(sql);
		        	MessageManager.broadcastToStaff("�c" + sender.getName() + "�7 temporarily muted �c" + name + " &7for �c" + this.format(args[1]) + " �7for \"�c" + reason.toString().trim() + "�7\".");
		        	return true;
		        }
	        } catch(SQLException e) {
	        	e.printStackTrace();
	        }
    	}
    	return true;
    } 
    public String format(String in) {
        HashMap<String, String> shorts = new HashMap<String, String>();
        shorts.put("d", "Day");
        shorts.put("days", "Day");
        shorts.put("day", "Day");
        shorts.put("m", "Minute");
        shorts.put("minute", "Minute");
        shorts.put("min", "Minute");
        shorts.put("minutes", "Minute");
        shorts.put("month", "Month");
        shorts.put("months", "Month");
        shorts.put("y", "Year");
        shorts.put("year", "Year");
        shorts.put("years", "Year");
        shorts.put("hour", "Hour");
        shorts.put("h", "Hour");
        shorts.put("hours", "Hour");
        shorts.put("s", "Second");
        shorts.put("second", "Second");
        shorts.put("seconds", "Second");
 
        String type = in.replaceAll("\\d{1,}(d|days|day|m|minute|minutes|min|hour|hours|h|month|months|year|years|y|s|seconds|second)", "$1");
        int time = NumberUtil.getInt(in.replaceAll(type, ""));
 
        return time + " " + shorts.get(type.toLowerCase()) + (time > 1 ? "s" : "");
    }
}