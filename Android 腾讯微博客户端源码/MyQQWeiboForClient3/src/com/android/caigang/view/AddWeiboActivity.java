package com.android.caigang.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.caigang.R;
import com.android.caigang.db.DataHelper;
import com.android.caigang.model.UserInfo;
import com.android.caigang.util.DataBaseContext;
import com.android.caigang.util.TextUtil;
import com.android.caigang.util.WeiboContext;
import com.mime.qweibo.examples.MyWeiboSync;

public class AddWeiboActivity extends Activity implements OnClickListener{
	
	private DataHelper dataHelper;
	private UserInfo user;
	private String user_default_name;
	private MyWeiboSync weibo;
	private ListView listView;
	private EditText weibo_content;
	private Button send_btn;
	private Button add_cmamera_btn;
	private Button add_at_btn;
	private Button add_topic_btn;
	private Button add_expression_btn;
	private Button add_location_btn;
	private GridView expressionGrid;
	private List<Map<String,Object>> expressionList;
	private ExpressionAdapter expressionAdapter;
	private FrameLayout operation_layout;
	private RelativeLayout add_top_bar;
	
	private ListView atListView;
	private RelativeLayout atRootLayout;
	private EditText atEditText;
	private Button atEnterBtn;
	private TextView topic_tip;
	private TextView add_top_tip;
	
	private RelativeLayout.LayoutParams atEdiLayoutParams,atEnterBtnLayoutParams,atListViewLayoutParams,topicTipViewLayoutParams;
	
	private JSONArray array;
	private Handler handler;
	private ArrayAdapter atAdapter;
	private List<String> atList;
	private AtThread thread;
	private List<String> matchStrList;//选择atList匹配的字符串
	private int flag;
	private static int FLAG_1 = 1;
	private static int FLAG_2 = 2;//1和2代表atEnterBtn的父亲控件不同
	
