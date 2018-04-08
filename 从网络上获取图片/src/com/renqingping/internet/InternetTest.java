package com.renqingping.internet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class InternetTest
{

    @Test
    public void getImage() throws Exception
    {
        // fail("Not yet implemented");
        // 首先我们要得到请求的路径，路径为我们想要得到的资源
        String urlpath = "http://pica.nipic.com/2008-05-23/200852381811521_2.jpg";
        // 建立URL对象，抛出异常
        URL url = new URL(urlpath);
        // 得到HttpURLConnection对象
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 声明请求方式
        conn.setRequestMethod("GET");
        // 设置连接超时
        conn.setConnectTimeout(6 * 1000);
        // 连接成功
        if (conn.getResponseCode() == 200)
        {
            // 得到服务器传回来的数据，相对我们来说输入流
            InputStream inputStream = conn.getInputStream();
            // 得到数据
            byte[] data = readInStream(inputStream);
            // 创建保存文件
            File file = new File("xiaocai.jpg");
            // 创建一个文件输出流
            FileOutputStream outputStream = new FileOutputStream(file);
            // 将我们所得的二进制数据全部写入我们建好的文件中
            outputStream.write(data);
            // 关闭输出流
            outputStream.close();
        }

    }

    // 读取流文件的内容
    public byte[] readInStream(InputStream inputStream) throws Exception
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 声明缓冲区
        byte[] buffer = new byte[1024];
        // 定义读取默认长度
        int length = -1;
        while ((length = inputStream.read(buffer)) != -1)
        {
            // 把缓冲区中输出到内存中
            byteArrayOutputStream.write(buffer, 0, length);
        }
        // 关闭输出流
        byteArrayOutputStream.close();
        // 关闭输入流
        inputStream.close();
        // 返回这个输出流的字节数组
        return byteArrayOutputStream.toByteArray();
    }
}
