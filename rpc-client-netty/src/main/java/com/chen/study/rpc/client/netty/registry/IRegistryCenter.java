package com.chen.study.rpc.client.netty.registry;

public interface IRegistryCenter {

    /**
     * 服务注册名称和服务注册地址实现服务的管理
     *
     * @param serviceName    className-version
     * @param serviceAddress ip:port
     */
    void registry(String serviceName, String serviceAddress);
}
