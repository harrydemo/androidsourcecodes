package cn.com.karl.domain;


import cn.com.karl.dida.DidaActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WordService {

	private DBOpenHandler dbOpenHandler;
	
	public WordService(Context context){
		this.dbOpenHandler=new DBOpenHandler(context);
	}
	public long insertWords(Dict dict)
	{
		SQLiteDatabase db=dbOpenHandler.getWritableDatabase();  
		ContentValues content=new ContentValues();
		content.put(DBOpenHandler.NAME,dict.getKey());
		content.put(DBOpenHandler.AUDIO,dict.getPron());
		content.put(DBOpenHandler.PRON, dict.getPs());
		content.put(DBOpenHandler.DEF, dict.getAcceptation());
		content.put(DBOpenHandler.XML, DidaActivity.sb.toString());
		
		return db.insert(DBOpenHandler.TB_WORD, null, content);
	}
	public Cursor listWords(){
		SQLiteDatabase db=dbOpenHandler.getReadableDatabase();  
		return db.query(DBOpenHandler.TB_WORD, new String[]{DBOpenHandler.NAME}, null, null, null, null, null);
	}
	public Cursor queryWord(){
		SQLiteDatabase db=dbOpenHandler.getReadableDatabase();  
		return db.query(DBOpenHandler.TB_WORD, new String[]{DBOpenHandler.NAME}, null, null, null, null, null);
	}
}
