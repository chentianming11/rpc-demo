package com.chen.study.rpc.client.netty;

import com.chen.study.rpc.api.IHelloService;
import com.chen.study.rpc.api.User;

@RpcService(value = IHelloService.class, version = "v2.0")
public class HelloServiceImpl2 implements IHelloService {

    @Override
    public String sayHello(String money) {
        System.out.println("【v2.0】request in sayHello:" + money);
        return "【v2.0】Say Hello:" + money;
    }

    @Override
    public String saveUser(User user) {
        System.out.println("【V1.0】request in saveUser:" + user);
        return "【v2.0】SUCCESS";
    }
}
