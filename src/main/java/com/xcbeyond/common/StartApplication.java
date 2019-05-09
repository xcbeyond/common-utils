package com.xcbeyond.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot启动类。
 * 在该项目中目的用来启动一个tomcat，作为web容器
 * @Auther: xcbeyond
 * @Date: 2019/5/9 22:34
 */
@SpringBootApplication
public class StartApplication {
    private static Logger logger = LoggerFactory.getLogger(StartApplication.class);

    public static void main(String[] args) {
        if(logger.isDebugEnabled()) {
            logger.debug("StartApplication starting...");
        }
        SpringApplication.run(StartApplication.class, args);
    }
}
