package com.php;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Json2Lv extends Activity {
	private ListView lv;
	private TextView view_result;
	String jsonData="";
	HttpResponse httpResponse = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        lv =(ListView) findViewById(R.id.listView1);
        view_result = (TextView) findViewById(R.id.welcome);
        startUrlCheck();
        //用户登录后，显示欢迎信息
        Bundle bundle=this.getIntent().getExtras();
		String userName=bundle.getString("MAP_USERNAME");
		view_result.setText("欢迎您回来！ "+userName+" ,login success!");
        
        jsonData = jsonData.substring(1, (jsonData.length()-1));
        jsonData = jsonData.replaceAll("\\\\","");
        Pattern p=Pattern.compile("[{\"id\":\"1\",\"title\":\"Windows8\",\"date\":\"2011\",\"address\":\"Nanjing\"},{\"id\":\"2\",\"title\":\"google\",\"date\":\"2008\",\"address\":\"Shenzhen\"},{\"id\":\"3\",\"title\":\"Android\",\"date\":\"2009\",\"address\":\"Beijing\"},{\"id\":\"4\",\"title\":\"IOS\",\"date\":\"2010\",\"address\":\"Hongkong\"}]");
        Matcher m=p.matcher(jsonData);
        System.out.println(m.replaceAll(""));
        
        JsonUtils jsonUtils = new JsonUtils();
        
        LinkedList<User> users = jsonUtils.parseUserFromJson(jsonData);
        
        lv.setAdapter(new ListViewAdapter(Json2Lv.this,users));
    }
    private String startUrlCheck() 
    { 
     HttpClient client = new DefaultHttpClient(); 

     StringBuilder builder = new StringBuilder(); 

     HttpGet myget = new HttpGet("http://10.0.2.2/jqueryRegister02/action.php"); 

		try {
			httpResponse = client.execute(myget);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				jsonData = convertStreamToString(inputStream);

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		} finally {
			client.getConnectionManager().shutdown();
			httpResponse = null;
		}
		return jsonData;

   } 
    
	public static String convertStreamToString(InputStream is) {
		  BufferedReader reader = null;
		  try {
		   reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),//��ֹģ�����ϵ�����
		     512 * 1024);
		  } catch (UnsupportedEncodingException e1) {
	
		   e1.printStackTrace();
		  }
		  StringBuilder sb = new StringBuilder();

		  String line = null;
		  try {
		   while ((line = reader.readLine()) != null) {
		     sb.append(line);
		   }
		  } catch (IOException e) {
		   Log.e("DataProvier convertStreamToString", e.getLocalizedMessage(),
		     e);
		  } finally {
		   try {
		    is.close();
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		  }
		 return sb.toString();
	}
	
class ListViewAdapter extends BaseAdapter{
    	
    	//inflater用于将一个XML文件转换成一个视图（View)
    	
    	LayoutInflater inflater;
    	
    	LinkedList<User> users; 
    	
    	public ListViewAdapter(Context context, LinkedList<User> users){
    		inflater =  LayoutInflater.from(context);
    		this.users =users;
    	}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return users.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return users.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = inflater.inflate(R.layout.list_item, null);
			User user = (User) this.getItem(position);
			TextView lv_id = (TextView) view.findViewById(R.id.uid);
			TextView lv_title = (TextView) view.findViewById(R.id.title);
			TextView lv_date = (TextView) view.findViewById(R.id.date);
			TextView lv_addr = (TextView) view.findViewById(R.id.address);
			
			System.out.println(user.getId());
			System.out.println(user.getTitle());
			
			lv_id.setText("Id-->"+user.getId());
			lv_title.setText("Title-->"+user.getTitle());
			lv_date.setText("Date-->"+user.getDate());
			lv_addr.setText("Address-->"+user.getAddress());
			
			return view;
		}
    }
	
	
	
	
	
	
	
}