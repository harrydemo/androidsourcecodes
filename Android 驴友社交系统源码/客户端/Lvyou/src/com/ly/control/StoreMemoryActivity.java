package com.ly.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ly.control.R;
import com.ly.db.DBHelper;
import com.ly.db.DBHelper2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class StoreMemoryActivity extends Activity implements OnClickListener{
     private Button btnleft,btnright;
     private ListView lv;
    
     
     private String s,s1,id;
     MoreAdapter ma;
     DBHelper2 db;
     SQLiteDatabase sd;
     SimpleCursorAdapter sa;
     Cursor c;
     private String title,time,address,content;
     private ArrayList<String> list = new ArrayList<String>();
    
     ArrayList<Map<String, Object>> attentionList;
     int VIEW_COUNT =5; //������ʾÿ��15��Item�
     int index = 0;//������ʾҳ�ŵ�����
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storememory);
		lv = (ListView)this.findViewById(R.id.list);
		id = getIntent().getStringExtra("result");
		
		
		db = new DBHelper2(StoreMemoryActivity.this);
		sd = db.getReadableDatabase();
		 c= sd.rawQuery("select memory_id _id,memory_title,memory_time,memory_address,memory_content from "+DBHelper2.table_name+" where m_u_id= "+id+"", null);
		
		 attentionList=new ArrayList<Map<String, Object>>();
	   	
	    for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		 {	title = c.getString(1);
		 	time = c.getString(2);
		 	address = c.getString(3);
		 	content = c.getString(4);
			 Map<String, Object> moreAttentionMap = new HashMap<String,Object>(); 
	         moreAttentionMap.put("m_AttentionPerson", title);
	         moreAttentionMap.put("m_AttentionImag", R.drawable.ballon);
	         moreAttentionMap.put("time", time);
	         moreAttentionMap.put("address", address);
	         moreAttentionMap.put("content", content);
	         
	         attentionList.add(moreAttentionMap);
	         
		 }
			
         ma = new MoreAdapter(this, attentionList);
         lv.setAdapter(ma);
         
         btnleft = (Button)this.findViewById(R.id.btnLeft);
         btnright = (Button)this.findViewById(R.id.btnRight); 
         btnleft.setOnClickListener(this);
         btnright.setOnClickListener(this);
         checkButton();
         lv.setOnItemClickListener(listener);
         
         sd.close();
			db.close();
	}
	private OnItemClickListener listener = new OnItemClickListener() {

	
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(StoreMemoryActivity.this,LYAddMemoryActivity.class);
			String ss = (String) attentionList.get(arg2+index*VIEW_COUNT).get("m_AttentionPerson");
			String ss1 = (String) attentionList.get(arg2+index*VIEW_COUNT).get("address");
			String ss2 = (String) attentionList.get(arg2+index*VIEW_COUNT).get("content");
			intent.putExtra("nll", "1");
			
			intent.putExtra("title", ss);
			intent.putExtra("address", ss1);
			intent.putExtra("content", ss2);
			intent.putExtra("result", id);
			startActivity(intent);
		}
	};
	 private void checkButton() { 
		  //����ֵС�ڵ���0����ʾ������ǰ��ҳ�ˣ��Ѿ����˵�һҳ�ˡ�
		  //����ǰ��ҳ�İ�ť��Ϊ�����á�
		  if(index <=0){
		    btnleft.setEnabled(false);
		    btnright.setEnabled(true);
		  }
		   /**ֵ�ĳ��ȼ�ȥǰ��ҳ�ĳ��ȣ�ʣ�µľ�����һҳ�ĳ��ȣ�
		    * �����һҳ�ĳ��ȱ�View_CountС��
		    * ��ʾ��������һҳ�ˣ�������û���ˡ�*/
		   //�����ҳ�İ�ť��Ϊ�����á�
		  else if(attentionList.size() - index*VIEW_COUNT <= VIEW_COUNT){
		     btnright.setEnabled(false);
		     btnleft.setEnabled(true);
		  }
		  //����2����ť����Ϊ���õġ�
		   else {
		    btnleft.setEnabled(true);
		    btnright.setEnabled(true);
		   }
		 } 
	 public void onClick(View v) { 
		    switch(v.getId()){
		    case R.id.btnLeft:
		     leftView();
		     break;
		    case R.id.btnRight:
		     rightView();
		     break;
		    }
		  
		 }
	private void rightView() { 
		   index++;
		  //ˢ��ListView�������ֵ��
		   ma.notifyDataSetChanged();
		    checkButton();
		 }
	 private void leftView() { 
		  index--;
		  //ˢ��ListView�������ֵ��
		  ma.notifyDataSetChanged();
		  //���Button�Ƿ���á�
		  checkButton();
		 }
	 public class MoreAdapter extends BaseAdapter {
		  
		  private Context context;
		  private ArrayList<Map<String, Object>> attentionList;
		  
		  class ViewHolder {
		   ImageView attentinImag; 
		   TextView attentionPerson;
		   TextView tx;
		  }
		  
		  public MoreAdapter(StoreMemoryActivity listIndexPage, ArrayList<Map<String, Object>> attentionList) { 
		   context = listIndexPage;
		   this.attentionList = attentionList;
		  }
		   //����ÿһҳ�ĳ��ȣ�Ĭ�ϵ���View_Count��ֵ��
		  public int getCount() { 
		   
		   //ori��ʾ��ĿǰΪֹ��ǰ��ҳ���ܹ��ĸ�����
		   int ori = VIEW_COUNT * index;
		   //ֵ���ܸ���-ǰ��ҳ�ĸ���������һҳҪ��ʾ�ĸ����������Ĭ�ϵ�ֵС��˵���������һҳ��ֻ����ʾ��ô��Ϳ�����
		   if(attentionList.size()- ori < VIEW_COUNT ){
		    return attentionList.size() - ori;
		   }
		   //�����Ĭ�ϵ�ֵ��Ҫ��˵��һҳ��ʾ���꣬��Ҫ�û�һҳ��ʾ����һҳ��Ĭ�ϵ�ֵ��ʾ���Ϳ����ˡ�
		    else {
		     return VIEW_COUNT;
		    }

		  }
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 ViewHolder holder;
			   if(convertView == null){
			    /**ʹ��newlistview.xmlΪÿһ��item��Layoutȡ��Id*/
			    LayoutInflater mInflater = LayoutInflater.from(context);
			    convertView = mInflater.inflate(R.layout.moreattentionlistview, null);
			    holder = new ViewHolder();
			    /**ʵ��������Ŀؼ�*/
			    holder.attentinImag = (ImageView) convertView.findViewById(R.id.moreAttentionImage);
			    holder.attentionPerson = (TextView) convertView.findViewById(R.id.moreAttentionPerson);
			    holder.tx = (TextView) convertView.findViewById(R.id.TextView01);
			    convertView.setTag(holder);
			   }else {
			    holder = (ViewHolder) convertView.getTag();
			   }
			   holder.attentionPerson .setText((String)attentionList.get(position+index*VIEW_COUNT).get("m_AttentionPerson"));
			   holder.tx .setText((String)attentionList.get(position+index*VIEW_COUNT).get("time"));
			   holder.attentinImag.setImageResource((Integer)attentionList.get(position+index*VIEW_COUNT).get("m_AttentionImag"));
			   return convertView;
		}
	 }
}