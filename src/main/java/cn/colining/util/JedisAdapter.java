package cn.colining.util;

import cn.colining.model.User;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by colin on 2017/7/27.
 */
@Service
public class JedisAdapter implements InitializingBean {
    private JedisPool jedisPool;
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    public static void print(int index, Object object) {
        System.out.println(String.format("%d,%s", index, object.toString()));
    }

    public static void main(String[] args) {
        
        Jedis jedis = new Jedis();
        jedis.flushDB();
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));

        jedis.setex("hello2", 15, "hello2");

        jedis.set("pv", "100");
        jedis.incr("pv");
        jedis.incrBy("pv", 55);
        jedis.decrBy("pv", 2);
        print(2, jedis.get("pv"));

        String listName = "list";
        jedis.del(listName);
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(4, jedis.lrange(listName, 0, 12));
        print(4, jedis.lrange(listName, 0, 0));
        print(5, jedis.llen(listName));
        print(6, jedis.lpop(listName));
        print(7, jedis.llen(listName));
        System.out.println("==");
        print(9, jedis.lindex(listName, 3));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bb"));
        print(11, jedis.llen(listName));
        print(11, jedis.lrange(listName, 0, 12));


        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "1618188");
        print(12, jedis.hget(userKey, "name"));
        print(12, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hexists(userKey, "email"));
        print(15, jedis.hexists(userKey, "age"));
        print(17, jedis.hkeys(userKey));
        print(18, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "School", "cugb");
        jedis.hsetnx(userKey, "age", "22");
        print(19, jedis.hgetAll(userKey));


        String listKey1 = "like1";
        String listKey2 = "like2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(listKey1, String.valueOf(i));
            jedis.sadd(listKey2, String.valueOf(i * i));
        }
        print(20, jedis.smembers(listKey1));
        print(21, jedis.smembers(listKey2));
        print(22, jedis.sunion(listKey1, listKey2));
        print(23, jedis.sdiff(listKey1, listKey2));
        print(24, jedis.sinter(listKey1, listKey2));
        print(25, jedis.sismember(listKey1, "16"));
        jedis.srem(listKey1, "5");
        print(26, jedis.smembers(listKey1));
        jedis.smove(listKey2, listKey1, "25");
        print(26, jedis.smembers(listKey1));
        print(26, jedis.smembers(listKey2));
        print(27, jedis.scard(listKey1));
        print(27, jedis.scard(listKey1));


        String rankKey = "rankKey";

        jedis.zadd(rankKey, 15, "jim");
        jedis.zadd(rankKey, 60, "li");
        jedis.zadd(rankKey, 90, "colin");
        jedis.zadd(rankKey, 70, "lucy");
        jedis.zadd(rankKey, 61, "meimei");
        print(30, jedis.zcard(rankKey));
        print(31, jedis.zcount(rankKey, 61, 100));
        print(32, jedis.zscore(rankKey, "meimei"));

        jedis.zincrby(rankKey, 2, "lucy");
        print(30, jedis.zscore(rankKey, "lucy"));

        jedis.zincrby(rankKey, 2, "Luc");

        print(34, jedis.zscore(rankKey, "Luc"));

        print(35, jedis.zrange(rankKey, 0, 100));
        print(36, jedis.zrange(rankKey, 0, 10));
        print(36, jedis.zrange(rankKey, 0, 3));
        print(36, jedis.zrevrange(rankKey, 0, 3));

        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")) {
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }

        print(38, jedis.zrank(rankKey, "colin"));
        print(38, jedis.zrevrank(rankKey, "colin"));


        String setKey = "set";
        jedis.zadd(setKey, 5, "gaa");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");

        print(40, jedis.zlexcount(setKey, "-", "+"));
        print(41, jedis.zlexcount(setKey, "(b", "[d"));
        print(42, jedis.zlexcount(setKey, "[b", "[d"));
//        jedis.zrem(setKey, "b");
        print(43, jedis.zrange(setKey, 0, 10));
//        jedis.zremrangeByLex(setKey, "(c", "+");
        print(44, jedis.zrange(setKey, 0, 2));

        JedisPool pool = new JedisPool();
        for (int i = 0; i < 100; i++) {
            Jedis jedis1 = pool.getResource();
            print(45, jedis1.get("pv"));
            jedis1.close();
        }

        User user = new User();
        user.setName("xx");
        user.setPassword("ppp");
        user.setHeadUrl("a.png");
        user.setSalt("salt");
//        user.setId(1);
        jedis.set("user1", JSONObject.toJSONString(user));
        String value = jedis.get("user1");
        User user1 = JSONObject.parseObject(value, User.class);
        System.out.println(user1.getSalt());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("redis://localhost:6379/10");

    }

    /**
     * 向redis中添加键值对
     * @param key
     * @param value
     * @return
     */
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常");
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常");
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常");
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key,String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sismember(key,value);
        } catch (Exception e) {
            logger.error("发生异常");
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (IOException ioe) {
                    logger.error("发生异常" + ioe.getMessage());
                }
            }
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }


    public Double zscore(String key,String memeber) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zscore(key,memeber);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
