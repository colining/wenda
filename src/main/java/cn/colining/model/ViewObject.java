package cn.colining.model;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by colin on 2017/6/29.
 */

/**
 * 通过ViewObject将全部需要传递至页面的对象都放入viewobject中
 * 这样就可以便捷的传递信息了
 */
public class ViewObject {

    private Map<String, Object> object = new HashMap<String, Object>();

    public void set(String key, Object values) {
        object.put(key, values);
    }

    public Object get(String key) {
        return object.get(key);
    }
}
