package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

//分段读取，聚集写入
public class DistributedBuffer {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("C:/Users/cd_new_01/Desktop/3-02更新.txt");
        FileOutputStream fos = new FileOutputStream("C:/Users/cd_new_01/Desktop/3-02更新-copy.txt");
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        //创建两个缓冲区 分段读取
        ByteBuffer byteBufferOne = ByteBuffer.allocate(100);
        ByteBuffer byteBufferTwo = ByteBuffer.allocate(500);
        //放入数组
        ByteBuffer[] byteBuffers = new ByteBuffer[]{byteBufferOne,byteBufferTwo};
        inChannel.read(byteBuffers);
        System.out.println(new String(byteBufferOne.array()));
        System.out.println(new String(byteBufferTwo.array()));
        //写入之前要开启读取模式
        byteBufferOne.flip();
        byteBufferTwo.flip();
        //将两个缓冲区的数组 聚集写入
        outChannel.write(byteBuffers);
        inChannel.close();
        outChannel.close();
        fis.close();
        fos.close();
    }
}

//编码
class Bm{
    public static void main(String[] args) throws CharacterCodingException {
        //编码格式
        Charset gbk = Charset.forName("GBK");
        //编码器
        CharsetEncoder charsetEncoder = gbk.newEncoder();
        //解码器
        CharsetDecoder charsetDecoder = gbk.newDecoder();

        String text = "牛逼了我的哥";
        //创建缓冲区
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        //放入值到缓冲区
        charBuffer.put(text);
        charBuffer.flip();
        //编码加密
        ByteBuffer encode = charsetEncoder.encode(charBuffer);
        //编码解密
        CharBuffer decode = charsetDecoder.decode(encode);
        System.out.println(decode.array());

    }
}