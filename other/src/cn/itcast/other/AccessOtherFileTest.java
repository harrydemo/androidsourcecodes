package cn.itcast.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import cn.itcast.utils.StreamTool;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

public class AccessOtherFileTest extends AndroidTestCase {
	private static final String TAG = "AccessOtherFileTest";
	//�Ҳ����ļ�
	//��������Ӧ����ʹ��Context.MODE_PRIVATE�������ļ�
	public void testAccessPrivateFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/itcast.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		byte[] data = StreamTool.readInputStream(inStream);
		String content = new String(data);
		Log.i(TAG, content);
	}
	//��������Ӧ����ʹ��Context.MODE_APPEND�������ļ�
	public void testAccessAppendFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/append.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		byte[] data = StreamTool.readInputStream(inStream);
		String content = new String(data);
		Log.i(TAG, content);
	}
	
	//��������Ӧ����ʹ��Context.MODE_WORLD_READABLE�������ļ�
	public void testAccessReadableFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/readable.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		byte[] data = StreamTool.readInputStream(inStream);
		String content = new String(data);
		Log.i(TAG, content);
	}
	
	//������Ӧ����ʹ��Context.MODE_WORLD_WRITEABLE�������ļ�д������
	public void testWriteWriteableFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/writeable.txt";
		File file = new File(path);
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write("www.itcast.cn".getBytes());
		outStream.close();
	}
	
	
	//��������Ӧ����ʹ��Context.MODE_WORLD_WRITEABLE�������ļ�
	public void testAccessWriteableFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/writeable.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		byte[] data = StreamTool.readInputStream(inStream);
		String content = new String(data);
		Log.i(TAG, content);
	}
	
	//������Ӧ����ʹ��Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_READABLE�������ļ�д������
	public void testWriteRWFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/rw.txt";
		File file = new File(path);
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write("www.itcast.cn".getBytes());
		outStream.close();
	}
	
	
	//��������Ӧ����ʹ��Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_READABLE�������ļ�
	public void testAccessRWFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/rw.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		byte[] data = StreamTool.readInputStream(inStream);
		String content = new String(data);
		Log.i(TAG, content);
	}
}
