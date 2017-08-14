package cn.colining.async;

import java.util.HashMap;

/**
 * Created by colin on 2017/8/9.
 */
public class EventModel {
    //除了基本的类型以外，最重要的是事件模型加了一个hashmap，就像viewobject一样，
    //随便存约定好内容即可
    private EventType type;
    private int actorId;
    private int entityType;
    private int entityId;
    private int entityOwnerId;

    private HashMap<String, String> exts = new HashMap<>();

    public EventModel (EventType type) {
        this.type = type;
    }

    public EventModel() {
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;

    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;

    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;

    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public HashMap<String, String> getExts() {
        return exts;
    }

    public void setExts(HashMap<String, String> exts) {
        this.exts = exts;
    }
}
