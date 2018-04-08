package com.jclt.activity;

import com.jclt.activity.more.LetaoAboutActivity;
import com.jclt.activity.myletao.LetaoCollectActivity;
import com.jclt.activity.shopping.ShoppingCarActivity;
import com.jclt.activity.type.TypeLetaoActivity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 继承此Activity类可以使用此类的方法 
 * 该类是提供整个项目中Activity所使用到的公共方法 
 * 该类中全属于公共方法.集成该类可以使用
 * 
 * @author TanRuixiang
 * @Time 2011年7月25日, PM 10:51:42
 */
public class CommonActivity extends ListActivity implements Runnable {
	/** ==============BEGAN菜单导航栏管理(ItemId)================= **/
	private static final int INDEX = 1;
	private static final int SEARCH = 2;
	private static final int MORE = 3;
	private static final int RECORD = 4;
	private static final int ADVICE = 5;
	private static final int EXIT = 6;
	public static boolean exit;
	public Intent intent = new Intent();
	/** ==============END菜单导航栏管理(ItemId)================= **/
	
	
	/**==============BEGAN进度条管理(ProgressDialog)=============**/
	public ProgressDialog progressDialog = null;
	public Handler handler = new Handler();
	private int i = 0;
	/**==============END进度条管理(ProgressDialog)=============**/
	
	
	/**==============BEGAN底部菜单栏管理(ImageView)============**/
	public ImageView imageViewIndex = null ;
	public ImageView imageViewType = null ;
	public ImageView imageViewShooping = null ;
	public ImageView imageViewMyLetao = null ; 
	public ImageView imageViewMore = null ;
	
	/**
	 * 底部菜单
	 * 首页点击事件
	 */
	public ImageViewIndex  viewIndex = new ImageViewIndex();
	/**
	 * 底部菜单
	 * 分类点击事件
	 */
	public ImageViewType viewType = new ImageViewType();
	/**
	 * 底部菜单
	 * 购物车点击事件
	 */
	public ImageViewShooping viewShooping = new ImageViewShooping();
	/**
	 * 底部菜单
	 * 历通点击事件
	 */
	public ImageViewMyLetao viewMyLetao = new ImageViewMyLetao();
	/**
	 * 底部菜单
	 * 更多点击事件
	 */
	public ImageViewMore viewMore = new ImageViewMore();
	/**==============END底部菜单栏管理(ImageView)============**/
	
	public ListView listViewAll = null ;
	public TextView textViewTitle = null ;
	
	/**
	 * 菜单栏设置
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, INDEX, 1, R.string.index);
		menu.add(0, SEARCH, 1, R.string.search);
		menu.add(0, MORE, 1, R.string.more);
		menu.add(0, RECORD, 1, R.string.record);
		menu.add(0, ADVICE, 1, R.string.advice);
		menu.add(0, EXIT, 1, R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 底部菜单控件事件
	 */
	public void ButtomOnclickListenerAll(){
		this.imageViewIndex.setOnClickListener(new IndexOnclickListener());
	}
	/**
	 *底部菜单首页控件事件
	 */
    class IndexOnclickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			intent.setClass(CommonActivity.this, SecondActivity.class);
			startActivity(intent);
		}
    	
    }
	/**
	 * 点击菜单栏中的按钮时触发该事件(onOptionsItemSelected方法)
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == INDEX) {
			intent.setClass(this, SecondActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == SEARCH) {
			intent.setClass(this, TypeLetaoActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == MORE) {
			intent.setClass(this, MoreInforActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == RECORD) {
			intent.setClass(this, LetaoCollectActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == ADVICE) {
			intent.setClass(this, LetaoAboutActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == EXIT) {
			openQiutDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 退出
	 */
	private void openQiutDialog() {
		new AlertDialog.Builder(this).setTitle("历通购物").setMessage("是否退出历通购物？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
//						Intent intent = new Intent(Intent.ACTION_MAIN);
//						intent.setClass(getApplicationContext(), FirstActivity.class);
//						intent.addCategory(Intent.CATEGORY_HOME);    
//						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
//						startActivity(intent); 
//						System.exit(0);
						/*if (CommonActivity.this instanceof SecondActivity) {
							finish();
						} else {
							Intent intent = new Intent();
							boolean exit = true;
							intent.setClass(CommonActivity.this,
									SecondActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
							System.exit(0);
						}*/
						if (CommonActivity.this instanceof SecondActivity) {
							finish();
						} else {
							Intent intent = new Intent();
							 exit = true;
							intent.setClass(CommonActivity.this,
									SecondActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
						}
						}
				}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();
	}
	// 底部菜单栏点击事件效果设置
	class ImageViewIndex implements OnTouchListener {
         //首页
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (event.getAction() == event.ACTION_DOWN) {
				imageViewIndex.setImageResource(R.drawable.menu_home_pressed);
				intent.setClass(CommonActivity.this, SecondActivity.class);
				startActivity(intent);
			}
			return false;
		}

	}

	class ImageViewType implements OnTouchListener {
       //类型(分类)
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == event.ACTION_DOWN) {
				startActivity(new Intent(getApplicationContext(),TypeLetaoActivity.class));
				imageViewType.setImageResource(R.drawable.menu_brand_pressed);
			}
			return false;
		}

	}
    
	class ImageViewShooping implements OnTouchListener {
        //购物车
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == event.ACTION_DOWN) {
				startActivity(new Intent(getApplicationContext(),ShoppingCarActivity.class));
				imageViewShooping.setImageResource(R.drawable.menu_shopping_cart_pressed);
			}
			return false;
		}

	}

	class ImageViewMyLetao implements OnTouchListener {
        //我的乐淘
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == event.ACTION_DOWN) {
				intent.setClass(getApplicationContext(), MyLetaoInforActivity.class);
				startActivity(intent);
				imageViewMyLetao.setImageResource(R.drawable.menu_my_letao_pressed);
			}
			return false;
		}
	}

	class ImageViewMore implements OnTouchListener {
        //更多信息
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == event.ACTION_DOWN) {
				imageViewMore.setImageResource(R.drawable.menu_more_pressed);
				intent.setClass(CommonActivity.this, MoreInforActivity.class);
				startActivity(intent);
			}
			return false;
		}

	}
	/**
	 * 调用该CommonActivity类的线程
	 */
	public void run() {

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (i > 500) {
			progressDialog.dismiss();
			handler.removeCallbacks(this);
		} else {
			i = i + 10;
			handler.post(this);
		}

	}
//	private void forecStopPackage(String pkgname){
//		   ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//		checkForceStop();
//		}
//	
//	private void checkForceStop(){
//		  Intent intent = new Intent(Intent.ACTION_QUERY_PACKAGE_RESTART,Uri.fromParts("package",mAppEntry.info.packageName,null));
//		intent.putExtra(Intent.EXTRA_PACKAGES, new String[]{mAppEntry.info.packageName});
//		}
}
