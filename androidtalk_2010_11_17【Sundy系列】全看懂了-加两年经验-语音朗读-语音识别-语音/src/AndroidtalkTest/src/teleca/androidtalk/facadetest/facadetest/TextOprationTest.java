package teleca.androidtalk.facadetest.facadetest;

import junit.framework.TestResult;
import teleca.androidtalk.facade.TextOpration;
import android.test.AndroidTestCase;
import android.util.Log;

public class TextOprationTest extends AndroidTestCase {
	private TextOpration to,to1,to2 ,gb2312,utf8,utf16;
	private String str,str1,str2;
	
	private String strutf8 = "/sdcard/UTF-8.txt";
	private String strgb = "/sdcard/GB2312.txt";
	private String strutf16 = "/sdcard/UTF-16BE.txt";
	//= new TextOpration("/sdcard/af.txt");
	
	public void setUp(){
		gb2312= new TextOpration(strgb);
		utf8 = new TextOpration(strutf8);
		utf16 = new TextOpration(strutf16);
		
	}
	
	
	public void testEncoding(){
		to = new TextOpration("/sdcard/UTF-16BE.txt");
		str = to.getFileEncoding();
		
		assertEquals("UTF-8",utf8.getFileEncoding());
	}
	

}
