package com.chen.study.rpc.client;

import com.chen.study.rpc.api.RpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author 陈添明
 * @date 2019/9/15
 */
public class RpcNetTransport {
    private String host;

    private int port;

    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object send(RpcRequest rpcRequest) {
        System.out.println("send: " + rpcRequest);
        Object result = null;
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            result = objectInputStream.readObject();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
