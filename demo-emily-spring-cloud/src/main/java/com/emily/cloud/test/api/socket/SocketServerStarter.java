package com.emily.cloud.test.api.socket;

import com.emily.infrastructure.date.DateConvertUtils;
import com.emily.infrastructure.date.DatePatternInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * @author Emily
 * @program: spring-parent
 *  socket监听器
 * @since 2021/03/04
 */
public class SocketServerStarter {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8888);
            while (true) {
                Socket a1 = ss.accept();
                InputStream is = a1.getInputStream();
                byte[] bytes = new byte[1024];
                int len = is.read(bytes);
                System.out.println("服务器端接收到：" + new String(bytes, 0, len));
                OutputStream os = a1.getOutputStream();
                String now = "我是服务器，当前时间是：" + DateConvertUtils.format(LocalDateTime.now(), DatePatternInfo.YYYY_MM_DD_HH_MM_SS_SSS);
                os.write(now.getBytes());
                os.flush();
            }

        } catch (Exception e) {

        }
    }
}
