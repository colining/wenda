package cn.colining.model;

import org.springframework.context.annotation.Bean;

/**
 * Created by colin on 2017/6/24.
 */

public class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name) {
        this.name = name;
    }
    public String getDescription(){
        return "This is " +name;
    }
}
