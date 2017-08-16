package cn.colining.async.handler;

import cn.colining.async.EventHandler;
import cn.colining.async.EventModel;
import cn.colining.async.EventType;
import cn.colining.model.EntityType;
import cn.colining.model.Message;
import cn.colining.model.User;
import cn.colining.service.MessageService;
import cn.colining.service.UserService;
import cn.colining.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by colin on 2017/8/9.
 */
@Component
public class FollowHandler implements EventHandler{
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    /**
     * handler是实际上做事的部分，生产者把事件通过消息队列传递过来；
     * 然后消费者获取队列，分发给handler；
     * @param eventModel
     */
    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(eventModel.getActorId());
        if (eventModel.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName() + "关注了你的问题,https://127.0.0.1:8080/question/" + eventModel.getEntityId());
        } else if (eventModel.getEntityType() == EntityType.ENTITY_USER) {
            message.setContent("用户" + user.getName() + "关注了你，https://127.0.0.1:8080/user/" + eventModel.getActorId());
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }

}
