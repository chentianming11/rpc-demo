package com.chen.study.rpc.server.netty;

import com.chen.study.rpc.api.RpcRequest;
import com.chen.study.rpc.server.netty.discovery.IServiceDiscovery;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 陈添明
 * @date 2019/11/9
 */
public class RemoteInvocationHandler implements InvocationHandler {


    private IServiceDiscovery serviceDiscovery;
    private String version;

    public RemoteInvocationHandler(IServiceDiscovery serviceDiscovery, String version) {
        this.serviceDiscovery = serviceDiscovery;
        this.version = version;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //请求数据的包装
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParamTypes(method.getParameterTypes());
        rpcRequest.setParameters(args);
        rpcRequest.setVersion(version);
        String serviceName = rpcRequest.getClassName();
        if (!StringUtils.isEmpty(version)) {
            serviceName = serviceName + "-" + version;
        }
        String serviceAddress = serviceDiscovery.discovery(serviceName);
        //远程通信
        RpcNetTransport netTransport = new RpcNetTransport(serviceAddress);
        Object result = netTransport.send(rpcRequest);
        return result;
    }
}
