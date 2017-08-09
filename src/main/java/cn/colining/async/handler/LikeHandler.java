package cn.colining.async.handler;

import cn.colining.async.EventHandler;
import cn.colining.async.EventModel;
import cn.colining.async.EventType;
import cn.colining.model.Message;
import cn.colining.model.User;
import cn.colining.service.MessageService;
import cn.colining.service.UserService;
import cn.colining.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by colin on 2017/8/9.
 */
@Component
public class LikeHandler implements EventHandler{
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(WendaUtil.ANONYMITY_USERID);
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(eventModel.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的评论,https://127.0.0.1:8080/question/" + eventModel.getExt("questionId"));
        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }

}
