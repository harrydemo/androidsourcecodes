package com.ly.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ly.control.R;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ActorActivity extends Activity implements OnClickListener{
	 private Button btnleft,btnright;
     private ListView lv;
     MoreAdapter ma;
     ArrayList<Map<String, Object>> attentionList;
     int VIEW_COUNT =15; //������ʾÿ��15��Item�
     int index = 0;//������ʾҳ�ŵ�����
     
     protected void onCreate(Bundle savedInstanceState) {
 		// TODO Auto-generated method stub
 		super.onCreate(savedInstanceState);
 		setContentView(R.layout.actor);
 		lv = (ListView)this.findViewById(R.id.list);
 		
 	   	attentionList=new ArrayList<Map<String, Object>>();
          for(int i=1;i<= 50;i++){
          Map<String, Object> moreAttentionMap = new HashMap<String,Object>(); 
          moreAttentionMap.put("m_AttentionPerson", "������"+""+i);
          moreAttentionMap.put("m_AttentionImag", R.drawable.pic1);
          attentionList.add(moreAttentionMap);
       }
          ma = new MoreAdapter(this, attentionList);
          lv.setAdapter(ma);
          
          btnleft = (Button)this.findViewById(R.id.btnLeft);
          btnright = (Button)this.findViewById(R.id.btnRight); 
          btnleft.setOnClickListener(this);
          btnright.setOnClickListener(this);
          checkButton();
 	}
     
     private void checkButton() { 
		  //����ֵС�ڵ���0����ʾ������ǰ��ҳ�ˣ��Ѿ����˵�һҳ�ˡ�
		  //����ǰ��ҳ�İ�ť��Ϊ�����á�
		  if(index <=0){
		    btnleft.setEnabled(false);
		  }
		   /**ֵ�ĳ��ȼ�ȥǰ��ҳ�ĳ��ȣ�ʣ�µľ�����һҳ�ĳ��ȣ�
		    * �����һҳ�ĳ��ȱ�View_CountС��
		    * ��ʾ��������һҳ�ˣ�������û���ˡ�*/
		   //�����ҳ�İ�ť��Ϊ�����á�
		  else if(attentionList.size() - index*VIEW_COUNT <= VIEW_COUNT){
		     btnright.setEnabled(false);
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
		  }
		  
		  public MoreAdapter(ActorActivity listIndexPage, ArrayList<Map<String, Object>> attentionList) { 
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
			    convertView.setTag(holder);
			   }else {
			    holder = (ViewHolder) convertView.getTag();
			   }
			   holder.attentionPerson .setText((String)attentionList.get(position+index*VIEW_COUNT).get("m_AttentionPerson"));
			   holder.attentinImag.setImageResource((Integer)attentionList.get(position+index*VIEW_COUNT).get("m_AttentionImag"));
			   return convertView;
		}
}
}