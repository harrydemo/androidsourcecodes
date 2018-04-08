package com.betterman.upload;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.content.Context;
import android.util.Log;

import com.betterman.util.Constant;

public class UploadThread extends Thread
{
    private String filePath = null;
    Context context = null;

    public UploadThread(Context context, String filePath)
    {
        this.filePath = filePath;
        this.context = context;
    }

    @Override
    public void run()
    {
        InetAddress address;
        try
        {
            address = InetAddress.getByName(Constant.SERVER_IP);
            Socket socket = new Socket(address, Constant.SERVER_PORT);
            
            // �������ֻ״̬���׽���
            socket.setKeepAlive(true);
            // ���ö�ȡ��ʱʱ��
           // socket.setSoTimeout(30 * 1000);
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            String fileNameLine = Constant.FILE_HEAD + fileName;
            // ��дһ��ͷ��Ϣ
            // os.write(Constant.HEADLINE.getBytes());
            // ��дһ���ļ�����Ϣ
            os.write(fileNameLine.getBytes());
            //��һ�ζ�ȡ������������Ϣ
            Log.i("betterman", "c-----First write fileNameLine : " + fileNameLine);
            byte[] buffer = new byte[2048];
            int length = is.read(buffer);
            
            String result = new String(buffer,0,length);
            Log.i("betterman", "c-----First receive result: " + result);
            
            if(Constant.SUCCESS.equals(result))
            {
                // �ڶ���д���ļ�
                FileInputStream fis = new FileInputStream(filePath);
                Log.i("betterman", "c-----Sencond write file start! ");
                
                int count = 0;
                while (-1 != (length = fis.read(buffer)) )
                {
                    os.write(buffer, 0, length);
                    Log.i("betterman", "c-----Sencond write file times: " + (++count));
                }
                fis.close();
                os.flush();
            }

            
            Log.i("betterman", "c-----Sencond write file success!");
          //�ڶ��ζ�ȡ������������Ϣ
//            length = is.read(buffer);
//            result = new String(buffer,0,length);;
           // Log.i("betterman", "c-----Sencond read result: " + result);
            // Toast.makeText(context, result, Toast.LENGTH_LONG).show();

            os.close();
            is.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
