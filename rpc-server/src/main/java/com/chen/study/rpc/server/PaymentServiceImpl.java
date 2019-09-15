package com.chen.study.rpc.server;

import com.chen.study.rpc.api.IPaymentService;


@RpcService(value = IPaymentService.class, version = "v1.0")
public class PaymentServiceImpl implements IPaymentService {


    @Override
    public void doPay() {
        System.out.println("执行doPay方法");
    }
}
