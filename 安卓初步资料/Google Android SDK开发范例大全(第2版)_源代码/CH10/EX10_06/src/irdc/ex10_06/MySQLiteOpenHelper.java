package irdc.ex10_06;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MySQLiteOpenHelper extends SQLiteOpenHelper
{
  public String TableNames[];
  public String FieldNames[][];
  public String FieldTypes[][];
  public static String NO_CREATE_TABLES = "no tables";
  private String message = "";
  
  public MySQLiteOpenHelper(Context context, String dbname, CursorFactory factory, int version, String tableNames[], String fieldNames[][], String fieldTypes[][])
  {
    super(context, dbname, factory, version);
    TableNames = tableNames;
    FieldNames = fieldNames;
    FieldTypes = fieldTypes;
  }

  @Override
  public void onCreate(SQLiteDatabase db)
  {
    if (TableNames == null)
    {
      message = NO_CREATE_TABLES;
      return;
    }
    /* 建立table */
    for (int i = 0; i < TableNames.length; i++)
    {
      String sql = "CREATE TABLE " + TableNames[i] + " (";
      for (int j = 0; j < FieldNames[i].length; j++)
      {
        sql += FieldNames[i][j] + " " + FieldTypes[i][j] + ",";
      }
      sql = sql.substring(0, sql.length() - 1);
      sql += ")";
      db.execSQL(sql);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int arg1, int arg2)
  {
    for (int i = 0; i < TableNames[i].length(); i++)
    {
      String sql = "DROP TABLE IF EXISTS " + TableNames[i];
      db.execSQL(sql);
    }
    onCreate(db);
  }

  public void execSQL(String sql) throws java.sql.SQLException
  {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL(sql);
  }

  /**
   * 查询数据
   * 
   * @param table
   *          查询的table name
   * @param columns
   *          查询的数据的字段名称
   * @param selection
   *          查询条件字符串 如：field1 = ? and field2 = ?
   * @param selectionArgs
   *          查询条件的值 如：["a","b"]
   * @param groupBy
   *          groupBy后面的字符串 如：field1,field2
   * @param having
   *          having后面的字符串
   * @param orderBy
   *          orderBy后面的字符串
   * @return Cursor 包含了取得的资料集
   */
  public Cursor select(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
  {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    return cursor;
  }

  /**
   * 新增资料
   * 
   * @param table
   *          新增资料的table name
   * @param fields
   *          新增数据的字段名称
   * @param values
   *          新增数据的字段值
   * @return long row id
   */
  public long insert(String table, String fields[], String values[])
  {
    SQLiteDatabase db = this.getWritableDatabase();
    /* 将新增的值放入ContentValues */
    ContentValues cv = new ContentValues();
    for (int i = 0; i < fields.length; i++)
    {
      cv.put(fields[i], values[i]);
    }
    return db.insert(table, null, cv);
  }

  /**
   * 删除数据
   * 
   * @param table
   *          删除数据的table name
   * @param where
   *          删除资料的条件
   * @param whereValue
   *          删除资料的条件值
   * @return int 删除的笔数
   */
  public int delete(String table, String where, String[] whereValue)
  {
    SQLiteDatabase db = this.getWritableDatabase();

    return db.delete(table, where, whereValue);
  }

  /**
   * 更新数据
   * 
   * @param table
   *          更新数据的table name
   * @param fields
   *          更新数据的字段名称
   * @param values
   *          更新数据的字段值
   * @param where
   *          更新除数据的条件
   * @param whereValue
   *          更新数据的条件值
   * @return int 更新的笔数
   */
  public int update(String table, String updateFields[],
      String updateValues[], String where, String[] whereValue)
  {
    SQLiteDatabase db = this.getWritableDatabase();

    /* 将修改的值放入ContentValues */
    ContentValues cv = new ContentValues();
    for (int i = 0; i < updateFields.length; i++)
    {
      cv.put(updateFields[i], updateValues[i]);
    }
    return db.update(table, cv, where, whereValue);
  }

  public String getMessage()
  {
    return message;
  }

  @Override
  public synchronized void close()
  {
    // TODO Auto-generated method stub
    super.close();
  }
}

