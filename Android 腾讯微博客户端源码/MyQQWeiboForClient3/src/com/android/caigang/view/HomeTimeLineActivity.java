package com.android.caigang.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

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
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.caigang.R;
import com.android.caigang.db.DataHelper;
import com.android.caigang.model.UserInfo;
import com.android.caigang.util.AsyncImageLoader;
import com.android.caigang.util.AsyncImageLoader.ImageCallback;
import com.android.caigang.util.DataBaseContext;
import com.android.caigang.util.RegexUtil;
import com.android.caigang.util.TextUtil;
import com.android.caigang.util.TimeUtil;
import com.android.caigang.util.WeiboContext;
import com.mime.qweibo.examples.MyWeiboSync;
import com.mime.qweibo.examples.QWeiboType.PageFlag;

public class HomeTimeLineActivity extends ListActivity implements OnItemClickListener,OnItemLongClickListener{
	
	private DataHelper dataHelper;
	private UserInfo user;
	private MyWeiboSync weibo;
	private ListView listView;
	private HomeAdapter adapter;
	private JSONArray array;
	private AsyncImageLoader asyncImageLoader;
	private Handler handler;
	private ProgressDialog progressDialog;
	private View top_panel;
	private Button top_btn_left;
	private Button top_btn_right;
	private TextView top_title;
	private LinearLayout list_footer;
	private TextView tv_msg;
	private LinearLayout loading;
	private List<JSONObject> list;//΢�������б�
	private ExecutorService executorService;
	private static int PAGE_SIZE = 20;//ÿҳ��ʾ��΢������
	private int TOTAL_PAGE = 0;//��ǰ�Ѿ����ڵ�΢��ҳ��
	private static int THREADPOOL_SIZE = 4;//�̳߳صĴ�С
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		setUpViews();
		setUpListeners();
		
		dataHelper = DataBaseContext.getInstance(getApplicationContext());
		weibo = WeiboContext.getInstance();
		
		List<UserInfo> userList = dataHelper.GetUserList(false);
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		String nick = preferences.getString("user_default_nick", "");//ȡ��Ĭ���˺���Ϣ
		
		if (nick != "") {
			user = dataHelper.getUserByName(nick,userList);
			top_title.setText(nick);//�����������ǳ�
		}
		
		weibo.setAccessTokenKey(user.getToken());
		weibo.setAccessTokenSecrect(user.getTokenSecret());
		
		progressDialog = new ProgressDialog(HomeTimeLineActivity.this);// ����һ��������
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("���Ե�");
		progressDialog.setMessage("���ڶ�ȡ������!");
		handler = new GetHomeTimeLineHandler();
		
