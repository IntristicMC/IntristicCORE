package com.intristicmc.core.miscellaneous;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.simple.parser.JSONParser;

import com.eclipsesource.json.JsonObject;

public class NameFetcher {

	JSONParser parser = new JSONParser();
	Object object = null;
	@SuppressWarnings("deprecation")
	public static String getNameFrom(String uuid) {
		String name = null;
		try {
			HttpURLConnection connection = connect(name);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));;
			String data = in.readLine();
			JsonObject jsonObject = JsonObject.readFrom(data);
			if(jsonObject.get("uuid").equals(null)) {
				return null;
			} else {
				name = jsonObject.get("name").toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public static HttpURLConnection connect(String username) {
		try {
			byte[] data = username.getBytes(Charset.forName("UTF-8"));
			int dataLength = data.length;
			String request = "http://mcuuid.com/api/" + username.toLowerCase();
			URL url = new URL(request);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "UTF-8");
			connection.setRequestProperty("Content-Length", String.valueOf(dataLength));
			
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(data);
			out.flush();
			out.close();
			
			return connection;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
