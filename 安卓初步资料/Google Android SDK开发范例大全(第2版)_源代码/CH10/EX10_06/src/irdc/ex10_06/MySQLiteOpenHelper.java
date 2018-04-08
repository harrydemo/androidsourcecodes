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
    /* ����table */
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
   * ��ѯ����
   * 
   * @param table
   *          ��ѯ��table name
   * @param columns
   *          ��ѯ�����ݵ��ֶ�����
   * @param selection
   *          ��ѯ�����ַ��� �磺field1 = ? and field2 = ?
   * @param selectionArgs
   *          ��ѯ������ֵ �磺["a","b"]
   * @param groupBy
   *          groupBy������ַ��� �磺field1,field2
   * @param having
   *          having������ַ���
   * @param orderBy
   *          orderBy������ַ���
   * @return Cursor ������ȡ�õ����ϼ�
   */
  public Cursor select(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
  {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    return cursor;
  }

  /**
   * ��������
   * 
   * @param table
   *          �������ϵ�table name
   * @param fields
   *          �������ݵ��ֶ�����
   * @param values
   *          �������ݵ��ֶ�ֵ
   * @return long row id
   */
  public long insert(String table, String fields[], String values[])
  {
    SQLiteDatabase db = this.getWritableDatabase();
    /* ��������ֵ����ContentValues */
    ContentValues cv = new ContentValues();
    for (int i = 0; i < fields.length; i++)
    {
      cv.put(fields[i], values[i]);
    }
    return db.insert(table, null, cv);
  }

  /**
   * ɾ������
   * 
   * @param table
   *          ɾ�����ݵ�table name
   * @param where
   *          ɾ�����ϵ�����
   * @param whereValue
   *          ɾ�����ϵ�����ֵ
   * @return int ɾ���ı���
   */
  public int delete(String table, String where, String[] whereValue)
  {
    SQLiteDatabase db = this.getWritableDatabase();

    return db.delete(table, where, whereValue);
  }

  /**
   * ��������
   * 
   * @param table
   *          �������ݵ�table name
   * @param fields
   *          �������ݵ��ֶ�����
   * @param values
   *          �������ݵ��ֶ�ֵ
   * @param where
   *          ���³����ݵ�����
   * @param whereValue
   *          �������ݵ�����ֵ
   * @return int ���µı���
   */
  public int update(String table, String updateFields[],
      String updateValues[], String where, String[] whereValue)
  {
    SQLiteDatabase db = this.getWritableDatabase();

    /* ���޸ĵ�ֵ����ContentValues */
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

