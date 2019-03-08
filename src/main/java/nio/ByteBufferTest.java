package nio;


import java.nio.ByteBuffer;

public class ByteBufferTest {
    public static void main(String[] args) {
        //初始化字节缓冲区 1024长度
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("----------------初始化-----------------------");
        System.out.println("缓冲区当前操作下标：" + byteBuffer.position());//获取缓冲区当前操作位相当于下标
        System.out.println("缓冲区当前可用大小：" + byteBuffer.limit()); //获取缓冲区可操作大小，缓冲区当前的终点
        System.out.println("缓冲区最大容量：" + byteBuffer.capacity());//获取缓冲区最大容量，不可被修改
        System.out.println("-------------------------------------------");
        //写入字节 缓冲区position 自增操作+5，根据字符串长度自增，一个英文1个byte，一个汉字3个byte。
        byteBuffer.put("apple".getBytes());
        System.out.println("------------------写入值----------------------");
        System.out.println("缓冲区当前操作下标：" + byteBuffer.position());
        System.out.println("缓冲区当前可用大小：" + byteBuffer.limit());
        System.out.println("缓冲区最大容量：" + byteBuffer.capacity());
        System.out.println("-------------------------------------------");
        //开启读取模式 缓冲区 position变成0。
        byteBuffer.flip();
        System.out.println("-----------------开启读取模式-----------------");
        System.out.println("缓冲区当前操作下标：" + byteBuffer.position());
        System.out.println("缓冲区当前可用大小：" + byteBuffer.limit());
        System.out.println("缓冲区最大容量：" + byteBuffer.capacity());
        System.out.println("-------------------------------------------");
        byte[] data = new byte[5];
        // 读取字节 根据data长度，从position加1的位置开始读取
        byteBuffer.get(data);
        System.out.println(new String(data));
        // 开启重读模式 还原到上次position的位置
        byteBuffer.rewind();
        System.out.println("-----------------重读----------------------");
        System.out.println("缓冲区当前操作下标：" + byteBuffer.position());
        System.out.println("缓冲区当前可用大小：" + byteBuffer.limit());
        System.out.println("缓冲区最大容量：" + byteBuffer.capacity());
        System.out.println("-------------------------------------------");
        byteBuffer.get(data);
        System.out.println(new String(data));
        // 清空缓冲区，还原position为0，不会清空值
        byteBuffer.clear();
        System.out.println("-----------------清空clear----------------------");
        System.out.println("缓冲区当前操作下标：" + byteBuffer.position());
        System.out.println("缓冲区当前可用大小：" + byteBuffer.limit());
        System.out.println("缓冲区最大容量：" + byteBuffer.capacity());
        System.out.println("-------------------------------------------");


        byte[] markData = new byte[5];
        byteBuffer.get(markData,0,2);
        System.out.println("数据：" + new String(markData));
        System.out.println("缓冲区当前操作下标：" + byteBuffer.position());
        //标记当前position的值
        byteBuffer.mark();

        byteBuffer.get(markData,2,2);
        System.out.println("数据：" + new String(markData));
        System.out.println("缓冲区当前操作下标：" + byteBuffer.position());
        // 重置position为mark标记的值
        byteBuffer.reset();
        byteBuffer.get(markData,0,2);
        System.out.println("数据：" + new String(markData));
        System.out.println("缓冲区当前操作下标：" + byteBuffer.position());

    }
}
