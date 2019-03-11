package socket.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class TCPServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        System.out.println("TCP服务器已经启动.......");
        //创建TCP服务器 指定端口号
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("TCP服务器等待接收服务器请求.......");
        try {
            while (true) {

                //等待接收客户端请求 （没有会进入阻塞状态，接收到之后会返回一个客户端对象）
                Socket accept = serverSocket.accept();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //获取客户端的字节输入流
                            InputStream inputStream = accept.getInputStream();
                            //输入流转换为字符串信息
                            byte[] data = new byte[1024];
                            inputStream.read(data);
                            String dataString = new String(data);
                            System.out.println("TCP服务器端接收到数据：" + dataString);
                            //获取客户端字节输出流
                            OutputStream outputStream = accept.getOutputStream();
                            //写入数据到字节输出流
                            outputStream.write(("HTTP/1.1 200 OK\r\n" +  //响应头第一行
                                    "Content-Type: text/html; charset=utf-8\r\n" +  //简单放一个头部信息
                                    "\r\n" +  //这个空行是来分隔请求头与请求体的
                                    "<h1>gzz联合王国来入侵了</h1>\r\n").getBytes());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            if (accept != null) {
                                try {
                                    accept.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //关闭TCP服务器
            serverSocket.close();
        }
    }
}

class TCPClient {
    public static void main(String[] args) throws IOException {
        //创建socket客户端
        Socket socket = new Socket("127.0.0.1", 8080);
        System.out.println("连接服务器成功.....");
        //获取客户端输出流
        OutputStream outputStream = socket.getOutputStream();
        //写入数据到输出流
        outputStream.write("gzz联合王国来入侵了".getBytes());
        InputStream inputStream = socket.getInputStream();
        byte[] dataByte = new byte[1024];
        inputStream.read(dataByte);
        String result = new String(dataByte);
        System.out.println("服务器返回数据：" + result);
        //关闭TCP客户端
        socket.close();
    }
}

public class TCPTest {
}
