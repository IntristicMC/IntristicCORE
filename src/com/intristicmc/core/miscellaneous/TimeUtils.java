package com.intristicmc.core.miscellaneous;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

	public static String calculateTime(Long timeIn){
      int day = (int) TimeUnit.MILLISECONDS.toDays(timeIn);
      long hours = TimeUnit.MILLISECONDS.toHours(timeIn) - (day * 24);
      long minutes = TimeUnit.MILLISECONDS.toMinutes(timeIn) - (TimeUnit.MILLISECONDS.toHours(timeIn) * 60);
      long seconds = TimeUnit.MILLISECONDS.toSeconds(timeIn) - (TimeUnit.MILLISECONDS.toMinutes(timeIn) * 60);
 
      String time = String.valueOf(day) + " Days " + String.valueOf(hours) + " Hours " +
    		  String.valueOf(minutes) + " Minutes " + String.valueOf(seconds) + " Seconds.";
 
      return time;
    }
}
