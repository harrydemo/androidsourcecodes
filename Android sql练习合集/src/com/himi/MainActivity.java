package com.himi;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// ------------第三种保存方式--------《SQLite》---------

/**
 * @author Himi
 * @保存方式：SQLite 轻量级数据库、
 * @优点： 可以将自己的数据存储到文件系统或者数据库当中， 也可以将自己的数据存
 *         储到SQLite数据库当中，还可以存到SD卡中
 * @注意1：数据库对于一个游戏(一个应用)来说是私有的，并且在一个游戏当中， 
 *         数据库的名字也是唯一的。
 * @注意2 apk中创建的数据库外部的进程是没有权限去读/写的, 
 *         我们需要把数据库文件创建到sdcard上可以解决类似问题.
 * @注意3 当你删除id靠前的数据或者全部删除数据的时候，SQLite不会自动排序，
 *        也就是说再添加数据的时候你不指定id那么SQLite默认还是在原有id最后添加一条新数据
 * @注意4 android 中 的SQLite 语法区分大小写的!!!!!这点要注意！
 *   String UPDATA_DATA = "UPDATE himi SET text='通过SQL语句来修改数据'  WHERE id=1"; 
 *                  千万 不能可以写成
 *   String UPDATA_DATA = "updata himi set text='通过SQL语句来修改数据'  where id=1";
 */
