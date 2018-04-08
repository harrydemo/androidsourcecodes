package cn.itcast.junit;

import android.test.AndroidTestCase;
import android.util.Log;

public class PersonTest extends AndroidTestCase {
	private static final String TAG = "PersonTest";

	public void testSave() throws Throwable{
		PersonService service = new PersonService();
		service.save();
	}
	
	public void testGetCount() throws Throwable{
		PersonService service = new PersonService();
		assertEquals(20, service.getCount());
	}
	
	public void testOutLog() throws Throwable{
		Log.i(TAG, "www.itcast.cn");
	}
	
	public void testSystem() throws Throwable{
		System.out.println("www.csdn.net");
		System.err.println("www.sohu.com");
	}
}
