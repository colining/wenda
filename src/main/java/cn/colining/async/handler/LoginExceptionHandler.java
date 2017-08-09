package cn.colining.async.handler;

import cn.colining.async.EventHandler;
import cn.colining.async.EventModel;
import cn.colining.async.EventType;
import cn.colining.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by colin on 2017/8/9.
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MailSender mailSender;
    @Override
    public void doHandle(EventModel eventModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", eventModel.getExt("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExt("email"), "登录异常", "mails/login_exception.ftl", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
