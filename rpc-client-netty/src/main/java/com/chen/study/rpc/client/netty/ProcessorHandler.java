package com.chen.study.rpc.client.netty;

import com.chen.study.rpc.api.RpcRequest;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 执行处理器
 *
 * @author 陈添明
 * @date 2019/11/9
 */
public class ProcessorHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private Map<String, Object> handlerMap;


    public ProcessorHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        Object result = handle(msg);
        ctx.writeAndFlush(result).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 处理请求
     *
     * @param msg
     * @return
     */
    private Object handle(RpcRequest msg) {
        String serviceName = msg.getClassName();
        String version = msg.getVersion();
        //增加版本号的判断
        if (!StringUtils.isEmpty(version)) {
            serviceName += "-" + version;
        }
        Object service = handlerMap.get(serviceName);
        //拿到客户端请求的参数
        Object[] args = msg.getParameters();
        // 反射调用对应方法
        Method method = ReflectionUtils.findMethod(service.getClass(), msg.getMethodName(), msg.getParamTypes());
        return ReflectionUtils.invokeMethod(method, service, args);
    }
}
