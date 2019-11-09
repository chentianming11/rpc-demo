package com.chen.study.rpc.server.netty;


import com.chen.study.rpc.server.netty.discovery.IServiceDiscovery;
import com.chen.study.rpc.server.netty.discovery.ServiceDiscoveryWithZk;

import java.lang.reflect.Proxy;


/**
 * RPC服务调用客户端
 * 动态代理
 */
public class RpcProxyClient {

    private IServiceDiscovery serviceDiscovery = new ServiceDiscoveryWithZk();


    public <T> T clientProxy(final Class<T> interfaceCls, String version) {

        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class<?>[]{interfaceCls}, new RemoteInvocationHandler(serviceDiscovery, version));
    }
}
