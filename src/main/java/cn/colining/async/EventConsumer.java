package cn.colining.async;

import cn.colining.util.JedisAdapter;
import cn.colining.util.RedisKeyUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by colin on 2017/8/9.
 */
@Service
public class EventConsumer  implements InitializingBean,ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    //某个EventType 所对应的Handler链表；
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    //上下文，可以通过它来找到所有实现EventHandler接口的类
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                //对于某个handler，他关注那些事件类型
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType eventType : eventTypes) {
                    //这个事件被第一次注册
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<>());
                    }
                    //将这个handler加入到他能处理的事件类型的那条链表中
                    config.get(eventType).add(entry.getValue());
                }
            }
        }
        //新建一个线程，简单来说，就是一直循环，有事件过来就消费；
        Thread thread = new Thread(() -> {
            while (true) {
                String key = RedisKeyUtil.getEventQueueKey();
                List<String> events = jedisAdapter.brpop(0, key);
                if (events != null) {
                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }
                        // 还原event
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("非法事件");
                            continue;
                        }
                        //  对于该event所对应的所有handler，让handler进行处理
                        for (EventHandler eventHandler : config.get(eventModel.getType())) {
                            eventHandler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
