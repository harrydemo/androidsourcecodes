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
	private List<String> matchStrList;//ѡ��atListƥ����ַ���
	private int flag;
	private static int FLAG_1 = 1;
	private static int FLAG_2 = 2;//1��2����atEnterBtn�ĸ��׿ؼ���ͬ
	
	private String to;//Ҫ�Ի�����
	private String from_flag;//��־��ת����д�㲥�����ǶԻ�������΢��
	private String reid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_weibo);
		
		setUpViews();
		setUpListeners();
		
		Intent intent = getIntent();
		String tip = intent.getStringExtra("tip");//���ñ���������ʾ
		add_top_tip.setText(tip);
		
		String content = intent.getStringExtra("content");
		weibo_content.setText(content);//��������
		
		to = intent.getStringExtra("to");
		from_flag = intent.getStringExtra("from_flag");
		reid = intent.getStringExtra("reid");
		
		dataHelper = DataBaseContext.getInstance(getApplicationContext());
		weibo = WeiboContext.getInstance();
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		user_default_name = preferences.getString("user_default_name", "");//ȡ��΢��Ĭ�ϵ�¼�˺���Ϣ
		
		handler = new AtHandler();
		thread = new AtThread();
		thread.start();//����һ���̻߳�ȡ����
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
		
		//���´�����������setUpViews�������Ǹ��˴��⵰����ϵ�����벼�֣���λ�ϴ���Ըĳ�xml���֣�����
		
		atRootLayout = new RelativeLayout(AddWeiboActivity.this);
		
		atEditText = new EditText(AddWeiboActivity.this);
		atEditText.setId(10000);
		
		atEnterBtn = new Button(AddWeiboActivity.this);
		atEnterBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enter_selector));
		
		atListView = new ListView(AddWeiboActivity.this);
		atListView.setCacheColorHint(Color.TRANSPARENT);//��ֹ����ʱ���ֺڿ죬���ſ���ע�͵��˾���һ��
		atListView.setDivider(getResources().getDrawable(R.drawable.list_divider));//���÷ָ���
		atListView.setBackgroundColor(Color.argb(255, 239, 239, 239));//alphaͨ��һ����Ҫ���ó�͸�����ˣ�Ҫ��ȻtextViewʲôҲ������,��Ϊ��������˺ܾã���Ϊ�������,���ŷ�����͸����
		
		topic_tip = new TextView(AddWeiboActivity.this);
		topic_tip.setText("�����뻰��");
		topic_tip.setTextSize(20);
		topic_tip.setTextColor(Color.argb(255, 90, 142, 189));//alphaͨ��һ����Ҫ���ó�͸�����ˣ�Ҫ��ȻtextViewʲôҲ������,��Ϊ��������˺ܾã���Ϊ�������,���ŷ�����͸����
		
		atRootLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		atEdiLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,80);
		atEnterBtnLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		atListViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		topicTipViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		
		
		//��Ӳ���Լ��
		atEdiLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		
		atEnterBtnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
		atEnterBtnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
		atEnterBtnLayoutParams.setMargins(0, 10, 10, 0);//���ñ߾࣬�ֱ�������ϣ��ң���
		
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
				operation_layout.removeAllViews();//������Ҫ�Ƴ���
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
			//֪ͨhandler��������
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
			operation_layout.removeAllViews();//������Ҫ�Ƴ���
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
						Toast.makeText(AddWeiboActivity.this, "���ͳɹ�", Toast.LENGTH_SHORT).show();//���գ��ǵ�Ҫshow,ÿ�ζ�������
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if("rebroad".equals(from_flag)){
				returnStr = weibo.reBroad(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), weibo_content.getText().toString(),reid);
				try {
					JSONObject dataObj = new JSONObject(returnStr);
					if("ok".equals(dataObj.getString("msg"))){
						Toast.makeText(AddWeiboActivity.this, "ת���ɹ�", Toast.LENGTH_SHORT).show();//���գ��ǵ�Ҫshow,ÿ�ζ�������
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if("private".equals(from_flag)){
				returnStr = weibo.addPrivate(weibo.getAccessTokenKey(), weibo.getAccessTokenSecrect(), weibo_content.getText().toString(), to);
				try {
					JSONObject dataObj = new JSONObject(returnStr);
					if("ok".equals(dataObj.getString("msg"))){
						Toast.makeText(AddWeiboActivity.this, "����˽�ųɹ�", Toast.LENGTH_SHORT).show();//���գ��ǵ�Ҫshow,ÿ�ζ�������
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if("comment".equals(from_flag)){
				returnStr = weibo.addComment(weibo.getAccessTokenKey(),weibo.getAccessTokenSecrect(),weibo_content.getText().toString(),reid);
				try {
					JSONObject dataObj = new JSONObject(returnStr);
					if("ok".equals(dataObj.getString("msg"))){
						Toast.makeText(AddWeiboActivity.this, "�ղسɹ�", Toast.LENGTH_SHORT).show();//���գ��ǵ�Ҫshow,ÿ�ζ�������
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
			// ��̬����װview
			atRootLayout.removeAllViews();// ��װǰ�Ȱ����еĺ����õ�
			atEditText.setText("@");
			flag = FLAG_1;//����atEnterBtn�����ĸ����水��
			atRootLayout.addView(atEditText, atEdiLayoutParams);
			atRootLayout.addView(atEnterBtn, atEnterBtnLayoutParams);
			atRootLayout.addView(atListView, atListViewLayoutParams);
			operation_layout.addView(atRootLayout);

			add_top_bar.setVisibility(View.GONE);// ���������bar���ı��༭�򣬲���֮��atѡ���໥Ӱ��
			weibo_content.setVisibility(View.GONE);
			operation_layout.setVisibility(View.VISIBLE);
		}
			break;
		case R.id.add_topic_btn:{
			//��̬����װview
			atRootLayout.removeAllViews();//��װǰ�Ȱ����еĺ����õ�
			atEditText.setText("");
			flag = FLAG_2;//����atEnterBtn�����ĸ����水��
			atRootLayout.addView(atEditText,atEdiLayoutParams);
			atRootLayout.addView(atEnterBtn,atEnterBtnLayoutParams);
			atRootLayout.addView(topic_tip,topicTipViewLayoutParams);
			operation_layout.addView(atRootLayout);
			
			add_top_bar.setVisibility(View.GONE);// ���������bar���ı��༭�򣬲���֮��atѡ���໥Ӱ��
			weibo_content.setVisibility(View.GONE);
			operation_layout.setVisibility(View.VISIBLE);
		}
			break;
		case R.id.add_expression_btn:{
			add_top_bar.setVisibility(View.GONE);//���������bar���ı��༭�򣬲���֮�����ѡ���gridView�໥Ӱ��
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
		DecimalFormat df = new DecimalFormat("000");//��ʽ������
		for(int i = 0;i<105;i++){
			Map<String,Object> map = new HashMap<String, Object>();
			String formatStr = "h"+df.format(i);
			int drawableId = 0 ;
			try {
				drawableId = R.drawable.class.getDeclaredField(formatStr).getInt(this);//����ȡ��id������ط�ѭ���׷��䣬�ǲ��Ǻܺ����ܰ�����û���Թ����鷳�кð취���ֵܽ��÷���һ��
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
			operation_layout.removeAllViews();//������Ҫ�Ƴ���
			
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
			operation_layout.removeAllViews();//������Ҫ�Ƴ���
			
			String str = matchStrList.get(position);
			String nickStr = str.substring(0,str.indexOf("("));
			weibo_content.setText(weibo_content.getText()+"@"+nickStr);
		}
	}
}
