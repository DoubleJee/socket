package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

//直接缓冲区 物理内存
class DirectBuffer{
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        //创建只读文件通道
        FileChannel inChannel = FileChannel.open(Paths.get("C:/Users/cd_new_01/Desktop/myPlay.mp4"), StandardOpenOption.READ,StandardOpenOption.CREATE);
        //创建读写文件通道
        FileChannel outChannel = FileChannel.open(Paths.get("C:/Users/cd_new_01/Desktop/myPlay-copy.mp4"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        //将该通道文件的一个区域直接映射到内存中。
        MappedByteBuffer inByteBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0l, inChannel.size());
//        //将该通道文件的一个区域直接映射到内存中。
//        MappedByteBuffer outByteBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0L, inChannel.size());
//        outByteBuffer.put(inByteBuffer);
        //将缓冲区的数据，写入通道
        outChannel.write(inByteBuffer);
        inChannel.close();
        outChannel.close();
        long endTime = System.currentTimeMillis();
        System.out.println("操作耗时：" + (endTime - startTime));
    }
}
//非直接缓冲区 JVM空间、物理内存空间两者copy操作
class NoDirectBuffer{
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream("C:/Users/cd_new_01/Desktop/myPlay.mp4");
        FileOutputStream fos = new FileOutputStream("C:/Users/cd_new_01/Desktop/myPlay-copy.mp4");
        //获取文件输入流通道
        FileChannel inChannel = fis.getChannel();
        //获取文件输出流通道
        FileChannel outChannel = fos.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //循环读取通道数据，放入缓冲区
        while (inChannel.read(byteBuffer) != -1){
            //开启读模式
            byteBuffer.flip();
            //循环将缓冲区的数据，写入通道
            outChannel.write(byteBuffer);
            //清空缓冲区
            byteBuffer.clear();
        }
        inChannel.close();
        outChannel.close();
        fis.close();
        fos.close();
        long endTime = System.currentTimeMillis();
        System.out.println("操作耗时：" + (endTime - startTime));
    }
}

public class BestBuffer {

}
