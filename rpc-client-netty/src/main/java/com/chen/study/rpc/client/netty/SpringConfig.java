package com.chen.study.rpc.client.netty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 陈添明
 * @date 2019/11/9
 */
@Configuration
@ComponentScan(basePackages = "com.chen.study")
public class SpringConfig {

    @Bean(name = "gpRpcServer")
    public RpcServer gpRpcServer() {
        return new RpcServer(8080);
    }
}
