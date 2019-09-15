package com.chen.study.rpc.client;

import com.chen.study.rpc.api.IHelloService;
import com.chen.study.rpc.api.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 陈添明
 * @date 2019/9/15
 */
public class AppClient {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        RpcProxyClient rpcProxyClient = applicationContext.getBean(RpcProxyClient.class);

        IHelloService helloService = rpcProxyClient.clientProxy(IHelloService.class, "localhost", 8080);
        String hello = helloService.sayHello("hello");
        System.out.println(hello);

        String result = helloService.saveUser(new User().setName("mic").setAge(10));
        System.out.println(result);

    }
}
