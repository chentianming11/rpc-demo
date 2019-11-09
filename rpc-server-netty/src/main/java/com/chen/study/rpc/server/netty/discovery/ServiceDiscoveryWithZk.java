package com.chen.study.rpc.server.netty.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/11/9
 */
public class ServiceDiscoveryWithZk implements IServiceDiscovery {

    CuratorFramework curatorFramework = null;

    private static String CONNECTION_STR = "192.168.13.102:2181,192.168.13.103:2181,192.168.13.104:2181";

    /**
     * 服务地址的本地缓存
     */
    List<String> serviceRepos = new ArrayList<>();

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
     * 根据服务名称返回服务地址
     *
     * @param serviceName
     * @return
     */
    @Override
    public String discovery(String serviceName) {
        //完成了服务地址的查找(服务地址被删除)
        String path = "/" + serviceName;
        if (serviceRepos.isEmpty()) {
            try {
                serviceRepos = curatorFramework.getChildren().forPath(path);
                registryWatch(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //针对已有的地址做负载均衡
        LoadBalanceStrategy loadBalanceStrategy = new RandomLoadBalance();
        return loadBalanceStrategy.selectHost(serviceRepos);
    }

    /**
     * 注册Watch
     * 服务子节点发生变动，更新本地服务缓存
     *
     * @param path
     * @throws Exception
     */
    private void registryWatch(final String path) throws Exception {
        PathChildrenCache nodeCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener nodeCacheListener = (curatorFramework1, pathChildrenCacheEvent) -> {
            System.out.println("客户端收到节点变更的事件");
            // 再次更新本地的缓存地址
            serviceRepos = curatorFramework1.getChildren().forPath(path);
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();

    }
}
