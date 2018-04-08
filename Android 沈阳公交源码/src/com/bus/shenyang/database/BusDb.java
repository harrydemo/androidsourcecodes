package com.bus.shenyang.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.bus.shenyang.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class BusDb {
	private final int BUFFER_SIZE = 400000;
public static final String DB_NAME = "shenyang.db"; //��������ݿ��ļ���
public static final String PACKAGE_NAME = "com.bus.shenyang";
public static final String DB_PATH = "/data"
		+ Environment.getDataDirectory().getAbsolutePath() + "/"
		+ PACKAGE_NAME;  //���ֻ��������ݿ��λ��

private SQLiteDatabase database;
private Context context;

public BusDb(Context context) {
	this.context = context;
}

public void openDatabase() {
	this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
}

private SQLiteDatabase openDatabase(String dbfile) {
	try {
		if (!(new File(dbfile).exists())) {//�ж����ݿ��ļ��Ƿ���ڣ�����������ִ�е��룬����ֱ�Ӵ����ݿ�
			InputStream is = this.context.getResources().openRawResource(
					R.raw.shenyang); 
			FileOutputStream fos = new FileOutputStream(dbfile);
			byte[] buffer = new byte[BUFFER_SIZE];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
		}
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
		return db;
	} catch (FileNotFoundException e) {
		Log.e("Database", "File not found");
		e.printStackTrace();
	} catch (IOException e) {
		Log.e("Database", "IO exception");
		e.printStackTrace();
	}
	return null;
}

public void closeDatabase() {

		this.database.close();
	}  


}
