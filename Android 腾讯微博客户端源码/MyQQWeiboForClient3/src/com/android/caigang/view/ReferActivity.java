package com.android.caigang.view;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.caigang.R;
import com.android.caigang.db.DataHelper;
import com.android.caigang.model.UserInfo;
import com.android.caigang.util.AsyncImageLoader;
import com.android.caigang.util.AsyncImageLoader.ImageCallback;
import com.android.caigang.util.DataBaseContext;
import com.android.caigang.util.TimeUtil;
import com.android.caigang.util.WeiboContext;
import com.mime.qweibo.examples.MyWeiboSync;
import com.mime.qweibo.examples.QWeiboType.PageFlag;

public class ReferActivity extends ListActivity implements OnItemClickListener,OnItemLongClickListener{
	
	private DataHelper dataHelper;
	private UserInfo user;
	private MyWeiboSync weibo;
	private ListView listView;
	private ReferAdapter adapter;
	private JSONArray array;
	private AsyncImageLoader asyncImageLoader;
	private Handler handler;
	private ProgressDialog progressDialog;
	private View top_panel;
	private Button top_btn_left;
	private Button top_btn_right;
	private TextView top_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refer);
		setUpViews();
		setUpListeners();
		
		dataHelper = DataBaseContext.getInstance(getApplicationContext());
		weibo = WeiboContext.getInstance();
		
		List<UserInfo> userList = dataHelper.GetUserList(false);
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		String nick = preferences.getString("user_default_nick", "");
		
		if (nick != "") {
			user = dataHelper.getUserByName(nick,userList);
			top_title.setText("提到我的");//顶部工具栏名称
		}
		
		/*weibo.setAccessTokenKey(user.getToken());
		weibo.setAccessTokenSecrect(user.getTokenSecret());*/
		
		progressDialog = new ProgressDialog(ReferActivity.this);// 生成一个进度条
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("请稍等");
		progressDialog.setMessage("正在读取数据中!");
		handler = new GetReferHandler();
		
		new GetReferThread().start();//耗时操作,开启一个新线程获取数据
		progressDialog.show();
	}
	
	private void setUpViews(){
		listView = getListView();
		top_panel = (View)findViewById(R.id.refer_top);
		top_btn_left = (Button)top_panel.findViewById(R.id.top_btn_left);
		top_btn_right = (Button)top_panel.findViewById(R.id.top_btn_right);
		top_title = (TextView)top_panel.findViewById(R.id.top_title);
	}
	
	private void setUpListeners(){
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}
	
	class GetReferHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(array!=null){
				adapter = new ReferAdapter(ReferActivity.this, array);
				listView.setAdapter(adapter);
			}
			
			progressDialog.dismiss();// 关闭进度条
		}
	}
	
	class GetReferThread extends Thread {
		@Override
		public void run() {
			String jsonStr = weibo.getRefers(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), PageFlag.PageFlag_First, 0, 20, 0);
			try {
				JSONObject dataObj = new JSONObject(jsonStr).getJSONObject("data");
				if(dataObj!=null){
					array = dataObj.getJSONArray("info");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}
	
	
	class ReferAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private JSONArray array;
		
		public ReferAdapter(Context context, JSONArray array) {
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
			ReferViewHolder viewHolder = new ReferViewHolder();
			JSONObject data = (JSONObject)array.opt(position);
			JSONObject source = null;
			convertView = inflater.inflate(R.layout.refer_list_item, null);
			try {
				source = data.getJSONObject("source");
			} catch (JSONException e1) {
				e1.printStackTrace(); 
			}
			viewHolder.refer_headicon = (ImageView) convertView.findViewById(R.id.refer_headicon);
			viewHolder.refer_nick = (TextView) convertView.findViewById(R.id.refer_nick);
			viewHolder.refer_hasimage = (ImageView) convertView.findViewById(R.id.refer_hasimage);
			viewHolder.refer_timestamp = (TextView) convertView.findViewById(R.id.refer_timestamp);
			viewHolder.refer_origtext = (TextView) convertView.findViewById(R.id.refer_origtext);
			viewHolder.refer_source = (TextView) convertView.findViewById(R.id.refer_source);
			
			if(data!=null){
				try {
					convertView.setTag(data.get("id"));
					viewHolder.refer_nick.setText(data.getString("nick"));
					viewHolder.refer_timestamp.setText(TimeUtil.converTime(Long.parseLong(data.getString("timestamp"))));
					viewHolder.refer_origtext.setText(data.getString("origtext"), TextView.BufferType.SPANNABLE);
					
					if(source!=null){
						viewHolder.refer_source.setText(source.getString("nick")+":"+source.getString("origtext"));
						viewHolder.refer_source.setBackgroundResource(R.drawable.source_bg);
					}
					//异步加载图片
					Drawable cachedImage = asyncImageLoader.loadDrawable(data.getString("head")+"/100",viewHolder.refer_headicon, new ImageCallback(){
	                    @Override
	                    public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
	                        imageView.setImageDrawable(imageDrawable);
	                    }
	                });
					if (cachedImage == null) {
						viewHolder.refer_headicon.setImageResource(R.drawable.icon);
					} else {
						viewHolder.refer_headicon.setImageDrawable(cachedImage);
					}
					if(data.getJSONArray("image")!=null){
						viewHolder.refer_hasimage.setImageResource(R.drawable.hasimage);
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
	
	static class ReferViewHolder {
		private ImageView refer_headicon;
		private TextView refer_nick;
		private TextView refer_timestamp;
		private TextView refer_origtext;
		private TextView refer_source;
		private ImageView refer_hasimage;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
		CharSequence [] items = null;
		try {
			items= new CharSequence[]{"转播","对话","点评","收藏",((JSONObject)array.opt(position)).getString("nick"),"取消"};
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AlertDialog.Builder(ReferActivity.this).setTitle("选项").setItems(items,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: {
						}
							break;
						case 1: {
						}
							break;
						case 2: {
						}
							break;
						case 3: {
						}
							break;
						case 4: {
						}
							break;
						case 5: {
						}
							break;
						default:
							break;
						}
			}
		}).show();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		JSONObject weiboInfo = (JSONObject)array.opt(position);
		Intent intent = new Intent(ReferActivity.this, WeiboDetailActivity.class);
		try {
			intent.putExtra("weiboid", weiboInfo.getString("id"));
			startActivity(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