	private String to;//要对话的人
	private String from_flag;//标志是转播，写广播，还是对话，点评微博
	private String reid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_weibo);
		
		setUpViews();
		setUpListeners();
		
		Intent intent = getIntent();
		String tip = intent.getStringExtra("tip");//设置标题栏的提示
		add_top_tip.setText(tip);
		
		String content = intent.getStringExtra("content");
		weibo_content.setText(content);//设置内容
		
		to = intent.getStringExtra("to");
		from_flag = intent.getStringExtra("from_flag");
		reid = intent.getStringExtra("reid");
		
		dataHelper = DataBaseContext.getInstance(getApplicationContext());
		weibo = WeiboContext.getInstance();
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		user_default_name = preferences.getString("user_default_name", "");//取得微博默认登录账号信息
		
		handler = new AtHandler();
		thread = new AtThread();
		thread.start();//开启一个线程获取数据
	}
	
	private void setUpViews(){
		weibo_content = (EditText)findViewById(R.id.weibo_content);
		send_btn = (Button)findViewById(R.id.send_btn);
		add_cmamera_btn = (Button)findViewById(R.id.add_cmamera_btn);
		add_at_btn = (Button)findViewById(R.id.add_at_btn);
		add_topic_btn = (Button)findViewById(R.id.add_topic_btn);
		add_expression_btn = (Button)findViewById(R.id.add_expression_btn);
		add_location_btn = (Button)findViewById(R.id.add_location_btn);
		
		add_top_bar = (RelativeLayout)findViewById(R.id.add_top_bar);
		operation_layout = (FrameLayout)findViewById(R.id.operation_layout);
		expressionGrid = new GridView(this);
		expressionGrid.setNumColumns(5);
		expressionList = buildExpressionsList();
		expressionAdapter = new ExpressionAdapter(AddWeiboActivity.this, expressionList);
		expressionGrid.setAdapter(expressionAdapter);
		
		add_top_tip = (TextView)findViewById(R.id.add_top_tip);
		
		//以下代码至本方法setUpViews结束，是个人纯粹蛋疼联系纯代码布局，各位老大可以改成xml布局，淡定
		
		atRootLayout = new RelativeLayout(AddWeiboActivity.this);
		
		atEditText = new EditText(AddWeiboActivity.this);
		atEditText.setId(10000);
		
		atEnterBtn = new Button(AddWeiboActivity.this);
		atEnterBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enter_selector));
		
		atListView = new ListView(AddWeiboActivity.this);
		atListView.setCacheColorHint(Color.TRANSPARENT);//防止滑屏时出现黑快，不信可以注释掉此句试一试
		atListView.setDivider(getResources().getDrawable(R.drawable.list_divider));//设置分割线
		atListView.setBackgroundColor(Color.argb(255, 239, 239, 239));//alpha通道一定不要设置成透明的了，要不然textView什么也看不见,因为这个我找了很久，以为代码错了,最后才发现是透明的
		
		topic_tip = new TextView(AddWeiboActivity.this);
		topic_tip.setText("请输入话题");
		topic_tip.setTextSize(20);
		topic_tip.setTextColor(Color.argb(255, 90, 142, 189));//alpha通道一定不要设置成透明的了，要不然textView什么也看不见,因为这个我找了很久，以为代码错了,最后才发现是透明的
		
		atRootLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		atEdiLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,80);
		atEnterBtnLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		atListViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		topicTipViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		
		
		//添加布局约束
		atEdiLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		
		atEnterBtnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
		atEnterBtnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
		atEnterBtnLayoutParams.setMargins(0, 10, 10, 0);//设置边距，分别代表左，上，右，下
		
		atListViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
		atListViewLayoutParams.addRule(RelativeLayout.BELOW, atEditText.getId());
		
		topicTipViewLayoutParams.addRule(RelativeLayout.BELOW, atEditText.getId());
		
	}
	
	private void setUpListeners(){
		send_btn.setOnClickListener(this);
		add_cmamera_btn.setOnClickListener(this);
		add_at_btn.setOnClickListener(this);
		add_topic_btn.setOnClickListener(this);
		add_expression_btn.setOnClickListener(this);
		add_location_btn.setOnClickListener(this);
		expressionGrid.setOnItemClickListener(new GridItemClickListener());
		atListView.setOnItemClickListener(new AtListViewItemListener());
		atEditText.addTextChangedListener(new MyTextWatcher());
		atEnterBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				add_top_bar.setVisibility(View.VISIBLE);
				weibo_content.setVisibility(View.VISIBLE);
				operation_layout.setVisibility(View.GONE);
				operation_layout.removeAllViews();//别忘记要移除掉
				if(flag==FLAG_1){
					weibo_content.setText(weibo_content.getText()+"@");
				}else if(flag==FLAG_2){
					weibo_content.setText(weibo_content.getText()+"#"+atEditText.getText()+"#");
				}
				
				
			}
		});
	}
	
	class AtThread extends Thread {
		@Override
		public void run() {
			String jsonStr = weibo.getFans(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), 20, 0, user_default_name);
			try {
				JSONObject dataObj = new JSONObject(jsonStr).getJSONObject("data");
				array = dataObj.getJSONArray("info");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//通知handler处理数据
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}
	
	class AtHandler extends Handler { 
		@Override
		public void handleMessage(Message msg){
			int size = array.length();
			atList = new ArrayList<String>();
			for(int i = 0;i<size;i++){
				JSONObject data = array.optJSONObject(i);
				try {
					atList.add(data.getString("nick")+"("+data.getString("name")+")");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			matchStrList = new ArrayList<String>();
			matchStrList.addAll(atList);
			atAdapter = new ArrayAdapter<String>(AddWeiboActivity.this,R.layout.at_list_item,R.id.at_nick_name,atList);
			atListView.setAdapter(atAdapter);
		}
	}
	
	class ExpressionAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<Map<String,Object>> list;
		
		public ExpressionAdapter(Context context, List<Map<String,Object>> list) {
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
		public View getView(final int position, View convertView, ViewGroup parent){
			Map<String,Object> map = list.get(position);
			ImageView image = new ImageView(context);
			image.setImageDrawable((Drawable)map.get("drawable"));
			return image;
		}
	}

	@Override
	public void onClick(View v) {
		if(operation_layout.getChildCount()>0){
			add_top_bar.setVisibility(View.VISIBLE);
			weibo_content.setVisibility(View.VISIBLE);
			operation_layout.setVisibility(View.GONE);
			operation_layout.removeAllViews();//别忘记要移除掉
			return;
		}
		switch (v.getId()) {
		
		case R.id.send_btn:{
			String returnStr = null;
			if("write".equals(from_flag)){
				returnStr = weibo.publishMsg(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), weibo_content.getText().toString());
				try {
					JSONObject dataObj = new JSONObject(returnStr);
					if("ok".equals(dataObj.getString("msg"))){
						Toast.makeText(AddWeiboActivity.this, "发送成功", Toast.LENGTH_SHORT).show();//我日，记得要show,每次都搞忘记
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if("rebroad".equals(from_flag)){
				returnStr = weibo.reBroad(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), weibo_content.getText().toString(),reid);
				try {
					JSONObject dataObj = new JSONObject(returnStr);
					if("ok".equals(dataObj.getString("msg"))){
						Toast.makeText(AddWeiboActivity.this, "转播成功", Toast.LENGTH_SHORT).show();//我日，记得要show,每次都搞忘记
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if("private".equals(from_flag)){
				returnStr = weibo.addPrivate(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), weibo_content.getText().toString(), to);
				try {
					JSONObject dataObj = new JSONObject(returnStr);
					if("ok".equals(dataObj.getString("msg"))){
						Toast.makeText(AddWeiboActivity.this, "发送私信成功", Toast.LENGTH_SHORT).show();//我日，记得要show,每次都搞忘记
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if("comment".equals(from_flag)){
				returnStr = weibo.addComment(weibo.getAccessTokenKey(),weibo.getAccessTokenSecrect(),weibo_content.getText().toString(),reid);
				try {
					JSONObject dataObj = new JSONObject(returnStr);
					if("ok".equals(dataObj.getString("msg"))){
						Toast.makeText(AddWeiboActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();//我日，记得要show,每次都搞忘记
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
			break;
		case R.id.add_cmamera_btn:{
			
		}
			break;
		case R.id.add_at_btn:{
			// 动态的组装view
			atRootLayout.removeAllViews();// 组装前先把所有的孩子拿掉
			atEditText.setText("@");
			flag = FLAG_1;//区分atEnterBtn是在哪个界面按的
			atRootLayout.addView(atEditText, atEdiLayoutParams);
			atRootLayout.addView(atEnterBtn, atEnterBtnLayoutParams);
			atRootLayout.addView(atListView, atListViewLayoutParams);
			operation_layout.addView(atRootLayout);

			add_top_bar.setVisibility(View.GONE);// 隐藏上面的bar和文本编辑框，不让之与at选择相互影响
			weibo_content.setVisibility(View.GONE);
			operation_layout.setVisibility(View.VISIBLE);
		}
			break;
		case R.id.add_topic_btn:{
			//动态的组装view
			atRootLayout.removeAllViews();//组装前先把所有的孩子拿掉
			atEditText.setText("");
			flag = FLAG_2;//区分atEnterBtn是在哪个界面按的
			atRootLayout.addView(atEditText,atEdiLayoutParams);
			atRootLayout.addView(atEnterBtn,atEnterBtnLayoutParams);
			atRootLayout.addView(topic_tip,topicTipViewLayoutParams);
			operation_layout.addView(atRootLayout);
			
			add_top_bar.setVisibility(View.GONE);// 隐藏上面的bar和文本编辑框，不让之与at选择相互影响
			weibo_content.setVisibility(View.GONE);
			operation_layout.setVisibility(View.VISIBLE);
		}
			break;
		case R.id.add_expression_btn:{
			add_top_bar.setVisibility(View.GONE);//隐藏上面的bar和文本编辑框，不让之与表情选择的gridView相互影响
			weibo_content.setVisibility(View.GONE);
			operation_layout.addView(expressionGrid);
			operation_layout.setVisibility(View.VISIBLE);
		}
			break;
		case R.id.add_location_btn:{
			
		}
			break;
		default:
			break;
		}
	}
	private List<Map<String,Object>> buildExpressionsList(){
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		DecimalFormat df = new DecimalFormat("000");//格式化数字
		for(int i = 0;i<105;i++){
			Map<String,Object> map = new HashMap<String, Object>();
			String formatStr = "h"+df.format(i);
			int drawableId = 0 ;
			try {
				drawableId = R.drawable.class.getDeclaredField(formatStr).getInt(this);//反射取得id，这个地方循环套反射，是不是很耗性能啊，我没测试过，麻烦有好办法的兄弟姐妹分享一下
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			Drawable drawable = getResources().getDrawable(drawableId);
			map.put("drawableId", formatStr);
			map.put("drawable",drawable);
			list.add(map);
		}
		return list;
	}
	
	class GridItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,long arg3) {
			Map<String, Object> map = expressionList.get(position);
			String drawableId = (String)map.get("drawableId");
			
			add_top_bar.setVisibility(View.VISIBLE);
			weibo_content.setVisibility(View.VISIBLE);
			operation_layout.setVisibility(View.GONE);
			operation_layout.removeAllViews();//别忘记要移除掉
			
			String expressionStr=null;
			expressionStr = TextUtil.drawableIdToFaceName.get(drawableId);
			expressionStr="/"+expressionStr;
			weibo_content.setText(weibo_content.getText().toString()+expressionStr);
		}
	}
	
	class MyTextWatcher implements TextWatcher{
		@Override
		public void afterTextChanged(Editable s){
			String changingStr = atEditText.getText().toString();
			if(changingStr.indexOf("@")!=-1){
				changingStr = changingStr.substring(1);
			}
			
			int size = atList.size();
			matchStrList.clear();
			for(int i = 0;i<size;i++){
				String currentStr = atList.get(i);
				if(currentStr.indexOf(changingStr)!=-1){
					matchStrList.add(currentStr);
				}
			}
			atAdapter = new ArrayAdapter<String>(AddWeiboActivity.this,R.layout.at_list_item,R.id.at_nick_name,matchStrList);
			atAdapter.notifyDataSetChanged();
			atListView.setAdapter(atAdapter);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			
		}
	}
	
	class AtListViewItemListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3){
			add_top_bar.setVisibility(View.VISIBLE);
			weibo_content.setVisibility(View.VISIBLE);
			operation_layout.setVisibility(View.GONE);
			operation_layout.removeAllViews();//别忘记要移除掉
			
			String str = matchStrList.get(position);
			String nickStr = str.substring(0,str.indexOf("("));
			weibo_content.setText(weibo_content.getText()+"@"+nickStr);
		}
	}
}
