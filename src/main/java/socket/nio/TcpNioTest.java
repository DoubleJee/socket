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
        //设置 通道非阻塞
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
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置 通道非阻塞
        serverSocketChannel.configureBlocking(false);
        //绑定 服务器socket通道连接端口，监听连接
        serverSocketChannel.bind(new InetSocketAddress(8080));
        System.out.println("TCP服务器已经启动.......");
        //创建选择器
        Selector selector = Selector.open();
        //将服务器型socket通道注册到选择器上，并指定注册事件，（连接接受就绪事件）
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("TCP服务器等待接收服务器请求.......");
        //当有注册的事件就绪到达时，方法返回 ，没有的话会阻塞。
        while (selector.select() > 0) {
            //获取就绪事件集合
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //判断此事件 是否连接接受就绪
                if (selectionKey.isAcceptable()) {
                    //获取此事件关联的socket通道
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //设置 通道非阻塞
                    socketChannel.configureBlocking(false);
                    //将此socket通道注册到选择器，并指定注册事件。（读就绪事件）
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                //判断此事件 是否读就绪
                if (selectionKey.isReadable()) {
                    //获取此事件关联的socket通道
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //设置 通道非阻塞
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
                //删除此事件避免重复
                iterator.remove();
            }
        }
        selector.close();
        serverSocketChannel.close();
    }

}

public class TcpNioTest {
}
