package com.waps.demo170;

import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waps.AdInfo;
import com.waps.AdView;
import com.waps.AppConnect;
import com.waps.MiniAdView;
import com.waps.UpdatePointsNotifier;

public class DemoApp extends Activity implements View.OnClickListener, UpdatePointsNotifier {

	private TextView pointsTextView;
	private TextView SDKVersionView;

	String displayPointsText;
	String currencyName = "金币";

	final Handler mHandler = new Handler();

	// 广告条layout.
	LinearLayout adLinearLayout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// 初始化统计器，并通过代码设置WAPS_ID, WAPS_PID
		AppConnect.getInstance("a78d77dc75f689a134427c4e8fbe00a0", "WAPS", this);
		
		// 初始化统计器，需要在AndroidManifest中注册WAPS_ID和WAPS_PID值
		// AppConnect.getInstance(this);
		// 以上两种统计器初始化方式任选其一，不要同时使用

		// 使用自定义的OffersWebView
		AppConnect.getInstance(this).setAdViewClassName("com.waps.demo170.MyAdView");

		// 禁用错误报告
		AppConnect.getInstance(this).setCrashReport(false);

		adLinearLayout = (LinearLayout) findViewById(R.id.AdLinearLayout);
		
		Button offers = (Button) findViewById(R.id.OffersButton);
		Button gameOffers = (Button) findViewById(R.id.gameOffersButton);
		Button appOffers = (Button) findViewById(R.id.appOffersButton);
		Button owns = (Button) findViewById(R.id.OwnsButton);
		Button spendPoints = (Button) findViewById(R.id.SpendPointsButton);
		Button feedbackBurron = (Button) findViewById(R.id.Feedback);
		Button awardBurron = (Button) findViewById(R.id.awardButton);
		Button diyAdButton = (Button) findViewById(R.id.diyAdButton);
		Button diyAdListButton = (Button) findViewById(R.id.diyAdList);
		Button checkUpdateButton = (Button) findViewById(R.id.checkUpdateButton);

		offers.setOnClickListener(this);
		gameOffers.setOnClickListener(this);
		appOffers.setOnClickListener(this);
		owns.setOnClickListener(this);
		spendPoints.setOnClickListener(this);
		feedbackBurron.setOnClickListener(this);
		awardBurron.setOnClickListener(this);
		diyAdButton.setOnClickListener(this);
		diyAdListButton.setOnClickListener(this);
		checkUpdateButton.setOnClickListener(this);

		pointsTextView = (TextView) findViewById(R.id.PointsTextView);
		SDKVersionView = (TextView) findViewById(R.id.SDKVersionView);
		
		// 初始化自定义广告数据
		AppConnect.getInstance(this).initAdInfo();
		
		// 带有默认参数值的在线配置，使用此方法，程序第一次启动使用的是"defaultValue"，之后再启动则是使用的服务器端返回的参数值
		String showAd = AppConnect.getInstance(this).getConfig("showAd", "defaultValue");
		
		SDKVersionView.setText("在线参数:showAd = "+showAd);
		
		SDKVersionView.setText(SDKVersionView.getText()+"\nSDK版本: " + AppConnect.LIBRARY_VERSION_NUMBER);
		
		// 互动广告调用方式
		LinearLayout container = (LinearLayout) findViewById(R.id.AdLinearLayout);
		new AdView(this, container).DisplayAd();
		
