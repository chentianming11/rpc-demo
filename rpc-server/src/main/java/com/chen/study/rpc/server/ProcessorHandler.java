package com.chen.study.rpc.server;

import com.chen.study.rpc.api.RpcRequest;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.util.StringUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/9/15
 */
public class ProcessorHandler implements Runnable {

    private Socket socket;

    private Map<String, Object> handlerMap;

    public ProcessorHandler(Socket socket, Map<String, Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }


    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 反射调用本地服务
            System.out.println("receive: " + rpcRequest);
            Object result = invoke(rpcRequest);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    /**
     * 反射调用
     *
     * @param request
     * @return
     */
    private Object invoke(RpcRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String methodName = request.getMethodName();
        String serviceName = request.getClassName();
        String version = request.getVersion();
        //增加版本号的判断
        if (!StringUtils.isEmpty(version)) {
            serviceName += "-" + version;
        }
        Object service = handlerMap.get(serviceName);
        if (service == null) {
            throw new RuntimeException("service not found:" + serviceName);
        }
        //拿到客户端请求的参数
        Object[] args = request.getParameters();
        Object result = MethodUtils.invokeExactMethod(service, methodName, args);
        return result;
    }
}
