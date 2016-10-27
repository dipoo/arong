package org.arong.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author zhangjp 2016/1/4
 */
public class RedisUtil {

    //Redis服务器IP
    private static String HOST = RedisConfigManager.getProperty("redis.ip");

    //Redis的端口号
    private static int PORT = Integer.parseInt(RedisConfigManager.getProperty("redis.port"));

    //访问密码
    private static String PASSWORD = RedisConfigManager.getProperty("redis.password").equals("") ? null 
    		: RedisConfigManager.getProperty("redis.password");

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_TOTAL = Integer.parseInt(RedisConfigManager
    		.getProperty("redis.pool.maxTotal"));

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = Integer.parseInt(RedisConfigManager
    		.getProperty("redis.pool.maxIdle"));

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException;
    private static int MAX_WAIT = Integer.parseInt(RedisConfigManager
    		.getProperty("redis.pool.maxWait"));

    private static int TIMEOUT = 10000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = Boolean.parseBoolean(RedisConfigManager
    		.getProperty("redis.pool.testOnBorrow"));
    private static boolean TEST_ON_RETURN = Boolean.parseBoolean(RedisConfigManager
    		.getProperty("redis.pool.testOnReturn"));

    private static JedisPool jedisPool = null;
   
    /**
     * 初始化Redis连接池
     */
    static {
    	try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            config.setTestOnReturn(TEST_ON_RETURN);
            jedisPool = new JedisPool(config, HOST, PORT, TIMEOUT, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                resource.select(1);
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     */
    public static void close() {
        jedisPool.close();
    }

    public static String setValue(String key, String value) {
        return getJedis().set(key, value);
    }

    public static Long setValue(String key, String field, String value) {
        return getJedis().hset(key, field, value);
    }

    public static String setValue(String key, String value, int seconds) {
    	Jedis jedis = getJedis();
        String result = jedis.set(key, value);
        jedis.expire(key, seconds);

        return result;
    }

    public static String setValueAtTime(String key, String value, long unixTime) {
    	Jedis jedis = getJedis();
    	String result = jedis.set(key, value);
        jedis.expireAt(key, unixTime);

        return result;
    }

    public static String setObject(String key, Serializable object) {
        String result = getJedis().set(key.getBytes(), SerializationUtils.serialize(object));

        return result;
    }

    public static String setObject(String key, Serializable object, int seconds) {
    	Jedis jedis = getJedis();
        String result = jedis.set(key.getBytes(), SerializationUtils.serialize(object));
        jedis.expire(key.getBytes(), seconds);

        return result;
    }

    public static String setObjectAtTime(String key, Serializable object, long unixTime) {
    	Jedis jedis = getJedis();
        String result = jedis.set(key.getBytes(), SerializationUtils.serialize(object));
        jedis.expireAt(key.getBytes(), unixTime);

        return result;
    }

    public static String setMap(String key, Map<String, String> value) {
        return getJedis().hmset(key, value);
    }

    public static String getValue(String key) {
        return getJedis().get(key);
    }

    public static String getValue(String key, String field) {
        return getJedis().hget(key, field);
    }

    public static Object getObject(String key) {
        byte[] bytes = getJedis().get(key.getBytes());

        return bytes != null ? SerializationUtils.deserialize(bytes) : null;
    }

    public static List<String> getMap(String key, String... fields) {
        return getJedis().hmget(key, fields);
    }

    public static Long remove(String key) {
        return getJedis().del(key);
    }

    public static Long remove(byte[] bytes) {
        return getJedis().del(bytes);
    }

    public static Long remove(String key, String... fields) {
        return getJedis().hdel(key, fields);
    }
}