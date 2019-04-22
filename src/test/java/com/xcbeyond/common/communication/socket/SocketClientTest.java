package com.xcbeyond.common.communication.socket;

/**
 * SocketClient测试类
 * @Auther: xuchao
 * @Date: 2019/4/22 16:58
 */
public class SocketClientTest {
    public static void main(String[] args) {
        SocketClient socketClient = new SocketClient("127.0.0.1", 9999);
        String msg = "0000047010000                                Q01710     743                  <req><head><msgType>OAApproveResult</msgType></head><body><formId>1468982058432dqga4ag2sbgpixru5qy08yp91q83tw73obkz</formId><outProcessId>279905598125900800</outProcessId><dealUserIds>xc</dealUserIds><noteList><noteMap><formTitle>test</formTitle><formDealerId>xc</formDealerId><formDealTime>2019-04-20</formDealTime><result>001</result><formContent>同意</formContent></noteMap></noteList></body></req>";
        String response = socketClient.sendMessage(msg);
        System.out.println("返回数据为：\n" + response);
    }
}
