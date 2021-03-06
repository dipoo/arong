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
    private static String HOST = RedisConfigManager.getString("redis.ip");

    //Redis的端口号
    private static int PORT = RedisConfigManager.getInteger("redis.port", 6379);

    //访问密码
    private static String PASSWORD = RedisConfigManager.getString("redis.password").equals("") ? null 
    		: RedisConfigManager.getString("redis.password");

  //默认选择的数据库
    private static Integer db = RedisConfigManager.getInteger("redis.db.default", 0);
    
    private static Integer initdb = RedisConfigManager.getInteger("redis.db.default", 0);
    
    //默认的KEY前缀
    private static String prefix = RedisConfigManager.getString("redis.key.prefix", "");
    
    private static String initprefix = RedisConfigManager.getString("redis.key.prefix", "");

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_TOTAL =  RedisConfigManager.getInteger("redis.pool.maxTotal");

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = RedisConfigManager.getInteger("redis.pool.maxIdle");

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException;
    private static int MAX_WAIT = RedisConfigManager.getInteger("redis.pool.maxWait");

    private static int TIMEOUT = 10000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = RedisConfigManager.getBoolean("redis.pool.testOnBorrow");
    private static boolean TEST_ON_RETURN =RedisConfigManager.getBoolean("redis.pool.testOnReturn");

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
     * 重置到初始化时的配置
     * 高并发下不推荐使用多db
     */
    public static void reset(){
    	db = initdb;
    	prefix = initprefix;
    }

    /**
     * 获取Jedis实例
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                if(db != null){
                	resource.select(db);
                }
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
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.set(prefix + key, value);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static Long setValue(String key, String field, String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.hset(prefix + key, field, value);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static String setValue(String key, String value, int seconds) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.set(prefix + key, value);
            jedis.expire(prefix + key, seconds);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static String setValueAtTime(String key, String value, long unixTime) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.set(prefix + key, value);
            jedis.expireAt(key, unixTime);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static String setObject(String key, Serializable object) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.set((prefix + key).getBytes(), SerializationUtils.serialize(object));
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static String setObject(String key, Serializable object, int seconds) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.set((prefix + key).getBytes(), SerializationUtils.serialize(object));
            jedis.expire((prefix + key).getBytes(), seconds);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static String setObjectAtTime(String key, Serializable object, long unixTime) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.set((prefix + key).getBytes(), SerializationUtils.serialize(object));
            jedis.expireAt(key.getBytes(), unixTime);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static String setMap(String key, Map<String, String> value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.hmset(prefix + key, value);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static String getValue(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.get(prefix + key);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static String getValue(String key, String field) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.hget(prefix + key, field);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static Object getObject(String key) {
        Jedis jedis = null;
        byte[] bytes = null;
        try {
            jedis = getJedis();
            bytes = jedis.get((prefix + key).getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return bytes != null ? SerializationUtils.deserialize(bytes) : null;
    }

    public static List<String> getMap(String key, String... fields) {
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.hmget(prefix + key, fields);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static Long remove(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.del(prefix + key);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static Long remove(byte[] bytes) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.del(bytes);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static Long remove(String key, String... fields) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.hdel(prefix + key, fields);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }

        return result;
    }

    public static void flushdb(Integer db){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.flushDB();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

	public static Integer getDb() {
		return db;
	}

	public static void setDb(Integer db) {
		RedisUtil.db = db;
	}

	public static String getPrefix() {
		return prefix;
	}

	public static void setPrefix(String prefix) {
		RedisUtil.prefix = prefix;
	}
}