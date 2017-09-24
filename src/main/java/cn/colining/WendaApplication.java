package cn.colining;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * main函数 SpringBootApplication 是<br>
 * 三个命令的替代写法
 */
@SpringBootApplication
//@MapperScan注解用于直接扫描mapper，不用每次都写
@MapperScan("cn.colining.dao")
public class WendaApplication extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WendaApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(WendaApplication.class, args);
    }
}
