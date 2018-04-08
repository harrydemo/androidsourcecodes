package net.blogjava.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class Main extends Activity implements OnClickListener, TextWatcher
{
	private final String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/dictionary";
	private AutoCompleteTextView actvWord;
	private final String DATABASE_FILENAME = "dictionary.db";
	private SQLiteDatabase database;
	private Button btnSelectWord;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		database = openDatabase();
		btnSelectWord = (Button) findViewById(R.id.btnSelectWord);
		actvWord = (AutoCompleteTextView) findViewById(R.id.actvWord);
		btnSelectWord.setOnClickListener(this);
		actvWord.addTextChangedListener(this);
	}



	public class DictionaryAdapter extends CursorAdapter
	{
		private LayoutInflater layoutInflater;
		@Override
		public CharSequence convertToString(Cursor cursor)
		{
			return cursor == null ? "" : cursor.getString(cursor
					.getColumnIndex("_id"));
		}

		private void setView(View view, Cursor cursor)
		{
			TextView tvWordItem = (TextView) view;
			tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor)
		{
			setView(view, cursor);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			View view = layoutInflater.inflate(R.layout.word_list_item, null);
			setView(view, cursor);
			return view;
		}

		public DictionaryAdapter(Context context, Cursor c, boolean autoRequery)
		{
			super(context, c, autoRequery);
			layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}

	public void afterTextChanged(Editable s)
	{
		
        //  必须将english字段的别名设为_id 
		Cursor cursor = database.rawQuery(
				"select english as _id from t_words where english like ?",
				new String[]
				{ s.toString() + "%" });
		DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
				cursor, true);
		actvWord.setAdapter(dictionaryAdapter);

	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{
		// TODO Auto-generated method stub

	}

	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		// TODO Auto-generated method stub

	}

	public void onClick(View view)
	{
		String sql = "select chinese from t_words where english=?";		
		Cursor cursor = database.rawQuery(sql, new String[]
		{ actvWord.getText().toString() });
		String result = "未找到该单词.";
		//  如果查找单词，显示其中文的意思
		if (cursor.getCount() > 0)
		{
			//  必须使用moveToFirst方法将记录指针移动到第1条记录的位置
			cursor.moveToFirst();
			result = cursor.getString(cursor.getColumnIndex("chinese"));
		}
		//  显示查询结果对话框
		new AlertDialog.Builder(this).setTitle("查询结果").setMessage(result)
				.setPositiveButton("关闭", null).show();

	}

	private SQLiteDatabase openDatabase()
	{
		try
		{
			// 获得dictionary.db文件的绝对路径
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(DATABASE_PATH);
			// 如果/sdcard/dictionary目录中存在，创建这个目录
			if (!dir.exists())
				dir.mkdir();
			// 如果在/sdcard/dictionary目录中不存在
			// dictionary.db文件，则从res\raw目录中复制这个文件到
			// SD卡的目录（/sdcard/dictionary）
			if (!(new File(databaseFilename)).exists())
			{
				// 获得封装dictionary.db文件的InputStream对象
				InputStream is = getResources().openRawResource(
						R.raw.dictionary);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				// 开始复制dictionary.db文件
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
			}
			// 打开/sdcard/dictionary目录中的dictionary.db文件
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
			return database;
		}
		catch (Exception e)
		{
		}
		return null;
	}

}