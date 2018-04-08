package cn.itcast.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 业务bean
 *
 */
public class FileService {
	private DBOpenHelper openHelper;

	public FileService(Context context) {
		openHelper = new DBOpenHelper(context);
	}
	/**
	 * 获取每条线程已经下载的文件长度
	 * @param path
	 * @return
	 */
	public String find(File file){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select sourceid from fileuploadlog where filepath=?", new String[]{file.getAbsolutePath()});
		if(cursor.moveToFirst()){
			return cursor.getString(0);
		}
		return null;
	}
	/**
	 * 保存上传文件信息
	 * @param path
	 * @param map
	 */
	public void save(File file, String sourceid){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		try{
			db.execSQL("insert into fileuploadlog(filepath, sourceid) values(?,?)",
					new Object[]{file.getAbsolutePath(), sourceid});
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		db.close();
	}
	/**
	 * 当文件上传完成后，删除对应的上传记录
	 * @param path
	 */
	public void delete(File file){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("delete from fileuploadlog where filepath=?", new Object[]{file.getAbsolutePath()});
		db.close();
	}
	
}