		executorService.submit(new GetHomeTimeLineThread());//��ʱ����,����һ�����̻߳�ȡ����
		progressDialog.show();
	}
	
	private void setUpViews(){
		list_footer = (LinearLayout)LayoutInflater.from(HomeTimeLineActivity.this).inflate(R.layout.list_footer, null);
		tv_msg = (TextView)list_footer.findViewById(R.id.tv_msg);
		loading = (LinearLayout)list_footer.findViewById(R.id.loading);
		listView = getListView();
		top_panel = (View)findViewById(R.id.home_top);
		top_btn_left = (Button)top_panel.findViewById(R.id.top_btn_left);
		top_btn_right = (Button)top_panel.findViewById(R.id.top_btn_right);
		top_title = (TextView)top_panel.findViewById(R.id.top_title);
		listView.addFooterView(list_footer);//����ǹؼ��еĹؼ�ѽ������FooterVIew��ҳ��̬����
		list = new ArrayList<JSONObject>();
		executorService = Executors.newFixedThreadPool(THREADPOOL_SIZE);
	}
	
	private void setUpListeners(){
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		top_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeTimeLineActivity.this,AddWeiboActivity.class);
				intent.putExtra("tip", "д�㲥");
				intent.putExtra("content", "");
				intent.putExtra("from_flag", "write");
				startActivity(intent);
			}
		});
		tv_msg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(HomeTimeLineActivity.this, "�ҽ���ʧ��", Toast.LENGTH_SHORT).show();
				executorService.submit(new GetHomeTimeLineThread());
				tv_msg.setVisibility(View.GONE);//���ظ�����ʾ��TextView
				loading.setVisibility(View.VISIBLE);//��ʾ���·��Ľ�����
			}
		});
	}
	
	class GetHomeTimeLineHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			adapter = new HomeAdapter(HomeTimeLineActivity.this, list);
			if(TOTAL_PAGE>1){
				adapter.notifyDataSetChanged();
			}
			if(TOTAL_PAGE==1){
				listView.setAdapter(adapter);
			}
			listView.setSelection((TOTAL_PAGE-1)*PAGE_SIZE+1);//�������»�ȡһҳ���ݳɹ�����ʾ���ݵ���ʼ����
			progressDialog.dismiss();// �رս�����
			loading.setVisibility(View.GONE);//�����·��Ľ�����
			tv_msg.setVisibility(View.VISIBLE);//��ʾ��������ʾTextView
		}
	}
	
	class GetHomeTimeLineThread implements Runnable{
		@Override
		public void run() {
			refreshList();
			Message msg = handler.obtainMessage();//֪ͨ�߳�������Χ������
			handler.sendMessage(msg);			
		}
	}
	
	private void refreshList(){
		String jsonStr = weibo.getHomeMsg(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), PageFlag.PageFlag_First, (TOTAL_PAGE+1)*PAGE_SIZE);
		try {
			JSONObject dataObj = new JSONObject(jsonStr).getJSONObject("data");
			array = dataObj.getJSONArray("info");
			if(array!=null&&array.length()>0){
				TOTAL_PAGE++;
				list.clear();
				int lenth =array.length();
				for(int i = 0;i<lenth;i++){
					list.add(array.optJSONObject(i));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	class HomeAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<JSONObject> list;
		
		public HomeAdapter(Context context, List<JSONObject> list) {
			super();
			this.context = context;
			this.list = list;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			asyncImageLoader = new AsyncImageLoader();
			HomeViewHolder viewHolder = new HomeViewHolder(); 
			JSONObject data = (JSONObject)list.get(position);
			JSONObject source = null;
			convertView = inflater.inflate(R.layout.home_list_item, null);
			
			viewHolder.home_headicon = (ImageView) convertView.findViewById(R.id.home_headicon);
			viewHolder.home_nick = (TextView) convertView.findViewById(R.id.home_nick);
			viewHolder.home_vip = (ImageView) convertView.findViewById(R.id.home_vip);
			viewHolder.home_hasimage = (ImageView) convertView.findViewById(R.id.home_hasimage);
			viewHolder.home_timestamp = (TextView) convertView.findViewById(R.id.home_timestamp);
			viewHolder.home_origtext = (TextView) convertView.findViewById(R.id.home_origtext);
			viewHolder.home_source = (TextView) convertView.findViewById(R.id.home_source);
			
			if(data!=null){
				try {
					convertView.setTag(data.get("id"));
					//viewHolder.home_headicon.setImageDrawable(ImageUtil.getDrawableFromUrl(data.getString("head")+"/100"));//ͬ������ͼƬ
					viewHolder.home_nick.setText(data.getString("nick"));
					if(data.getInt("isvip")!=1){
						viewHolder.home_vip.setVisibility(View.GONE);//��vip����vip��־
					}
					viewHolder.home_timestamp.setText(TimeUtil.converTime(Long.parseLong(data.getString("timestamp"))));
					
					//�첽����ͼƬ
					Drawable cachedImage = asyncImageLoader.loadDrawable(data.getString("head")+"/100",viewHolder.home_headicon, new ImageCallback(){
	                    @Override
	                    public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
	                        imageView.setImageDrawable(imageDrawable);
	                    }
	                });
					if (cachedImage == null) {
						viewHolder.home_headicon.setImageResource(R.drawable.icon);
					} else {
						viewHolder.home_headicon.setImageDrawable(cachedImage);
					}
					
					if(!"null".equals(data.getString("image"))){
						viewHolder.home_hasimage.setImageResource(R.drawable.hasimage);
					}
					
					//΢�����ݿ�ʼ
					String origtext = data.getString("origtext");
					
					SpannableString spannable = new SpannableString(origtext);
					
					
					/*spannable = TextUtil.decorateFaceInStr(spannable, RegexUtil.getStartAndEndIndex(data.getString("origtext"), Pattern.compile("\\/[\u4e00-\u9fa5a-zA-Z]{1,3}")), getResources());//�����ɱ��ر���
					spannable = TextUtil.decorateRefersInStr(spannable, RegexUtil.getStartAndEndIndex(origtext, Pattern.compile("@.*:")), getResources());//������ʾ΢��ת�����ظ����ǳ�
					spannable = TextUtil.decorateTopicInStr(spannable, RegexUtil.getStartAndEndIndex(origtext, Pattern.compile("#.*#")), getResources());//������ʾ��������
					spannable = TextUtil.decorateTopicInStr(spannable, RegexUtil.getStartAndEndIndex(origtext, Pattern.compile("^http://\\w+(\\.\\w+|)+(/\\w+)*(/\\w+\\.(\\w+|))?")), getResources());//������ʾurl��ַ
					*/
					viewHolder.home_origtext.setText(spannable);
					//΢���������ý���
					
					
					//�������õ�ת�������۵�΢������
					try {
						if(!"null".equals(data.getString("source"))){
							source = data.getJSONObject("source");
						}
					} catch (JSONException e1) {
						e1.printStackTrace(); 
					}
					if(source!=null){
						String home_source_text = null;
						boolean isvip = source.getInt("isvip")==1?true:false;
						if(isvip){
							home_source_text = "@"+source.getString("nick")+"======:"+source.getString("origtext");//��6��������=�����滻vip��־ͼ��
						}else{
							home_source_text = "@"+source.getString("nick")+":"+source.getString("origtext");
						}
						SpannableString spannableSource = new SpannableString(home_source_text);
						spannableSource = TextUtil.decorateRefersInStr(spannableSource, RegexUtil.getStartAndEndIndex(home_source_text, Pattern.compile("@.*:")), getResources());
						spannableSource = TextUtil.decorateTopicInStr(spannableSource, RegexUtil.getStartAndEndIndex(home_source_text, Pattern.compile("#.*#")), getResources());
						spannableSource = TextUtil.decorateTopicInStr(spannableSource, RegexUtil.getStartAndEndIndex(home_source_text, Pattern.compile("^http://\\w+(\\.\\w+|)+(/\\w+)*(/\\w+\\.(\\w+|))?")), getResources());
						
						if(isvip){
							spannableSource = TextUtil.decorateVipInStr(spannableSource, RegexUtil.getStartAndEndIndex(home_source_text, Pattern.compile("======")), getResources());//�滻Ϊvip��֤ͼƬ
						}
						viewHolder.home_source.setText(spannableSource);
						viewHolder.home_source.setBackgroundResource(R.drawable.home_source_bg);
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
	
	static class HomeViewHolder {
		private ImageView home_headicon;
		private TextView home_nick;
		private ImageView home_vip;
		private TextView home_timestamp;
		private TextView home_origtext;
		private TextView home_source;
		private ImageView home_hasimage;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
		CharSequence [] items = null;
		try {
			items= new CharSequence[]{"ת��","�Ի�","����","�ղ�",((JSONObject)array.opt(position)).getString("nick"),"ȡ��"};
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AlertDialog.Builder(HomeTimeLineActivity.this).setTitle("ѡ��").setItems(items,new DialogInterface.OnClickListener() {
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
		Intent intent = new Intent(HomeTimeLineActivity.this, WeiboDetailActivity.class);
		try {
			intent.putExtra("weiboid", weiboInfo.getString("id"));
			startActivity(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.home_timeline_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings: {

		}
			break;
		case R.id.menu_official: {

		}
			break;
		case R.id.menu_feedback: {

		}
			break;
		case R.id.menu_account: {
			Intent intent = new Intent(HomeTimeLineActivity.this,
					AccountActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.menu_about: {
			Intent intent = new Intent(HomeTimeLineActivity.this,
					AddWeiboActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.menu_quit: {

		}
			break;
		default:
			break;
		}
		return true;
	}
}
