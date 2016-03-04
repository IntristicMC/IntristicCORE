package com.intristicmc.core.miscellaneous;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabListChanger {

	public static boolean setHeaderAndFooter(Player player, String header, String footer){
	    try{
	        //Version
	    	String v = split(Bukkit.getServer().getClass().getName(), '.')[3];
	   
	    	//Create chatComponents
	    	Object cc1 = Class.forName("net.minecraft.server."+v+".ChatComponentText").getConstructor(new Class[]{String.class}).newInstance(header);
	    	Object cc2 = Class.forName("net.minecraft.server."+v+".ChatComponentText").getConstructor(new Class[]{String.class}).newInstance(footer);
	   
	    	//Create packet
	    	Object p = Class.forName("net.minecraft.server."+v+".PacketPlayOutPlayerListHeaderFooter").newInstance();
	    	Field a = p.getClass().getDeclaredField("a");
	    	a.setAccessible(true);
	    	a.set(p, cc1);
	    	Field b = p.getClass().getDeclaredField("b");
	    	b.setAccessible(true);
	    	b.set(p, cc2);
	   
	    	//Send packet
	    	Object cp = player.getClass().getDeclaredMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
	    	Object pc = cp.getClass().getDeclaredField("playerConnection").get(cp);
	    	pc.getClass().getDeclaredMethod("sendPacket", new Class[]{Class.forName("net.minecraft.server."+v+".Packet")}).invoke(pc, new Object[]{p});
	        return true;
	   } catch(Exception e){
		   e.printStackTrace();
	       return false;
	   }
	}
	
	/**
	 * Splits the string every time the given
	 * character has been found
	 * @param string
	 * @return
	 */
	private static String[] split(String string, char c){
	        if(countMatches(string, c) <= 0)return new String[]{string};
	       
	        String s = string + c;
	        String[] sa = new String[countMatches(string, c)+1];
	        StringBuilder sb = new StringBuilder();
	        char[] ca = s.toCharArray();
	        int n = 0, x;
	       
	        for(x = 0; x < ca.length; x ++){
	                if(ca[x] == c){
	                        sa[n] = sb.toString();
	                        sb = new StringBuilder();
	                        n++;
	                }else{
	                        sb.append(ca[x]);
	                }
	        }
	       
	        return sa;
	}
	
	/**
	 * Counts the amount of times a certain
	 * character is part of a string
	 * @param string
	 * @return
	 */
	private static int countMatches(String string, char c){
	        int n = 0;
	        for(char tc : string.toCharArray()){
	                if(c == tc)n++;
	        }
	        return n;
	}
}
