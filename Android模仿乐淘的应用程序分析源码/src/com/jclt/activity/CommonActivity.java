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
 * �̳д�Activity�����ʹ�ô���ķ��� 
 * �������ṩ������Ŀ��Activity��ʹ�õ��Ĺ������� 
 * ������ȫ���ڹ�������.���ɸ������ʹ��
 * 
 * @author TanRuixiang
 * @Time 2011��7��25��, PM 10:51:42
 */
public class CommonActivity extends ListActivity implements Runnable {
	/** ==============BEGAN�˵�����������(ItemId)================= **/
	private static final int INDEX = 1;
	private static final int SEARCH = 2;
	private static final int MORE = 3;
	private static final int RECORD = 4;
	private static final int ADVICE = 5;
	private static final int EXIT = 6;
	public static boolean exit;
	public Intent intent = new Intent();
	/** ==============END�˵�����������(ItemId)================= **/
	
	
	/**==============BEGAN����������(ProgressDialog)=============**/
	public ProgressDialog progressDialog = null;
	public Handler handler = new Handler();
	private int i = 0;
	/**==============END����������(ProgressDialog)=============**/
	
	
	/**==============BEGAN�ײ��˵�������(ImageView)============**/
	public ImageView imageViewIndex = null ;
	public ImageView imageViewType = null ;
	public ImageView imageViewShooping = null ;
	public ImageView imageViewMyLetao = null ; 
	public ImageView imageViewMore = null ;
	
	/**
	 * �ײ��˵�
	 * ��ҳ����¼�
	 */
	public ImageViewIndex  viewIndex = new ImageViewIndex();
	/**
	 * �ײ��˵�
	 * �������¼�
	 */
	public ImageViewType viewType = new ImageViewType();
	/**
	 * �ײ��˵�
	 * ���ﳵ����¼�
	 */
	public ImageViewShooping viewShooping = new ImageViewShooping();
	/**
	 * �ײ��˵�
	 * ��ͨ����¼�
	 */
	public ImageViewMyLetao viewMyLetao = new ImageViewMyLetao();
	/**
	 * �ײ��˵�
	 * �������¼�
	 */
	public ImageViewMore viewMore = new ImageViewMore();
	/**==============END�ײ��˵�������(ImageView)============**/
	
	public ListView listViewAll = null ;
	public TextView textViewTitle = null ;
	
	/**
	 * �˵�������
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
	 * �ײ��˵��ؼ��¼�
	 */
	public void ButtomOnclickListenerAll(){
		this.imageViewIndex.setOnClickListener(new IndexOnclickListener());
	}
	/**
	 *�ײ��˵���ҳ�ؼ��¼�
	 */
    class IndexOnclickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			intent.setClass(CommonActivity.this, SecondActivity.class);
			startActivity(intent);
		}
    	
    }
	/**
	 * ����˵����еİ�ťʱ�������¼�(onOptionsItemSelected����)
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
	 * �˳�
	 */
	private void openQiutDialog() {
		new AlertDialog.Builder(this).setTitle("��ͨ����").setMessage("�Ƿ��˳���ͨ���")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

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
				}).setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();
	}
	// �ײ��˵�������¼�Ч������
	class ImageViewIndex implements OnTouchListener {
         //��ҳ
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
       //����(����)
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
        //���ﳵ
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
        //�ҵ�����
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
        //������Ϣ
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
	 * ���ø�CommonActivity����߳�
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
