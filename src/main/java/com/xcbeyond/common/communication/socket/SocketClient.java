package com.xcbeyond.common.communication.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.Charset;

/**
 * socket客户端
 * @Auther: xcbeyond
 * @Date: 2019/4/22 15:45
 */
public class SocketClient {
	// 日志记录类
	private static Logger logger = LoggerFactory.getLogger(SocketClient.class);
	
	// 字符集编码
	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	// Socket链接地址
	private String ip = null;
	// Socket链接端口
	private int port = 0;
	// 消息长度
	private int msglen = 8;
	// Socket链接超时时间(默认值为30秒)
	private int timeout = 30000;
	// 读取缓冲区(默认1024字节)
	private int bufferCapacity = 1024;
	// 字符集
	private String charset = "UTF-8";
	

	public SocketClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public String sendMessage(String msg) {
		logger.debug("通讯开始处理请求！");
		
		Socket client = null;
		//输入流
		InputStream is = null;
		//输出流
		OutputStream os = null;
		//返回报文
		byte[] result = null;
		try {
			// 创建Socket链接
			client = new Socket(ip, port);
			
			// 定义用来存储发送数据的byte缓冲区
			byte[] sendContent = msg.getBytes(charset);
			int contentLength = sendContent.length;
			ByteBuffer sendbuffer = ByteBuffer.allocate(contentLength + 8);

			// 添加报文头
			String header = getWriteHead(contentLength);
			sendbuffer.put(header.getBytes(charset));
			
			sendbuffer.put(msg.getBytes(charset));

			// 将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			
			// 向服务器发送数据
			this.sendMsg(sendbuffer.array(), client, os);
			// 读取对方返回数据
			result = this.readMsg(client, is);
			
			if (null == result) {
				logger.error("收到应答报文为空");
			}
			
		} catch (UnresolvedAddressException ue) {
			logger.error("远程服务配置的地址格式不正确:[" + ip + "]", ue);
		} catch (UnknownHostException e) {
			logger.error("无法建立到远程服务器地址的链接[" + ip + ":" + port
					+ "]", e);
		} catch (ConnectException ce) {
			logger.error("无法连接到远程服务器[" + ip + ":" + port
					+ "]", ce);
		} catch (IOException e) {
			logger.error("通讯接出捕获IO错误", e);
		} catch (Exception e) {
			logger.error("连接到远程服务器执行服务时发生未知异常", e);
		} finally {
			try {
				// 关闭输出流
				if (null != os) {
					os.close();
				}
				// 关闭输入流
				if (null != is) {
					is.close();
				}
				// 关闭链接
				if (null != client) {
					client.close();
				}
			} catch (IOException e) {
					logger.error("关闭链接时捕获IO错误，抛出通讯接出异常！", e);
				}
			}
		
		return new String(result,UTF8);
	}
		
	
	/**
	 * 读取返回数据
	 * @param client
	 * @param is
	 * @return
	 * @throws IOException 
	 */
	private byte[] readMsg(Socket client, InputStream is) throws IOException {
		// 记录等待起始时间
		long startTime = System.currentTimeMillis();
		
		//设置超时时间
		client.setSoTimeout(timeout);
		is = client.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		//首先读取报文头长度
		byte[] head = new byte[msglen];
		bis.read(head, 0, head.length);
		int length = 0;
		try {
			length = Integer.parseInt(new String(head));
		} catch (NumberFormatException nfe) {
			logger.error("报文格式有误，[报文长度]域包含无效数字！");
			nfe.printStackTrace();
		}
		
		logger.debug("读取报文头，获得报文内容的大小为:[" + length + "]");
		
		//定义用于存放整个报文体的缓冲区数组
		byte[] request = new byte[length];
		//已读取到的数据
		int bufferSize = 0;
		// 每次读取的数据的值
		int numberRead = 0;
		while (true) {
			if(bufferCapacity + bufferSize > length) {
				numberRead = bis.read(request, bufferSize, length - bufferSize);
			} else {
				numberRead = bis.read(request, bufferSize ,bufferCapacity);
			}
			bufferSize += numberRead;
			if (bufferSize == length) {
				break;
			}
		}
		
		logger.debug("读取报文完成，获取报文内容为:[" + new String(request,UTF8) + "]");
		
		// 记录当前时间并计算已等待时间
		long currentTime = System.currentTimeMillis();
		long waitTime = currentTime - startTime;
		
		logger.debug("当前时间:" + currentTime
					+ ",开始等待读的时间:" + startTime + ",已等待时间："
					+ waitTime + "毫秒");
		return request;
	}

	/**
	 * 发送消息
	 * @param buff
	 * @return
	 * @throws IOException
	 */
	private void sendMsg(byte[] buff, Socket client, OutputStream os) throws IOException {
		logger.debug("开始发送数据...\r\n"+ new String(buff, UTF8));

		os = client.getOutputStream();
		os.write(buff);
		os.flush();
		
		logger.debug("数据发送完毕，发送数据的长度为：[" + buff.length + "]");
	}

	/**
	 * 拼发送8位报文长度
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