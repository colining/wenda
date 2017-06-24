package cn.colining.aspect;


import com.sun.org.apache.xpath.internal.Arg;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * Created by colin on 2017/6/24.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* cn.colining.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : joinPoint.getArgs() ) {
            stringBuilder.append("arg: " + o.toString() + "|");
        }
        LOGGER.info("before method "+stringBuilder.toString());

    }
    @After("execution(* cn.colining.controller.IndexController.*(..))")
    public void  afterMethod(){
        LOGGER.info("after method"+new Date());

    }
}
