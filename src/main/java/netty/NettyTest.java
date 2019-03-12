package netty;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//事件处理类
class ServerHandler extends SimpleChannelHandler {

    // 收到客户端数据事件
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        super.messageReceived(ctx, e);
        String message = e.getMessage().toString();
        System.out.println("收到消息事件....");
        System.out.println("服务器端接受到数据：" + message);
        Channel channel = ctx.getChannel();
        Scanner scanner = new Scanner(System.in);
        channel.write(scanner.next());
    }

    // 异常捕捉事件
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        super.exceptionCaught(ctx, e);
        System.out.println("异常捕捉事件....");
    }

    // 通道断开连接事件
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
        System.out.println("通道断开事件....");
    }

    // 通道关闭事件
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelClosed(ctx, e);
        System.out.println("通道关闭了....");
    }

}

class ClientHandler extends SimpleChannelHandler{

    // 收到服务器数据事件
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        super.messageReceived(ctx, e);
        String message = e.getMessage().toString();
        System.out.println("收到消息事件....");
        System.out.println("客户端接受到数据：" + message);
        Channel channel = ctx.getChannel();
        Scanner scanner = new Scanner(System.in);
        channel.write(scanner.next());
    }

    // 异常捕捉事件
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        super.exceptionCaught(ctx, e);
        System.out.println("异常捕捉事件....");
    }

    // 通道断开连接事件
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
        System.out.println("通道断开事件....");
    }

    // 通道关闭事件
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelClosed(ctx, e);
        System.out.println("通道关闭了....");
    }
}

//netty服务器
class NettyServer{
    public static void main(String[] args) {
        // 1.创建netty服务器
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 2.创建两个线程池，一个监听端口号，一个NIO监听处理，工作线程池
        ExecutorService boos = Executors.newCachedThreadPool();
        ExecutorService work = Executors.newCachedThreadPool();
        // 3.将线程池加入工厂
        serverBootstrap.setFactory(new NioServerSocketChannelFactory(boos,work));
        // 4.设置管道工厂 处理NIO
        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            //设置管道
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                //创建管道
                ChannelPipeline pipeline = Channels.pipeline();
                //设置管道解码器
                pipeline.addLast("decoder",new StringDecoder());
                //设置管道编码器
                pipeline.addLast("encoder",new StringEncoder());
                //设置服务器管道事件处理者
                pipeline.addLast("serverHandler",new ServerHandler());
                return pipeline;
            }
        });

        // 5.绑定netty服务器监听端口号
        serverBootstrap.bind(new InetSocketAddress(8080));
        System.out.println("Netty服务器端已启动.....");

    }
}
// netty客户端
class NettyClient{
    public static void main(String[] args) {
        // 1.创建netty客户端
        ClientBootstrap clientBootstrap = new ClientBootstrap();
        // 2.创建两个线程池，一个监听端口号，一个NIO监听处理，工作线程池
        ExecutorService boos = Executors.newCachedThreadPool();
        ExecutorService work = Executors.newCachedThreadPool();
        // 3.将线程池放入工厂
        clientBootstrap.setFactory(new NioClientSocketChannelFactory(boos,work));
        // 4.设置管道工厂
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            //设置管道
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                //创建管道
                ChannelPipeline pipeline = Channels.pipeline();
                //设置管道解码器
                pipeline.addLast("decoder",new StringDecoder());
                //设置管道编码器
                pipeline.addLast("encoder",new StringEncoder());
                //设置客户端管道事件处理者
                pipeline.addLast("clientHandler",new ServerHandler());
                return pipeline;
            }
        });

        // 5.连接netty服务器地址端口
        ChannelFuture connect = clientBootstrap.connect(new InetSocketAddress("127.0.0.1", 8080));
        Channel channel = connect.getChannel();
        channel.write("在吗?");

    }
}

public class NettyTest {
}
