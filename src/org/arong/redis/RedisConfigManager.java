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
	
	public static String getString(String name, String... defaultValue) {
		return config.get(name) == null && defaultValue != null ? defaultValue[0] : config.get(name);
	}
	public static Integer getInteger(String name, Integer... defaultValue){
		return config.get(name) == null && defaultValue != null  ? defaultValue[0] : Integer.parseInt(config.get(name));
	}
	public static Long getLong(String name, Long... defaultValue){
		return config.get(name) == null && defaultValue != null  ? defaultValue[0] : Long.parseLong(config.get(name));
	}
	public static Boolean getBoolean(String name, Boolean... defaultValue){
		return config.get(name) == null && defaultValue != null  ? defaultValue[0] : Boolean.valueOf(config.get(name));
	}
}
