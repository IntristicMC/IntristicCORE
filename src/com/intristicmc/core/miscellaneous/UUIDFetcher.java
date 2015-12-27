package com.intristicmc.core.miscellaneous;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.simple.parser.JSONParser;

import com.eclipsesource.json.JsonObject;

public class UUIDFetcher {

	JSONParser parser = new JSONParser();
	Object object = null;
	@SuppressWarnings("deprecation")
	public static UUID getUUIDOf(String name) {
		String uuid = null;
		try {
			HttpURLConnection connection = connect(name);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));;
			String data = in.readLine();
			JsonObject jsonObject = JsonObject.readFrom(data);
			if(jsonObject.get("uuid").equals("null")) {
				return null;
			} else {
				String plain = jsonObject.get("uuid").asString();
				byte[] uuiddata = Hex.decodeHex(plain.toCharArray());
				uuid = new UUID(ByteBuffer.wrap(uuiddata, 0, 8).getLong(), ByteBuffer.wrap(uuiddata, 8, 8).getLong()).toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return UUID.fromString(uuid);
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
