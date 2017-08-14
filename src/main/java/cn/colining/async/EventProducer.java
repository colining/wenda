package cn.colining.async;

import cn.colining.util.JedisAdapter;
import cn.colining.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by colin on 2017/8/9.
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 添加时间给消息队列，这里消息队列由Redis实现
     * 由于这里并不存在优先级级的问题，所以就用普通链表即可
     * 这里将事件json化，然后使用kv存入redis，然后就可以从redis在读出来，
     * 还原成事件
     * @param eventModel
     * @return
     */
    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
