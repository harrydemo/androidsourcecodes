package com.jclt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class SecondActivity extends CommonActivity {
	private int BOYSHOES = 0;
	private int GIRLSHOES = 1;
	private int MORESHOES = 2;
	
	private ListAdapter adapter = null ;
    private float density = 0 ;
	private List<Map<String, Object>> list = null;
	
	/**=================Gallery===============*/
	 private Gallery pictureGallery = null ;
	 private int[] picture = {
			 R.drawable.gallery1,
			 R.drawable.gallery2,
			 R.drawable.gallery3,
			 R.drawable.gallery4,
			 R.drawable.gallery5,
			 R.drawable.gallery6,
	 };
	 private int index = 0 ;
	/**=======================================*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// ȥ���ֻ�����Ĭ�ϱ���
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second_tiem);
		// �ֻ������������
		super.textViewTitle = (TextView) findViewById(R.id.title);
		super.textViewTitle.setText(R.string.app_name);
		
		//
	this.pictureGallery = (Gallery)findViewById(R.id.gallery);
	ImageAdapter adapter = new ImageAdapter(this);
	this.pictureGallery.setAdapter(adapter);
		Timer timer = new Timer();
		timer.schedule(task, 2000, 2000);
		this.parentControl();
	}
	
	/**
	 * ��ʱ��
	 */
	private TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			Message message = new Message();
			message.what = 2 ; 
			index = pictureGallery.getSelectedItemPosition();
			index ++ ;
			handler.sendMessage(message);
		}
	};
	
	/**
	 * Handler
	 */
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				pictureGallery.setSelection(index);
				break;

			default:
				break;
			}
		}
		
	};
	/**
	 * CommonActivity�Ĺ����ؼ�
	 */
	private void parentControl(){
		//��ȡ�ܶ�
		 this.getDensity();
		// �ײ��˵�������¼�Ч������
		imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
		imageViewIndex.setOnTouchListener(viewIndex);
		imageViewIndex.setImageResource(R.drawable.menu_home_pressed);
		
		imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
		imageViewType.setOnTouchListener(viewType);
		
		imageViewShooping = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewShooping.setOnTouchListener(viewShooping);

		imageViewMyLetao = (ImageView) findViewById(R.id.menu_my_letao_img);
		imageViewMyLetao.setOnTouchListener(viewMyLetao);
		
		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnTouchListener(viewMore);
		// ��XML�е�ListView����ΪItem������
		super.listViewAll = (ListView) findViewById(android.R.id.list);
		initDate();
		// ����ý���ʱ,ģ�´ӷ�������������ʱ�����������
//		super.progressDialog = ProgressDialog.show(this, "��ͨ����", "���ݻ�ȡ��....",
//				true);
		//super.progressDialog.show();
		// ͨ���߳���ѭ�����ý�����
		//super.handler.post(this);
	}
	
	
	/**
	 * ��ȡ��Ļ���ܶ�
	 */
    private void getDensity(){
    	DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		density = metrics.density;
    }
    
    /**
     * ��ȡListView������
     * @return
     */
	private List<Map<String, Object>> getDate() {
		List<Map<String, Object>> listLitong = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < TYPE.length; i++) {
			Map<String, Object> maplitong = new HashMap<String, Object>();
			maplitong.put("text", TYPE[i]);
			maplitong.put("img", R.drawable.toright_mark);
			maplitong.put("img_pre", R.drawable.paopao);
			listLitong.add(maplitong);
		}

		return listLitong;
	}
    
	/**
	 * ListView����
	 */
	static final String[] TYPE = { "��Ь", "ŮЬ", "�������", "��Ʒ", "Ʒ�ƹ�", "ÿ�վ�Ʒ" };
     
	
	/**
	 * �ڲ���
	 * ListView������
	 * @author Administrator
	 *
	 */
	class listViewLitongOnclickListener implements OnItemClickListener {
		//private Intent intent = getIntent();
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position,
				long id) {
			if (position == BOYSHOES) {
				intent.setClass(SecondActivity.this, BoyShoesActivity.class);
				startActivity(intent);
			} else if (position == GIRLSHOES) {
				intent.setClass(SecondActivity.this, GirlShoesActivity.class);
				startActivity(intent);
			} else if (position == MORESHOES) {
				intent.setClass(SecondActivity.this, MoreShoesActivity.class);
				startActivity(intent);
			}else {
				intent.setClass(SecondActivity.this, NewProMarketActivity.class);
				startActivity(intent);
			}

		}

	}
	
	/**
	 * ����һ��������(�Ѹ��������󶨵�ListView����)
	 * 
	 * @author TanRuixiang
	 * @date 2011��8��3��, PM 03:06:36
	 */
	class ListAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		public final class Holder {
			ImageView picture;
			TextView text;
			ImageView navigation;
		}
        public ListAdapter(Context context){
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
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null) {
				holder = new Holder();
				convertView = inflater.inflate(R.layout.common_listview_text, null);
				holder.picture = (ImageView) convertView.findViewById(R.id.img_pre);
				holder.text = (TextView) convertView.findViewById(R.id.text);
				holder.navigation = (ImageView)convertView.findViewById(R.id.img);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.picture.setImageResource(R.drawable.paopao);
			holder.text.setText((String) list.get(position).get("text"));
			holder.navigation.setImageResource(R.drawable.toright_mark);
			return convertView;
		}
		
	}
	/**
	 * �õ���������List�Ĵ�С</br> ���List�����ڿ� ���� List�Ĵ�С������</br>
	 * ʵ����һ���Զ����������,���Զ�����������󶨵��ؼ�ListView����</br>
	 * ʵ����һ���Զ���ļ�����,���Զ���ļ������󶨵��ؼ�ListView����</br> �ж���Ļ���ܶ� </br> ����ܶ�С��1.0
	 * �ܶȵ���1.5</br> �͸�ListView�ֶ����ÿ�Ⱥ͸߶�</br>
	 */
	private void initDate(){
		list = getDate();
		if(list != null && list.size() > 0 ){
			adapter = new ListAdapter(this);
			super.listViewAll.setAdapter(adapter);
			super.listViewAll.setOnItemClickListener(new listViewLitongOnclickListener());
			if(density < 1.0 || density == 1.5){
				super.listViewAll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, (int) (list
						.size() * 48 * density)));
			}else{
				super.listViewAll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, (int) (list
						.size() * 47 * density)));
			}
		}
	}

	
	
	/**
	 * �Զ���һ��ͼƬ��ʾ������
	 * @author TanRuixiang
	 *
	 */
	class ImageAdapter extends BaseAdapter{
		private int GalleryItemBackground;
	    private Context context ; 
	   public ImageAdapter(Context context ){
		   this.context = context ; 
		   TypedArray typedArray = context.obtainStyledAttributes(R.styleable.Gallery);
		   GalleryItemBackground = typedArray.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
	   } 
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			imageView.setImageResource(picture[position % picture.length]);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.FILL_PARENT));
			imageView.setBackgroundResource(GalleryItemBackground);
			return imageView;
		}
		
	}
	
	@Override
	protected void onResume() {
		if (CommonActivity.exit) {
			CommonActivity.exit = false;
			finish();
		}
		
		super.onResume();
	}
	/**
	 * �˳�
	 */
	private void openQiutDialog() {
		new AlertDialog.Builder(this).setTitle("��ͨ����").setMessage("�Ƿ��˳���ͨ���")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						 System.exit(0);
						 finish();
						}
				}).setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		   if(keyCode==KeyEvent.KEYCODE_BACK){
			   openQiutDialog();
			   return true;
		   }
		return super.onKeyDown(keyCode, event);
	}

}
