package com.chen.study.rpc.client.netty;

import com.chen.study.rpc.api.IPaymentService;


@RpcService(IPaymentService.class)
public class PaymentServiceImpl implements IPaymentService {
    @Override
    public void doPay() {
        System.out.println("执行doPay方法");
    }
}
