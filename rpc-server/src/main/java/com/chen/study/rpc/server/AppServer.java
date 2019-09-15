package com.chen.study.rpc.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 陈添明
 * @date 2019/9/15
 */
public class AppServer {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        applicationContext.start();
    }
}
