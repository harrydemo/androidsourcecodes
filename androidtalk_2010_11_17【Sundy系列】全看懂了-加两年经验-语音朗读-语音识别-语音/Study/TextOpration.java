package com.androidtalk;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.util.Log;


public final class TextOpration {
	// ������ʽ�й�
	private final static String ENC_UTF16BE = "UTF-16BE";
	private final static String ENC_UTF8 = "UTF-8";
	private final static String ENC_GB2312 = "GB2312";
	private File _file;
	
	//���ļ������йصı���
	private String fullBookName;  // ȫ·������
	private String bookName;  // ��·������
	private long bookSize; // size of the book
   	private InputStream fis;	
  	private String bookFormat; // �ļ���ʽ

	
    
    
    public TextOpration(String fileName) {
		setFullBookName(fileName);
		byte[] headOfBook = new byte[3];
		try {
	        _file = new File(fileName);
	        bookSize = _file.length();
	        fis = new FileInputStream(_file);	
			fis.read(headOfBook); //���ļ�ͷ�����ֽڣ��ж��ļ������ʽ
		    fis.close();
		    fis = null;
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		// �жϱ����ʽ
		if ((headOfBook[0] == -2) && (headOfBook[1] == -1)) {
			bookFormat =  ENC_UTF16BE;
		}
		else if ((headOfBook[0] == -1) && (headOfBook[1] == -2)) {
			bookFormat =  ENC_UTF16BE;
		}
		else if ((headOfBook[0] == -17) && (headOfBook[1] == -69) &&(headOfBook[2] == -65))
		{
			bookFormat = ENC_UTF8;  // UTF8 with BOM
		}
		else {
			bookFormat = ENC_GB2312;
		}
		
	}
	
    
	public String getStringFromFile()
	{
		try {
			StringBuffer sBuffer = new StringBuffer();
			fis = new FileInputStream(_file);
			InputStreamReader inputStreamReader = new InputStreamReader(fis, bookFormat);
			BufferedReader in = new BufferedReader(inputStreamReader);
			if(!_file.exists())
			{
				return null;
			}
			while (in.ready()) {
				sBuffer.append(in.readLine() + "\n");
			}
			in.close();
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
	
	
    private void Erro(String str){
    	Log.d("TAG", str);
    }
    
      
      
	public long getBookSize() {
		return bookSize;
	}
    
    public void setFullBookName(String fileName){
		fullBookName = fileName;
		int i = fileName.lastIndexOf('/') + 1;
		int t = fileName.lastIndexOf('.');
		bookName = fileName.substring(i, t);
    }
	
    public String getBookName() {
		return bookName;
	}
}
