package com.chen.study.rpc.client.netty;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 启动类
 */
public class Bootstrap {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        ((AnnotationConfigApplicationContext) context).start();
    }
}
