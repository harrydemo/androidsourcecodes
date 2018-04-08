package irdc.ex05_05; 
import android.content.ContentValues; 
import android.content.Context; 
import android.database.Cursor; 
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class ToDoDB extends SQLiteOpenHelper
{
  private final static String DATABASE_NAME = "todo_db";
  private final static int DATABASE_VERSION = 1;
  private final static String TABLE_NAME = "todo_table"; 
  public final static String FIELD_id = "_id";
  public final static String FIELD_TEXT = "todo_text"; 
  public ToDoDB(Context context) 
  { 
    super(context, DATABASE_NAME, null, DATABASE_VERSION); }
  @Override 
  public void onCreate(SQLiteDatabase db)
  {
    /* 建立table */ 
    String sql = "CREATE TABLE " 
      + TABLE_NAME + " (" + FIELD_id +
      " INTEGER primary key autoincrement, "
      + " " + FIELD_TEXT + " text)"; 
    db.execSQL(sql); 
    } 
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  { 
    String sql = "DROP TABLE IF EXISTS " 
      + TABLE_NAME; db.execSQL(sql);
      onCreate(db); 
      }
  public Cursor select() 
  { 
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
    return cursor;
    }
  public long insert(String text)
  { 
    SQLiteDatabase db = this.getWritableDatabase();
    /* 将新增的值放入ContentValues */
    ContentValues cv = new ContentValues();
    cv.put(FIELD_TEXT, text);
    long row = db.insert(TABLE_NAME, null, cv); 
    return row; 
    } 
  public void delete(int id) 
  { 
    SQLiteDatabase db = this.getWritableDatabase();
    String where = FIELD_id + " = ?"; 
    String[] whereValue = { Integer.toString(id) };
    db.delete(TABLE_NAME, where, whereValue); 
    } 
  public void update(int id, String text) 
  { 
    SQLiteDatabase db = this.getWritableDatabase(); 
    String where = FIELD_id + " = ?";
    String[] whereValue = { Integer.toString(id) }; 
    /* 将修改的值放入ContentValues */ 
    ContentValues cv = new ContentValues(); 
    cv.put(FIELD_TEXT, text); 
    db.update(TABLE_NAME, cv, where, whereValue); 
    }
  }