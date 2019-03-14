package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

//服务器端通道处理类
class ServerChannelHandler extends ChannelHandlerAdapter {

    //通道读就绪事件（收到客户端数据事件）
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        Channel channel = ctx.channel();
        String text = (String) msg;
        System.out.println("收到客户端 " + channel.remoteAddress() + "\t" + channel.id() + " 消息：" + text);
        channel.writeAndFlush(new Scanner(System.in).next() + "~!gzz!~");
    }

    // 异常捕捉事件
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("异常捕捉事件....");
    }

    // 通道断开连接事件
    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.disconnect(ctx, promise);
        System.out.println("通道断开事件....");

    }

    // 通道关闭事件
    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);
        System.out.println("通道关闭了....");

    }
}

//客户端通道处理类
class ClientChannelHandler extends ChannelHandlerAdapter {

    //通道读就绪事件（收到服务器数据事件）
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        Channel channel = ctx.channel();
        String text = (String) msg;
        System.out.println("收到服务器 " + channel.remoteAddress() + "\t" + channel.id() + " 消息：" + text);
        channel.writeAndFlush(new Scanner(System.in).next() + "~!gzz!~");
    }

    // 异常捕捉事件
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("异常捕捉事件....");
    }

    // 通道断开连接事件
    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.disconnect(ctx, promise);
        System.out.println("通道断开事件....");

    }

    // 通道关闭事件
    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);
        System.out.println("通道关闭了....");

    }
}

//netty5 服务器端
class Netty5Server {

    public static void main(String[] args) throws InterruptedException {
        // 1.创建 netty服务器引导类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 2.创建 用于接收客户端Accept连接事件的循环组（负责接收客户端连接的线程池）
        NioEventLoopGroup boos = new NioEventLoopGroup();
        // 3.创建 用户接受客户端Read/Write事件的循环组 (负责IO操作、数据传输的工作线程池)
        NioEventLoopGroup work = new NioEventLoopGroup();
        // 4.绑定事件循环组
        serverBootstrap.group(boos, work);
        // 5.设置服务器通道类型
        serverBootstrap.channel(NioServerSocketChannel.class);
        // 6.设置服务器通道处理类
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ByteBuf byteBuf = Unpooled.copiedBuffer("~!gzz!~".getBytes());
                //设置TCP通道传输粘包解码器
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,byteBuf));
                //设置协助通道事件处理者数据编码器
                ch.pipeline().addLast(new StringEncoder());
                //设置协助通道事件处理者数据解码器
                ch.pipeline().addLast(new StringDecoder());
                //设置事件处理者
                ch.pipeline().addLast(new ServerChannelHandler());
            }
        });

        System.out.println("Netty5服务器已开启....");
        // 7.绑定netty服务器端口号
        ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
        channelFuture.channel().closeFuture().sync();
        //优雅关闭循环组
        boos.shutdownGracefully();
        work.shutdownGracefully();
    }

}

class Netty5Client {

    public static void main(String[] args) throws InterruptedException {
        //1. 创建客户端引导类
        Bootstrap bootstrap = new Bootstrap();
        // 2.创建 接受服务端Read/Write事件的循环组 (负责IO操作、数据传输的工作线程池)
        NioEventLoopGroup work = new NioEventLoopGroup();
        // 3.绑定事件循环组
        bootstrap.group(work);
        // 4.绑定客户端通道类型
        bootstrap.channel(NioSocketChannel.class);
        // 5.设置客户端处理者
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ByteBuf byteBuf = Unpooled.copiedBuffer("~!gzz!~".getBytes());
                //设置TCP通道传输粘包解码器
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,byteBuf));
                //设置协助通道事件处理者数据编码器
                ch.pipeline().addLast(new StringEncoder());
                //设置协助通道事件处理者数据解码器
                ch.pipeline().addLast(new StringDecoder());
                //设置事件处理者
                ch.pipeline().addLast(new ClientChannelHandler());
            }
        });
        System.out.println("Netty5客户端已开启.....");
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
        channelFuture.channel().writeAndFlush("在吗？~!gzz!~");
        channelFuture.channel().closeFuture().sync();
        work.shutdownGracefully();
    }
}

public class Netty5Test {
}
