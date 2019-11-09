package com.chen.study.rpc.client.netty;

import com.chen.study.rpc.client.netty.registry.IRegistryCenter;
import com.chen.study.rpc.client.netty.registry.RegistryCenterWithZk;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/11/9
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {

    /**
     * 处理器Map
     * key：serviceName -> 服务类名+版本
     * value：serviceBean
     */
    private Map<String, Object> handlerMap = new HashMap();

    /**
     * 服务端口
     */
    private int port;

    /**
     * 注册中心
     */
    private IRegistryCenter registryCenter = new RegistryCenterWithZk();

    public RpcServer(int port) {
        this.port = port;
    }


    /**
     * 获得本机的ip地址
     *
     * @return
     */
    private static String getAddress() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return inetAddress.getHostAddress();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //接收客户端的链接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //处理已经被接收的链接
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().
                                    addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null))).
                                    addLast(new ObjectEncoder()).
                                    addLast(new ProcessorHandler(handlerMap));
                        }
                    });
            serverBootstrap.bind(port).sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                //拿到注解
                RpcService rpcService = serviceBean.getClass().getAnnotation((RpcService.class));
                // 拿到接口类定义名称
                String serviceName = rpcService.value().getName();
                //拿到版本号
                String version = rpcService.version();
                if (!StringUtils.isEmpty(version)) {
                    serviceName += "-" + version;
                }
                handlerMap.put(serviceName, serviceBean);
                registryCenter.registry(serviceName, getAddress() + ":" + port);
            }
        }
    }
}
