package com.chen.study.rpc.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = "com.chen.study.rpc")
public class SpringConfig {
    @Bean
    public RpcServer rpcServer() {
        return new RpcServer(8080);
    }
}
