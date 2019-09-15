package com.chen.study.rpc.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Rpc服务类
 *
 * @author 陈添明
 * @date 2019/9/15
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {

    /**
     * 服务暴露的端口号
     */
    private Integer port;

    /**
     * 服务处理映射
     * key - 服务接口name-版本号
     * Value - 服务bean
     */
    private Map<String, Object> handlerMap = new HashMap();


    ExecutorService executorService = Executors.newCachedThreadPool();

    public RpcServer(Integer port) {
        this.port = port;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // RpcServer启动
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                // 每一个socket交由一个线程进行处理
                executorService.execute(new ProcessorHandler(socket, handlerMap));
            }
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 从spring容器中取带有RpcService注解的bean
        Map<String, Object> objectMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!objectMap.isEmpty()) {
            objectMap.forEach((beanName, bean) -> {
                //拿到注解
                RpcService rpcService = bean.getClass().getAnnotation((RpcService.class));
                //拿到接口类定义
                String serviceName = rpcService.value().getName();
                //拿到版本号
                String version = rpcService.version();
                if (!StringUtils.isEmpty(version)) {
                    serviceName += "-" + version;
                }
                // 注册服务处理器
                handlerMap.put(serviceName, bean);
            });
        }
    }
}
