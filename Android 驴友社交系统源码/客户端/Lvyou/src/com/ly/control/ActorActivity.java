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
     int VIEW_COUNT =15; //用于显示每列15个Item项。
     int index = 0;//用于显示页号的索引
     
     protected void onCreate(Bundle savedInstanceState) {
 		// TODO Auto-generated method stub
 		super.onCreate(savedInstanceState);
 		setContentView(R.layout.actor);
 		lv = (ListView)this.findViewById(R.id.list);
 		
 	   	attentionList=new ArrayList<Map<String, Object>>();
          for(int i=1;i<= 50;i++){
          Map<String, Object> moreAttentionMap = new HashMap<String,Object>(); 
          moreAttentionMap.put("m_AttentionPerson", "王麻子"+""+i);
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
		  //索引值小于等于0，表示不能向前翻页了，已经到了第一页了。
		  //将向前翻页的按钮设为不可用。
		  if(index <=0){
		    btnleft.setEnabled(false);
		  }
		   /**值的长度减去前几页的长度，剩下的就是这一页的长度，
		    * 如果这一页的长度比View_Count小，
		    * 表示这是最后的一页了，后面在没有了。*/
		   //将向后翻页的按钮设为不可用。
		  else if(attentionList.size() - index*VIEW_COUNT <= VIEW_COUNT){
		     btnright.setEnabled(false);
		  }
		  //否则将2个按钮都设为可用的。
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
		  //刷新ListView里面的数值。
		   ma.notifyDataSetChanged();
		    checkButton();
		 }
	 private void leftView() { 
		  index--;
		  //刷新ListView里面的数值。
		  ma.notifyDataSetChanged();
		  //检查Button是否可用。
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
		   //设置每一页的长度，默认的是View_Count的值。
		  public int getCount() { 
		   
		   //ori表示到目前为止的前几页的总共的个数。
		   int ori = VIEW_COUNT * index;
		   //值的总个数-前几页的个数就是这一页要显示的个数，如果比默认的值小，说明这是最后一页，只需显示这么多就可以了
		   if(attentionList.size()- ori < VIEW_COUNT ){
		    return attentionList.size() - ori;
		   }
		   //如果比默认的值还要大，说明一页显示不完，还要用换一页显示，这一页用默认的值显示满就可以了。
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
			    /**使用newlistview.xml为每一个item的Layout取得Id*/
			    LayoutInflater mInflater = LayoutInflater.from(context);
			    convertView = mInflater.inflate(R.layout.moreattentionlistview, null);
			    holder = new ViewHolder();
			    /**实例化具体的控件*/
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