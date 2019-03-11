package socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

//NIO Socket客户端 异步非阻塞
class TcpNioClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        //创建socket客户端通道
        SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
        //设置 通道异步非阻塞
        channel.configureBlocking(false);
        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("爱你一万年".getBytes());
        //开启读取模式
        byteBuffer.flip();
        //将数据写入传输到通道
        channel.write(byteBuffer);
        byteBuffer.clear();
        Thread.sleep(10);
        //读取服务器响应的数据
        channel.read(byteBuffer);
        System.out.println("TCP服务器返回数据" + new String(byteBuffer.array()));
        //关闭通道和socket连接
        channel.close();
    }
}

//NIO Socket服务器 异步非阻塞
class TcpNioServer {
    public static void main(String[] args) throws IOException {
        //获取 socket服务器通道
        ServerSocketChannel channel = ServerSocketChannel.open();
        //设置 通道异步非阻塞
        channel.configureBlocking(false);
        //绑定 服务器通道连接端口
        channel.bind(new InetSocketAddress(8080));
        System.out.println("TCP服务器已经启动.......");
        //创建选择器
        Selector selector = Selector.open();
        //将服务器通道注册到选择器上，并监听指定事件，（已经准备好接受的事件）
        channel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("TCP服务器等待接收服务器请求.......");
        //判断选择器是否有接受到的事件
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //判断此事件 是否已经准备好接受
                if (selectionKey.isAcceptable()) {
                    //获取此事件关联的socket通道
                    SocketChannel socketChannel = channel.accept();
                    //设置 通道异步非阻塞
                    socketChannel.configureBlocking(false);
                    //将此通道注册到选择器，并监听指定事件，（数据就绪可读的事件）
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                //判断此事件 数据是否已经就绪可读
                if (selectionKey.isReadable()) {
                    //获取此事件关联的socket通道
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //设置 通道异步非阻塞
                    socketChannel.configureBlocking(false);
                    //创建缓冲区接收数据
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    socketChannel.read(byteBuffer);
                    System.out.println("TCP服务器端接收到数据：" + new String(byteBuffer.array()));
                    byteBuffer.clear();
                    byteBuffer.put(("HTTP/1.1 200 OK\r\n" +  //响应头第一行
                            "Content-Type: text/html; charset=utf-8\r\n" +  //简单放一个头部信息
                            "\r\n" +  //这个空行是来分隔请求头与请求体的
                            "<h1>gzz联合王国来入侵了</h1>\r\n").getBytes());
                    byteBuffer.flip();
                    //响应给客户端
                    socketChannel.write(byteBuffer);
                    //关闭 通道
                    socketChannel.close();
                }

                iterator.remove();
            }
        }
        selector.close();
        channel.close();
    }

}

public class TcpNioTest {
}
