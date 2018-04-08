package com.android.caigang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.caigang.model.UserInfo;

public class SqliteHelper extends SQLiteOpenHelper{
    //用来保存UserID、Access Token、Access Secret的表名
    public static final String TB_NAME="users";
    public SqliteHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+
                TB_NAME+"("+
                UserInfo.ID+" integer primary key,"+
                UserInfo.USERID+" varchar,"+
                UserInfo.TOKEN+" varchar,"+
                UserInfo.TOKENSECRET+" varchar,"+
                UserInfo.USERNAME+" varchar,"+
                UserInfo.USERICON+" blob"+
                ")"
                );
        Log.e("Database","onCreate");
    }
    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
        Log.e("Database","onUpgrade");
    }
    //更新列
    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
        try{
            db.execSQL("ALTER TABLE " +
                    TB_NAME + " CHANGE " +
                    oldColumn + " "+ newColumn +
                    " " + typeColumn
            );
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}