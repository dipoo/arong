package org.arong.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class RedisConfigManager {
	private static Map<String, String> config = new HashMap<String, String>();
	
	static{
		Properties prop = new Properties();
		try {
			InputStream is = RedisConfigManager.class.getResourceAsStream("/redis.properties");
			prop.load(is);
			Set<Object> keys = prop.keySet();
			for(Object key : keys){
				config.put(key.toString(), prop.get(key) == null ? null : prop.get(key).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, String> getConfig() {
		return config;
	}
	
	public static String getProperty(String name) {
		return config.get(name);
	}
}
