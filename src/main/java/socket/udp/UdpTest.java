package socket.udp;

import java.io.IOException;
import java.net.*;


// upd协议的服务器端
class UDPServer{
    public static void main(String[] args) throws IOException {
        System.out.println("建立UDP服务器端成功.....");
        //定义udp（服务器）数据报通讯端，端口号8080，ip地址默认本机
        DatagramSocket server = new DatagramSocket(8080);
        //接收数据对象
        byte[] data = new byte[1024];
        //定义数据通讯包
        DatagramPacket dp = new DatagramPacket(data,data.length);
        System.out.println("等待客户端接入数据.....");
        //数据报通讯端 等待接受数据（阻塞状态）
        server.receive(dp);
        System.out.println("数据包来源：" + dp.getAddress()+ "数据包数据：" + new String(dp.getData()));
        //关闭udp服务器通讯端
        server.close();
    }
}
// udp协议的客户端
class UDPClient{
    public static void main(String[] args) throws IOException {
        System.out.println("建立UDP客户端端成功.....");
        //定义udp（客户端）数据报通讯端。无任何指向
        DatagramSocket client = new DatagramSocket();
        //定义数据通讯包数据
        String data = "gzz联合王国";
        //定义数据通讯包 要发往的ip和端口，定位到应用。
        DatagramPacket dp = new DatagramPacket(data.getBytes(),data.getBytes().length, InetAddress.getByName("127.0.0.1"),8080);
        client.send(dp);
        System.out.println("发送数据到指定服务器，不管它接受成不成功.....");
        client.close();
    }
}

public class UdpTest {
}
