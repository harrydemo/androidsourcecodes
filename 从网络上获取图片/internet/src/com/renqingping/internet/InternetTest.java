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
        // ��������Ҫ�õ������·����·��Ϊ������Ҫ�õ�����Դ
        String urlpath = "http://pica.nipic.com/2008-05-23/200852381811521_2.jpg";
        // ����URL�����׳��쳣
        URL url = new URL(urlpath);
        // �õ�HttpURLConnection����
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // ��������ʽ
        conn.setRequestMethod("GET");
        // �������ӳ�ʱ
        conn.setConnectTimeout(6 * 1000);
        // ���ӳɹ�
        if (conn.getResponseCode() == 200)
        {
            // �õ������������������ݣ����������˵������
            InputStream inputStream = conn.getInputStream();
            // �õ�����
            byte[] data = readInStream(inputStream);
            // ���������ļ�
            File file = new File("xiaocai.jpg");
            // ����һ���ļ������
            FileOutputStream outputStream = new FileOutputStream(file);
            // ���������õĶ���������ȫ��д�����ǽ��õ��ļ���
            outputStream.write(data);
            // �ر������
            outputStream.close();
        }

    }

    // ��ȡ���ļ�������
    public byte[] readInStream(InputStream inputStream) throws Exception
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // ����������
        byte[] buffer = new byte[1024];
        // �����ȡĬ�ϳ���
        int length = -1;
        while ((length = inputStream.read(buffer)) != -1)
        {
            // �ѻ�������������ڴ���
            byteArrayOutputStream.write(buffer, 0, length);
        }
        // �ر������
        byteArrayOutputStream.close();
        // �ر�������
        inputStream.close();
        // ���������������ֽ�����
        return byteArrayOutputStream.toByteArray();
    }
}
