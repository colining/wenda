package cn.colining.async;

import java.util.List;

/**
 * Created by colin on 2017/8/9.
 */
public interface EventHandler {
    void doHandle(EventModel eventModel);

    List<EventType> getSupportEventTypes();
}