public class MainActivity extends Activity implements OnClickListener {
	private Button btn_addOne, btn_deleteone, btn_check, btn_deleteTable,
			btn_edit, btn_newTable;
	private TextView tv;
	private MySQLiteOpenHelper myOpenHelper;// 创建一个继承SQLiteOpenHelper类实例
	private SQLiteDatabase mysql ; 
//---------------以下两个成员变量是针对在SD卡中存储数据库文件使用
//	private File path = new File("/sdcard/himi");// 创建目录
//	private File f = new File("/sdcard/himi/himi.db");// 创建文件
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		tv = (TextView) findViewById(R.id.tv_title);
		btn_addOne = (Button) findViewById(R.id.sql_addOne);
		btn_check = (Button) findViewById(R.id.sql_check);
		btn_deleteone = (Button) findViewById(R.id.sql_deleteOne);
		btn_deleteTable = (Button) findViewById(R.id.sql_deleteTable);
		btn_newTable = (Button) findViewById(R.id.sql_newTable);
		btn_edit = (Button) findViewById(R.id.sql_edit);
		btn_edit.setOnClickListener(this);
		btn_addOne.setOnClickListener(this);
		btn_check.setOnClickListener(this);
		btn_deleteone.setOnClickListener(this);
		btn_deleteTable.setOnClickListener(this);
		btn_newTable.setOnClickListener(this);
		myOpenHelper = new MySQLiteOpenHelper(this);// 实例一个数据库辅助器
//备注1  ----如果你使用的是将数据库的文件创建在SD卡中，那么创建数据库mysql如下操作：
//		if (!path.exists()) {// 目录存在返回false
//			path.mkdirs();// 创建一个目录
//		}
//		if (!f.exists()) {// 文件存在返回false
//			try {
//				f.createNewFile();//创建文件 
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} 
	}

	@Override
	public void onClick(View v) {  
		try { 
//备注2----如果你使用的是将数据库的文件创建在SD卡中，那么创建数据库mysql如下操作：
//			    mysql = SQLiteDatabase.openOrCreateDatabase(f, null); 
//备注3--- 如果想把数据库文件默认放在系统中,那么创建数据库mysql如下操作：
				mysql = myOpenHelper.getWritableDatabase(); // 实例数据库
			if (v == btn_addOne) {// 添加数据
				// ---------------------- 读写句柄来插入---------
				// ContentValues 其实就是一个哈希表HashMap， key值是字段名称，
				//Value值是字段的值。然后 通过 ContentValues 的 put 方法就可以
				//把数据放到ContentValues中，然后插入到表中去!
				ContentValues cv = new ContentValues();
				cv.put(MySQLiteOpenHelper.TEXT, "Himi测试数据");
				mysql.insert(MySQLiteOpenHelper.TABLE_NAME, null, cv);
				// inser() 第一个参数 标识需要插入操作的表名
				// 第二个参数 :默认传null即可
				// 第三个是插入的数据
				// ---------------------- SQL语句插入--------------
				// String INSERT_DATA =
				// "INSERT INTO himi (id,text) values (1, '通过SQL语句插入')";
				// db.execSQL(INSERT_DATA);
				tv.setText("添加数据成功！点击查看数据库查询");
			} else if (v == btn_deleteone) {// 删除数据
				// ---------------------- 读写句柄来删除
				mysql.delete("himi", MySQLiteOpenHelper.ID + "=1", null);
				// 第一个参数 需要操作的表名
				// 第二个参数为 id+操作的下标 如果这里我们传入null，表示全部删除
				// 第三个参数默认传null即可
				// ----------------------- SQL语句来删除
				// String DELETE_DATA = "DELETE FROM himi WHERE id=1";
				// db.execSQL(DELETE_DATA);
				tv.setText("删除数据成功！点击查看数据库查询");
			} else if (v == btn_check) {// 遍历数据
//备注4------
				Cursor cur = mysql.rawQuery("SELECT * FROM "
						+ MySQLiteOpenHelper.TABLE_NAME, null);
				if (cur != null) {
					String temp = "";
					int i = 0;
					while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
						temp += cur.getString(0); 
						// 参数0 指的是列的下标,这里的0指的是id列
						temp += cur.getString(1);
						// 这里的0相对于当前应该是咱们的text列了
						i++;
						temp += "  "; // 这里是我整理显示格式 ,呵呵~
						if (i % 3 == 0) // 这里是我整理显示格式 ,呵呵~
							temp += "\n";// 这里是我整理显示格式 ,呵呵~
					}
					tv.setText(temp);
				}
			} else if (v == btn_edit) {// 修改数据
				// ------------------------句柄方式来修改 -------------
				ContentValues cv = new ContentValues();
				cv.put(MySQLiteOpenHelper.TEXT, "修改后的数据");
				mysql.update("himi", cv, "id " + "=" + Integer.toString(3), null);
				// ------------------------SQL语句来修改 -------------
				// String UPDATA_DATA =
				// "UPDATE himi SET text='通过SQL语句来修改数据'  WHERE id=1";
				// db.execSQL(UPDATA_DATA);
				tv.setText("修改数据成功！点击查看数据库查询");
			} else if (v == btn_deleteTable) {// 删除表
				mysql.execSQL("DROP TABLE himi");
				tv.setText("删除表成功！点击查看数据库查询");
			} else if (v == btn_newTable) {// 新建表
				String TABLE_NAME = "himi";
				String ID = "id";
				String TEXT = "text";
				String str_sql2 = "CREATE TABLE " + TABLE_NAME + "(" + ID
						+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TEXT
						+ " text );";
				mysql.execSQL(str_sql2);
				tv.setText("新建表成功！点击查看数据库查询");
			}
			// 删除数据库:
			// this.deleteDatabase("himi.db");
		} catch (Exception e) {
			tv.setText("操作失败！");
		} finally {// 如果try中异常，也要对数据库进行关闭
			mysql.close();
		}
	}
}

//
// 备注1:
// 在Android中查询数据是通过Cursor类来实现的，当我们使用SQLiteDatabase.query()方法时，
// 会得到一个Cursor对象，Cursor指向的就是每一条数据。它提供了很多有关查询的方法，具体方法如下：
//
// 方法 说明
// move 以当前的位置为参考，将Cursor移动到指定的位置，成功返回true, 失败返回false
//
// moveToPosition 将Cursor移动到指定的位置，成功返回true,失败返回false
//
// moveToNext 将Cursor向前移动一个位置，成功返回true,失败返回false
//
// moveToLast 将Cursor向后移动一个位置，成功返回true,失败返回 false。
//
// movetoFirst 将Cursor移动到第一行，成功返回true,失败返回false
//
// isBeforeFirst 返回Cursor是否指向第一项数据之前
//
// isAfterLast 返回Cursor是否指向最后一项数据之后
//   
// isClosed 返回Cursor是否关闭
//
// isFirst 返回Cursor是否指向第一项数据
//
// isLast 返回Cursor是否指向最后一项数据
//
// isNull 返回指定位置的值是否为null
//
// getCount 返回总的数据项数
//
// getInt 返回当前行中指定的索引数据
