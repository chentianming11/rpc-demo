package com.chen.study.rpc.client;

import com.chen.study.rpc.api.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 陈添明
 * @date 2019/9/15
 */
public class RemoteInvocationHandler implements InvocationHandler {

    private String host;

    private int port;

    public RemoteInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //请求数据的包装
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setVersion("v1.0");
        //远程通信
        RpcNetTransport netTransport = new RpcNetTransport(host, port);
        return netTransport.send(rpcRequest);
    }
}
