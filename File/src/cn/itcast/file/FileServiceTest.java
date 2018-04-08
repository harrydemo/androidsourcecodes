package cn.itcast.file;

import cn.itcast.service.FileService;
import android.test.AndroidTestCase;
import android.util.Log;

public class FileServiceTest extends AndroidTestCase {
	private static final String TAG = "FileServiceTest";

	public void testReadFile() throws Throwable{
		FileService service = new FileService(this.getContext());
		String content = service.getContent("itcast.txt");
		Log.i(TAG, content);
	}
	
	public void testSaveAppend() throws Throwable{
		FileService service = new FileService(this.getContext());
		service.saveAppend("append.txt", ",http://www.sohu.com");
	}
	
	public void testSaveReadable() throws Throwable{
		FileService service = new FileService(this.getContext());
		service.saveReadable("readable.txt", ",http://www.csdn.com");
	}
	
	public void testSaveWriteable() throws Throwable{
		FileService service = new FileService(this.getContext());
		service.saveWriteable("writeable.txt", "http://www.babasport.com");
	}
	
	public void testSaveRW() throws Throwable{
		FileService service = new FileService(this.getContext());
		service.saveRW("rw.txt", "http://www.sina.com");
	}
}
