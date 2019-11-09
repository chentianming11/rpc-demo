package com.chen.study.rpc.client.netty.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author 陈添明
 * @date 2019/11/9
 */
public class RegistryCenterWithZk implements IRegistryCenter {

    CuratorFramework curatorFramework = null;

    private static String CONNECTION_STR = "192.168.13.102:2181,192.168.13.103:2181,192.168.13.104:2181";

    {
        //初始化zookeeper的连接， 会话超时时间是5s，衰减重试
        curatorFramework = CuratorFrameworkFactory.builder().
                connectString(CONNECTION_STR).sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                namespace("registry")
                .build();
        curatorFramework.start();
    }

    /**
     * 服务注册名称和服务注册地址实现服务的管理
     *
     * @param serviceName
     * @param serviceAddress
     */
    @Override
    public void registry(String serviceName, String serviceAddress) {
        String servicePath = "/" + serviceName;
        try {
            // 判断节点是否存在
            if (curatorFramework.checkExists().forPath(servicePath) == null) {
                // 节点不存在，创建持久化节点
                curatorFramework.create().creatingParentsIfNeeded().
                        withMode(CreateMode.PERSISTENT).forPath(servicePath);
            }
            //serviceAddress: ip:port 注册服务节点（临时）
            String addressPath = servicePath + "/" + serviceAddress;
            curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
            System.out.println("服务注册成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
