package cn.colining.util;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * Created by colin on 2017/7/27.
 */
public class JedisAdapter {
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
        print(15, jedis.exists(userKey, "email"));
        print(15, jedis.exists(userKey, "age"));
        print(17, jedis.hkeys(userKey));
        print(18, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "School", "cugb");
        jedis.hsetnx(userKey, "age", "22");
        print(19, jedis.hgetAll(userKey));


        String listKey1 = "like1";
        String listKey2 = "like2";
        for (int i = 0; i <10;i++) {
            jedis.sadd(listKey1, String.valueOf(i));
            jedis.sadd(listKey2, String.valueOf(i * i));
        }
        print(20, jedis.smembers(listKey1));
        print(21, jedis.smembers(listKey2));
        print(22, jedis.sunion(listKey1, listKey2));
        print(23,jedis.sdiff(listKey1,listKey2));
        print(24, jedis.sinter(listKey1, listKey2));
        print(25, jedis.sismember(listKey1, "16"));
        jedis.srem(listKey1, "5");
        print(26, jedis.smembers(listKey1));
        jedis.smove(listKey2, listKey1, "25");
        print(26, jedis.smembers(listKey1));
        print(26, jedis.smembers(listKey2));
        print(27,jedis.scard(listKey1));


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
        print(36,jedis.zrange(rankKey,0,10));
        print(36,jedis.zrange(rankKey,0,3));
        print(36,jedis.zrevrange(rankKey,0,3));

        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")) {
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }

        print(38, jedis.zrank(rankKey, "colin"));
        print(38, jedis.zrevrank(rankKey, "colin"));


        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");

        print(40, jedis.zlexcount(setKey, "-", "+"));
        print(41, jedis.zlexcount(setKey, "(b", "[d"));
        print(41, jedis.zlexcount(setKey, "[b", "[d"));
    }
}
