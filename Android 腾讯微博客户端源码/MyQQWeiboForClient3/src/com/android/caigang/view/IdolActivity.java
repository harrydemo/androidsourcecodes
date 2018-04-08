package com.android.caigang.view;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.caigang.R;
import com.android.caigang.db.DataHelper;
import com.android.caigang.model.UserInfo;
import com.android.caigang.util.AsyncImageLoader;
import com.android.caigang.util.TimeUtil;
import com.android.caigang.util.AsyncImageLoader.ImageCallback;
import com.mime.qweibo.examples.MyWeiboSync;

public class IdolActivity extends ListActivity implements OnItemClickListener{

	private DataHelper dataHelper;
	private UserInfo user;
	private MyWeiboSync weibo;
	private Handler handler;
	private AsyncImageLoader asyncImageLoader; 
	private IdolThread thread;
	private ProgressDialog progressDialog;
	private JSONArray array;
	private IdolAdapter adapter;
	private ListView listView;
	private String name;
	private String currentNick;//��ǰ������ǳ�
	private View top_panel;
	private Button top_btn_left;
	private Button top_btn_right;
	private TextView top_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idol);
		setUpViews();//����view
		setUpListeners();//����listenter
		
		asyncImageLoader = new AsyncImageLoader();
		dataHelper = new DataHelper(IdolActivity.this);
		weibo = new MyWeiboSync();
		List<UserInfo> userList = dataHelper.GetUserList(false);
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		String nick = preferences.getString("user_default_nick", "");
		if (nick != "") {
			user = dataHelper.getUserByName(nick,userList);
		}
		weibo.setAccessTokenKey(user.getToken());
		weibo.setAccessTokenSecrect(user.getTokenSecret());
		
		Intent intent = getIntent();
		name = intent.getStringExtra("name");//��ȡ��ǰ��ҳ�洫�ݹ���������
		currentNick = intent.getStringExtra("nick");
		top_title.setText(currentNick+"��ż��");
		
		progressDialog = new ProgressDialog(IdolActivity.this);// ����һ��������
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("���Ե�");
		progressDialog.setMessage("���ڶ�ȡ������!");
		
		handler = new IdolHandler();
		thread = new IdolThread();
		thread.start();//����һ���̻߳�ȡ����
		progressDialog.show();
	}
	
	private void setUpViews(){
		listView = getListView();
		top_panel = (View)findViewById(R.id.idol_top);
		top_btn_left = (Button)top_panel.findViewById(R.id.top_btn_left);
		top_btn_right = (Button)top_panel.findViewById(R.id.top_btn_right);
		top_title = (TextView)top_panel.findViewById(R.id.top_title);
	}
	
	private void setUpListeners(){
		listView.setOnItemClickListener(this);
	}
	
	class IdolThread extends Thread {
		@Override
		public void run() {
			String jsonStr = weibo.getIdols(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), 20, 0, name);
			try {
				JSONObject dataObj = new JSONObject(jsonStr).getJSONObject("data");
				array = dataObj.getJSONArray("info");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//֪ͨhandler��������
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}
	
	class IdolHandler extends Handler { 
		@Override
		public void handleMessage(Message msg){
			adapter = new IdolAdapter(IdolActivity.this, array);
			listView.setAdapter(adapter);
			progressDialog.dismiss();// �رս�����
		}
	}
	
	class IdolAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private JSONArray array;
		
		public IdolAdapter(Context context, JSONArray array) {
			super();
			this.context = context;
			this.array = array;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return array.length();
		}

		@Override
		public Object getItem(int position) {
			return array.opt(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			asyncImageLoader = new AsyncImageLoader();
			IdolViewHolder viewHolder = new IdolViewHolder();
			JSONObject data = (JSONObject)array.opt(position);
			convertView = inflater.inflate(R.layout.idol_list_item, null);
			
			viewHolder.idol_headicon = (ImageView) convertView.findViewById(R.id.idol_headicon);
			viewHolder.idol_nick = (TextView) convertView.findViewById(R.id.idol_nick);
			viewHolder.idol_name = (TextView) convertView.findViewById(R.id.idol_name);
			
			Drawable cachedImage = null;
			if(data!=null){
				try {
					convertView.setTag(data.get("name"));
					viewHolder.idol_nick.setText(data.getString("nick"));
					viewHolder.idol_name.setText("@"+data.getString("name"));
					//�첽����ͼƬ
					cachedImage = asyncImageLoader.loadDrawable(data.getString("head")+"/100",viewHolder.idol_headicon, new ImageCallback(){
	                    @Override
	                    public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
	                        imageView.setImageDrawable(imageDrawable);
	                    }
	                });
					if(cachedImage == null) {
						viewHolder.idol_headicon.setImageResource(R.drawable.icon);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return convertView;
		}
	}
	
	static class IdolViewHolder {
		private ImageView idol_headicon;
		private TextView idol_nick;
		private TextView idol_name;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent = new Intent(IdolActivity.this,UserInfoActivity.class);
		try {
			JSONObject fansInfo = (JSONObject)array.opt(position);
			JSONArray tweets = fansInfo.getJSONArray("tweet");
			JSONObject tweet = null;
			if(tweets!=null&&tweets.length()>0){
				tweet = (JSONObject)tweets.opt(0);
				intent.putExtra("origtext", tweet.getString("text"));
				intent.putExtra("timestamp", TimeUtil.getStandardTime(tweet.getLong("timestamp")));
			}
			intent.putExtra("name", fansInfo.getString("name"));
			intent.putExtra("nick", fansInfo.getString("nick"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		startActivity(intent);//��ת���û���Ϣ����
	}
}
