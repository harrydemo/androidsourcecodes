package com.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 本例实现SQLite数据库增加、删除、修改、模糊查询操作。这里不是最好的实现方法,
 * 如想研究SQL如何封装，请详细查看SQLiteDatebase类.
 * 查看SQL语句：String sql = SQLiteQueryBuilder.buildQueryString();
 * 希望机友们多交流心得!
 * @author ytm0220@163.com
 */
public class SQLDemo extends Activity {
	private static String DB_NAME = "mycity.db";
	private static int DB_VERSION = 1;
	private static int POSTION;
	private ListView listview;
	private Cursor cursor;
	private SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private ListAdapter listAdapter;
	
	private EditText etCity;
	private EditText etCode;
	private Button bt_add;
	private Button bt_modify;
	private Button bt_query;
	
	private List<CityBean> cityList = new ArrayList<CityBean>();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
    	etCity = (EditText) findViewById(R.id.etCity);
    	etCode = (EditText) findViewById(R.id.etCode);
    	bt_add = (Button) findViewById(R.id.bt_add);
    	bt_modify = (Button) findViewById(R.id.bt_modify);
    	bt_query = (Button) findViewById(R.id.bt_query);
    	
    	try{
    		/* 初始化并创建数据库 */
    		dbHelper = new SQLiteHelper(this, DB_NAME, null, DB_VERSION);
    		/* 创建表 */
    		db = dbHelper.getWritableDatabase();	//调用SQLiteHelper.OnCreate()        	
        	/* 查询表，得到cursor对象 */
        	cursor = db.query(SQLiteHelper.TB_NAME, null, null, null, null, null, CityBean.CODE + " DESC");
        	cursor.moveToFirst();
        	while(!cursor.isAfterLast() && (cursor.getString(1) != null)){    
        		CityBean city = new CityBean();
        		city.setId(cursor.getString(0));
        		city.setCity(cursor.getString(1));
        		city.setCode(cursor.getString(2));
        		cityList.add(city);
        		cursor.moveToNext();
        	}
    	}catch(IllegalArgumentException e){
    		//当用SimpleCursorAdapter装载数据时，表ID列必须是_id，否则报错column '_id' does not exist
    		e.printStackTrace();
    		//当版本变更时会调用SQLiteHelper.onUpgrade()方法重建表 注：表以前数据将丢失
    		++ DB_VERSION;
    		dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
//    		dbHelper.updateColumn(db, SQLiteHelper.ID, "_"+SQLiteHelper.ID, "integer");
    	}
    	listview = (ListView)findViewById(R.id.listView);
    	listAdapter = new ListAdapter();
    	listview.setAdapter(listAdapter);
    	listview.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int postion,
					long arg3) {
				setSelectedValues(postion);
			}    		
    	});
    	
    	/* 插入表数据并ListView显示更新 */
    	bt_add.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(etCity.getText().length() > 1 && etCode.getText().length() >1){
					ContentValues values = new ContentValues();
					values.put(CityBean.CITY, etCity.getText().toString().trim());
					values.put(CityBean.CODE, etCode.getText().toString().trim());
					//插入数据 用ContentValues对象也即HashMap操作,并返回ID号
					Long cityID = db.insert(SQLiteHelper.TB_NAME, CityBean.ID, values);
					CityBean city = new CityBean();
					city.setId(""+cityID);
	        		city.setCity(etCity.getText().toString().trim());
	        		city.setCode(etCode.getText().toString().trim());
	        		cityList.add(city);
	        		listview.setAdapter(new ListAdapter());
	        		resetForm();
				}
			}
		});
    	
    	/* 查询表，模糊条件查询 */
    	bt_query.setOnClickListener(new Button.OnClickListener(){
    		@Override
    		public void onClick(View view) {
    			cityList.removeAll(cityList);
    			String sql = null;
    			String sqlCity = etCity.getText().length() > 0 ? CityBean.CITY + " like '%" + etCity.getText().toString().trim() + "%'" : "";    			
    			String sqlCode = etCode.getText().length() > 0 ? CityBean.CITY + " like '%" + etCity.getText().toString().trim() + "%'" : "";
    			if( (!"".equals(sqlCity)) && (!"".equals(sqlCode)) ){
    				sql = sqlCity + " and" + sqlCode;
    			}else if(!"".equals(sqlCity)){
    				sql = sqlCity;
    			}else if(!"".equals(sqlCode)){
    				sql = sqlCode;
    			}
    			cursor = db.query(true, SQLiteHelper.TB_NAME, 
    					new String[]{CityBean.ID, CityBean.CITY, CityBean.CODE}, 
    					sql, 
    					null, null, null, null, null);
    			cursor.moveToFirst();
    			while(!cursor.isAfterLast() && (cursor.getString(1) != null)){  
    				CityBean city = new CityBean();
            		city.setId(cursor.getString(0));
            		city.setCity(cursor.getString(1));
            		city.setCode(cursor.getString(2));
            		cityList.add(city);
    				cursor.moveToNext();
    			}
    			listview.setAdapter(new ListAdapter());
    			resetForm();
    		}
    	});
    	
    	/* 修改表数据 */
    	bt_modify.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ContentValues values = new ContentValues();
				values.put(CityBean.CITY, etCity.getText().toString().trim());
				values.put(CityBean.CODE, etCode.getText().toString().trim());
				db.update(SQLiteHelper.TB_NAME, values, CityBean.ID + "=" + cityList.get(POSTION).getId(), null);
				cityList.get(POSTION).setCity(etCity.getText().toString().trim());
				cityList.get(POSTION).setCode(etCode.getText().toString().trim());
				listview.setAdapter(new ListAdapter());
				resetForm();
			}
		});
    }
    
    /* 设置选中ListView的值 */
    public void setSelectedValues(int postion){
    	POSTION = postion;
		etCity.setText(cityList.get(postion).getCity());
		etCode.setText(cityList.get(postion).getCode());
    }
    
    /* 重值form */
    public void resetForm(){
		etCity.setText("");
		etCode.setText("");
    }
    
    @Override
    protected void onDestroy() {
    	db.delete(SQLiteHelper.TB_NAME, null, null);
    	super.onDestroy();
    }
    
    private class ListAdapter extends BaseAdapter{
    	public ListAdapter(){
    		super();
    	}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cityList.size();
		}

		@Override
		public Object getItem(int postion) {
			// TODO Auto-generated method stub
			return postion;
		}

		@Override
		public long getItemId(int postion) {
			// TODO Auto-generated method stub
			return postion;
		}

		@Override
		public View getView(final int postion, View view, ViewGroup parent) {
			view = getLayoutInflater().inflate(R.layout.listview, null);
			TextView tv = (TextView) view.findViewById(R.id.tvCity);
			tv.setText("" + cityList.get(postion).getCity());
			TextView bu = (TextView) view.findViewById(R.id.btRemove);
			bu.setText(R.string.delete);
			bu.setId(Integer.parseInt(cityList.get(postion).getId()));
			
			/* 删除表数据 */
			bu.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View view) {
					try{
						db.delete(SQLiteHelper.TB_NAME, CityBean.ID + "=" + view.getId(), null);
						cityList.remove(postion);
						listview.setAdapter(new ListAdapter());						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			return view;
		}
    }
}