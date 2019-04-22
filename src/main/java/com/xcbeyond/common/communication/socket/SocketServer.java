package com.xcbeyond.common.communication.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * socket服务器
 * @Auther: xcbeyond
 * @Date: 2019/4/22 16:29
 */
public class SocketServer {
	private static Logger log = LoggerFactory.getLogger(SocketServer.class);
	private static int port = 9999;

	public SocketServer(int port) {
		this.port = port;
	}

	public void start() {
		log.info("开始启动SocketServer，端口：" + port);
		try {
			ServerSocket server = new ServerSocket(port);
			while (true) {
				Socket socket = server.accept();
				new Task(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("SocketServer启动失败，异常为：" + e.getMessage());
		}
	}

	/**
	 * 用来处理Socket的Task
	 */
	class Task extends Thread {
		// 字符集编码
		private final Charset UTF8 = Charset.forName("UTF-8");
		
		private Socket socket;
		
		public Task(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			handleSocket();

		}

		// 完成与客户端socket的通信
		private void handleSocket() {
			DataInputStream dis = null;
			DataOutputStream dos = null;
			try {

				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				
				byte[] contentLengthBy = new byte[8];
				dis.read(contentLengthBy);
				int contentLength = Integer.parseInt(new String(contentLengthBy, UTF8));
				byte[] bytes = new byte[contentLength];
				dis.read(bytes); // size是读取到的字节数
				
				String request = new String(bytes, UTF8);
				log.debug("接收到的数据：" + request);


				//TODO 此处进行服务端的业务逻辑处理
				String responseBody = "Request received!";

				//拼装响应数据长度
				String response = getWriteHead(responseBody.getBytes("UTF-8").length) + responseBody;

				log.debug("返回的数据：" + response);
				dos.write(response.getBytes(UTF8));
				dos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					dis.close();
					dos.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * 拼发送报文长度
	 * @param length
	 * @return
	 */
	private String getWriteHead(long length) {
		StringBuilder result = new StringBuilder();
		String res = String.valueOf(length);
		if (res.length() == 8) {
			result.append(res);
		} else {
			res = "00000000" + res;
			int index = res.length() - 8;
			result.append(res.substring(index, res.length()));
		}
		return result.toString();
	}
}
