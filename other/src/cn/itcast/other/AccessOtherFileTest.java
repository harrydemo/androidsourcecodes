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
	//找不到文件
	//访问其他应用中使用Context.MODE_PRIVATE创建的文件
	public void testAccessPrivateFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/itcast.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		byte[] data = StreamTool.readInputStream(inStream);
		String content = new String(data);
		Log.i(TAG, content);
	}
	//访问其他应用中使用Context.MODE_APPEND创建的文件
	public void testAccessAppendFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/append.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		byte[] data = StreamTool.readInputStream(inStream);
		String content = new String(data);
		Log.i(TAG, content);
	}
	
	//访问其他应用中使用Context.MODE_WORLD_READABLE创建的文件
	public void testAccessReadableFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/readable.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		byte[] data = StreamTool.readInputStream(inStream);
		String content = new String(data);
		Log.i(TAG, content);
	}
	
	//往其他应用中使用Context.MODE_WORLD_WRITEABLE创建的文件写入数据
	public void testWriteWriteableFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/writeable.txt";
		File file = new File(path);
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write("www.itcast.cn".getBytes());
		outStream.close();
	}
	
	
	//访问其他应用中使用Context.MODE_WORLD_WRITEABLE创建的文件
	public void testAccessWriteableFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/writeable.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		byte[] data = StreamTool.readInputStream(inStream);
		String content = new String(data);
		Log.i(TAG, content);
	}
	
	//往其他应用中使用Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_READABLE创建的文件写入数据
	public void testWriteRWFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/rw.txt";
		File file = new File(path);
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write("www.itcast.cn".getBytes());
		outStream.close();
	}
	
	
	//访问其他应用中使用Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_READABLE创建的文件
	public void testAccessRWFile() throws Throwable{
		String path = "/data/data/cn.itcast.file/files/rw.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		byte[] data = StreamTool.readInputStream(inStream);
		String content = new String(data);
		Log.i(TAG, content);
	}
}
