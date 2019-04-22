package com.xcbeyond.common.communication.socket;

/**
 * 启动Socket Server
 * @Auther: xcbeyond
 * @Date: 2019/4/22 16:39
 */
public class SocketServerTest {
    public static void main(String[] args) {
        new SocketServer(9999).start();
    }
}