		// 迷你广告调用方式
		// AppConnect.getInstance(this).setAdBackColor(Color.argb(50, 120, 240, 120));//设置迷你广告背景颜色
		// AppConnect.getInstance(this).setAdForeColor(Color.YELLOW);//设置迷你广告文字颜色
		LinearLayout miniLayout = (LinearLayout) findViewById(R.id.miniAdLinearLayout);
		new MiniAdView(this, miniLayout).DisplayAd(10);// 10秒刷新一次
	}

	public void onClick(View v) {
		if (v instanceof Button) {
			int id = ((Button) v).getId();

			switch (id) {
			case R.id.OffersButton:
				//显示推荐列表（综合）
				AppConnect.getInstance(this).showOffers(this);
				break;
			case R.id.appOffersButton:
				//显示推荐列表（软件）
				AppConnect.getInstance(this).showAppOffers(this);
				break;
			case R.id.gameOffersButton:
				//显示推荐列表（游戏）
				AppConnect.getInstance(this).showGameOffers(this);
				break;
			case R.id.OwnsButton:
				//显示自家应用列表
				AppConnect.getInstance(this).showMore(this);
				break;
			case R.id.diyAdList:
				//获取全部自定义广告数据
				showList();
				break;
			case R.id.diyAdButton:
				//获取一条自定义广告数据
				AdInfo adInfo = AppConnect.getInstance(DemoApp.this).getAdInfo();
				showAdDetail(adInfo);
				break;
			case R.id.SpendPointsButton:
				//消费虚拟货币.
				AppConnect.getInstance(this).spendPoints(10, this);
				break;
			case R.id.awardButton:
				//奖励虚拟货币
				AppConnect.getInstance(this).awardPoints(10, this);
				break;
			case R.id.Feedback:
				//用户反馈
				AppConnect.getInstance(this).showFeedback();
				break;
			case R.id.checkUpdateButton:
				//手动检查新版本
				AppConnect.getInstance(this).checkUpdate();
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		// 从服务器端获取当前用户的虚拟货币.
		// 返回结果在回调函数getUpdatePoints(...)中处理
		AppConnect.getInstance(this).getPoints(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		AppConnect.getInstance(this).finalize();
		super.onDestroy();
	}

	// 创建一个线程
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			if (pointsTextView != null) {
				pointsTextView.setText(displayPointsText);
			}
		}
	};

	/**
	 * AppConnect.getPoints()方法的实现，必须实现
	 * 
	 * @param currencyName
	 *            虚拟货币名称.
	 * @param pointTotal
	 *            虚拟货币余额.
	 */
	public void getUpdatePoints(String currencyName, int pointTotal) {
		this.currencyName = currencyName;
		displayPointsText = currencyName + ": " + pointTotal;
		mHandler.post(mUpdateResults);
	}

	/**
	 * AppConnect.getPoints() 方法的实现，必须实现
	 * 
	 * @param error
	 *            请求失败的错误信息
	 */
	public void getUpdatePointsFailed(String error) {
		displayPointsText = error;
		mHandler.post(mUpdateResults);
	}

	/**
	 * 获取广告全部数据
	 */
	private void showList(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getListView();
		if(view != null){
			builder.setView(getListView());
		}else{
			builder.setMessage("未获取到自定义广告数据");
		}
    	builder.create().show();
    }
    /**
     * 获取适配后的ListView
     * @return ListView实例
     */
    private View getListView(){
    	ListView listView = new ListView(this);
    	@SuppressWarnings("unchecked")
		final List<AdInfo> list = AppConnect.getInstance(this).getAdInfoList();
    	if(list != null && list.size() > 0){
	    	listView.setAdapter(new MyAdapter(this, list));
	    	listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					AppConnect.getInstance(DemoApp.this).clickAd(list.get(position).getAdId());
				}
			});
	    	return listView;
    	}else{
    		return null;
    	}
    	
    }
    
    /**
     * 用于自定义广告列表的Adapter
     */
    private class MyAdapter extends BaseAdapter{
    	Context context;
    	List<AdInfo> list;
    	public MyAdapter(Context context, List<AdInfo> list){
    		this.context = context;
    		this.list = list;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			RelativeLayout r_layout;
        	ImageView app_icon;
        	TextView app_name;
        	RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	
    		AdInfo adInfo = list.get(position);
    		r_layout = new RelativeLayout(context);
    		app_icon = new ImageView(context);
    		app_icon.setId(1);
    		app_name = new TextView(context);
    		
    		app_icon.setLayoutParams(new LayoutParams(75,75));
    		app_icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
    		
    		rlp.addRule(RelativeLayout.RIGHT_OF, app_icon.getId());
    		rlp.addRule(RelativeLayout.CENTER_VERTICAL);
            app_icon.setImageDrawable(new BitmapDrawable(adInfo.getAdIcon()));  
            app_icon.setPadding(5, 5, 5, 5);
            
            app_name.setText(adInfo.getAdName());
            app_name.setTextSize(18);
            app_name.setTextColor(Color.WHITE);
            app_name.setPadding(10, 0, 0, 0);
            
            TextView content = new TextView(context);
            content.setText(adInfo.getAdText());
            content.setPadding(10, 0, 0, 0);
            
            LinearLayout layout  = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            
            layout.addView(app_name);
            layout.addView(content);
            
            r_layout.addView(app_icon);
            r_layout.addView(layout, rlp);
            
        	convertView = r_layout;
        	convertView.setTag(r_layout);
            return r_layout;
		}
    }
	
    /**
     * 显示自定义的广告详情
     * @param adInfo 
     */
	public void showAdDetail(final AdInfo adInfo){
    	try {
    		AlertDialog.Builder builder  = new AlertDialog.Builder(this);
    		if(adInfo != null){
    		
				View view = View.inflate(this, R.layout.detail, null);
				ImageView icon = (ImageView) view.findViewById(R.id.detail_icon);
				TextView title = (TextView) view.findViewById(R.id.detail_title);
				TextView version = (TextView) view.findViewById(R.id.detail_version);
				TextView filesize = (TextView) view.findViewById(R.id.detail_filesize);
				TextView points = (TextView) view.findViewById(R.id.detail_points);
				Button downButton1 = (Button) view.findViewById(R.id.detail_downButton1);
				TextView content = (TextView) view.findViewById(R.id.detail_content);
				TextView description = (TextView) view.findViewById(R.id.detail_description);
				ImageView image1 = (ImageView) view.findViewById(R.id.detail_image1);
				ImageView image2 = (ImageView) view.findViewById(R.id.detail_image2);
				
				icon.setImageBitmap(adInfo.getAdIcon());
				icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
				title.setText(adInfo.getAdName());
				version.setText("  "+adInfo.getVersion());
				filesize.setText("  "+adInfo.getFilesize()+"M");
				points.setText(adInfo.getAction() + "送"+String.valueOf(adInfo.getAdPoints())+currencyName);
				content.setText(adInfo.getAdText());
				description.setText(adInfo.getDescription());
				
				new GetImagesTask(adInfo, image1, image2).execute();
			
				downButton1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AppConnect.getInstance(DemoApp.this).downloadAd(adInfo.getAdId());
					}
				});
			
				builder.setView(view);
			}else{
				builder.setMessage("未获取到自定义广告数据");
			}
    		
			builder.create().show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 异步获取广告截图
	 */
	private class GetImagesTask extends AsyncTask<Void, Void, Boolean> {
		Bitmap bitmap1;
		Bitmap bitmap2;
		AdInfo adInfo;
		ImageView image1;
		ImageView image2;
		public GetImagesTask(AdInfo adInfo, ImageView image1, ImageView image2){
			this.adInfo = adInfo;
			this.image1 = image1;
			this.image2 = image2;
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			boolean returnValue = false;
			
			try {
				bitmap1 = BitmapFactory.decodeStream(
					new DefaultHttpClient().execute(new HttpGet(adInfo.getImageUrls()[0].replaceAll(" ", "%20"))).getEntity().getContent());
				bitmap2 = BitmapFactory.decodeStream(
					new DefaultHttpClient().execute(new HttpGet(adInfo.getImageUrls()[1].replaceAll(" ", "%20"))).getEntity().getContent());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return returnValue;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			try {
				if (bitmap1 != null && bitmap2 != null) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							try {
								image1.setImageBitmap(bitmap1);
								image2.setImageBitmap(bitmap2);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}