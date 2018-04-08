package cn.com.hh.view;

import java.util.List;

import cn.com.hh.domian.UserInfo;
import cn.com.hh.sqlite.DataHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class LoginActivity extends Activity {
	private Dialog dialog;
	private DataHelper dbHelper;
	private List<UserInfo>userList;
	private EditText iconSelect;
	private ImageView icon;
	final static String Select_Name = "Select_Name";
	
	public class UserAdapater extends BaseAdapter{

        public int getCount() {
            return userList.size();
        }

        public Object getItem(int position) {
            return userList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_user, null);
            ImageView iv = (ImageView) convertView.findViewById(R.id.iconImg);
            TextView tv = (TextView) convertView.findViewById(R.id.showName);
            UserInfo user = userList.get(position);
            try {
                //����ͼƬ��ʾ
                iv.setImageDrawable(user.getUserIcon());
                //������Ϣ
                tv.setText(user.getUserName());

                
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		 LinearLayout layout=(LinearLayout)findViewById(R.id.layout);
		 iconSelect = (EditText)findViewById(R.id.iconSelect);
		 icon = (ImageView)findViewById(R.id.icon);
	        //�����Զ���Ӧ
//	        AndroidHelper.AutoBackground(this, layout, R.drawable.bg_v, R.drawable.bg_h);
		 	initUser();
	      
		 	ImageButton iconSelectBtn=(ImageButton)findViewById(R.id.iconSelectBtn);
	    	iconSelectBtn.setOnClickListener(new OnClickListener(){
	    	            public void onClick(View v) {
	    	            	View diaView=View.inflate(LoginActivity.this, R.layout.dialog2, null);
	    	                dialog=new Dialog(LoginActivity.this,R.style.dialog2);
	    	                dialog.setContentView(diaView);
	    	                dialog.show();
	    	                
	    	                UserAdapater adapater = new UserAdapater();
	    	                ListView listview=(ListView)diaView.findViewById(R.id.list);
	    	                listview.setVerticalScrollBarEnabled(false);// ListViewȥ��������
	    	                listview.setAdapter(adapater);
	    	                //�Ե�¼�б�ļ���
	    	                listview.setOnItemClickListener(new OnItemClickListener(){
	    	                	public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
	    	                        TextView tv=(TextView)view.findViewById(R.id.showName);
	    	                        iconSelect.setText(tv.getText());
	    	                        ImageView iv=(ImageView)view.findViewById(R.id.iconImg);
	    	                        icon.setImageDrawable(iv.getDrawable());
	    	                        dialog.dismiss();
	    	                    }
						   	                    
	    	                });
	    	            }
	    	            
	    	        });
	    	ImageButton login=(ImageButton)findViewById(R.id.login);
	        login.setOnClickListener(new OnClickListener(){
	            public void onClick(View v) {
	                GoHome();
	            }
	            
	        });

	}
	private void initUser(){
        //��ȡ�˺��б�
        dbHelper=new DataHelper(this);
        userList = dbHelper.GetUserList(false);
        if(userList.isEmpty())
        {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, AuthorizeActivity.class);
            startActivity(intent);
        }
        else
        {
            SharedPreferences preferences = getSharedPreferences(Select_Name, Activity.MODE_PRIVATE);
            String str= preferences.getString("name", "");
            UserInfo user=null;
            if(str!="")
            {
                user=GetUserByName(str);
            }
            if(user==null)
            {
                user=userList.get(0);
            }
            icon.setImageDrawable(user.getUserIcon());
            iconSelect.setText(user.getUserName());
        }
    }
	
	public static class ConfigHelper {
		public static UserInfo nowUser;
	}

	public UserInfo GetUserByName(String name) {
		for (UserInfo u : userList) {
			if (u.getUserName().equals(name)) {
				return u;
			}
		}
		return null;
	}
	
	//�����û���ҳ
	private void GoHome(){
	        if(userList!=null)
	        {
	            String name=iconSelect.getText().toString();
	            UserInfo u=GetUserByName(name);
	            if(u!=null)
	            {
	                ConfigHelper.nowUser=u;//��ȡ��ǰѡ����û����ұ���
	            }
	        }
	        if(ConfigHelper.nowUser!=null)
	        {
	                        //�����û���ҳ
	            Intent intent = new Intent();
	                    intent.setClass(LoginActivity.this, HomeActivity.class);
	                    startActivity(intent);
	        }
	    }
	

	@Override
    protected void onStop() {
        //���SharedPreferences����
        SharedPreferences MyPreferences = getSharedPreferences(Select_Name, Activity.MODE_PRIVATE);
        //���SharedPreferences.Editor����
        SharedPreferences.Editor editor = MyPreferences.edit();
        //��������е�ֵ
        editor.putString("name", iconSelect.getText().toString());
        editor.commit();
        super.onStop();
    }
	
}
