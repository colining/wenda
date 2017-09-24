package cn.colining.async;

/**
 * Created by colin on 2017/8/9.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5),
    ADD_QUESTION(6);
    private int value;

    EventType(int value) {
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
