package com.chen.study.rpc.server;

import com.chen.study.rpc.api.IHelloService;
import com.chen.study.rpc.api.User;

/**
 * @author 陈添明
 * @date 2019/9/15
 */
@RpcService(value = IHelloService.class, version = "v1.0")
public class HelloServiceImpl implements IHelloService {

    @Override
    public String sayHello(String content) {
        System.out.println("【V1.0】request in sayHello:" + content);
        return "【V1.0】Say Hello:" + content;
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @Override
    public String saveUser(User user) {
        System.out.println("【V1.0】request in saveUser:" + user);
        return "【V1.0】SUCCESS";
    }
}
