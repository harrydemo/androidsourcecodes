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
 * ����ʵ��SQLite���ݿ����ӡ�ɾ�����޸ġ�ģ����ѯ���������ﲻ����õ�ʵ�ַ���,
 * �����о�SQL��η�װ������ϸ�鿴SQLiteDatebase��.
 * �鿴SQL��䣺String sql = SQLiteQueryBuilder.buildQueryString();
 * ϣ�������Ƕཻ���ĵ�!
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
    		/* ��ʼ�����������ݿ� */
    		dbHelper = new SQLiteHelper(this, DB_NAME, null, DB_VERSION);
    		/* ������ */
    		db = dbHelper.getWritableDatabase();	//����SQLiteHelper.OnCreate()        	
        	/* ��ѯ���õ�cursor���� */
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
    		//����SimpleCursorAdapterװ������ʱ����ID�б�����_id�����򱨴�column '_id' does not exist
    		e.printStackTrace();
    		//���汾���ʱ�����SQLiteHelper.onUpgrade()�����ؽ��� ע������ǰ���ݽ���ʧ
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
    	
    	/* ��������ݲ�ListView��ʾ���� */
    	bt_add.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(etCity.getText().length() > 1 && etCode.getText().length() >1){
					ContentValues values = new ContentValues();
					values.put(CityBean.CITY, etCity.getText().toString().trim());
					values.put(CityBean.CODE, etCode.getText().toString().trim());
					//�������� ��ContentValues����Ҳ��HashMap����,������ID��
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
    	
    	/* ��ѯ��ģ��������ѯ */
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
    	
    	/* �޸ı����� */
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
    
    /* ����ѡ��ListView��ֵ */
    public void setSelectedValues(int postion){
    	POSTION = postion;
		etCity.setText(cityList.get(postion).getCity());
		etCode.setText(cityList.get(postion).getCode());
    }
    
    /* ��ֵform */
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
			
			/* ɾ�������� */
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