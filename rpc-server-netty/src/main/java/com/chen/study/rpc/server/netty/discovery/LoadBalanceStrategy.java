package com.chen.study.rpc.server.netty.discovery;

import java.util.List;

public interface LoadBalanceStrategy {

    String selectHost(List<String> repos);

}
