package com.jclt.activity.type;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.jclt.activity.CommonActivity;
import com.jclt.activity.R;
import com.jclt.adapter.GalleryImageAdapter;
import com.jclt.custom.BrandGallery;

public class CommondityInforLetaoActivity extends CommonActivity implements OnClickListener , OnItemSelectedListener {
	/**
	 * 品牌介绍
	 */
	private TextView brandInforTextView = null;
	/**
	 * 价格信息
	 */
	private TextView priceTextView = null;
	/**
	 * 美元为单位
	 */
	private TextView dollarTextView = null;
	/**
	 * 立即购买
	 */
	private TextView buyTextView = null;
	/**
	 * 加入购物车
	 */
	private TextView shoppingCarTextView = null;
	/**
	 * 上市时间
	 */
	private TextView timeTextView = null;
	/**
	 * 款式
	 */
	private TextView styleTextView = null;
	/**
	 * 材质
	 */
	private TextView materialQualityTextView = null;
	
	/**
	 * 货号
	 */
	private TextView itemNoTextView = null;
	/**
	 * 男女款
	 */
	private TextView bandGstyleTextView = null;
	/**
	 * 颜色
	 */
	private TextView colorTextView = null;
	/**
	 * 专柜价格
	 */
	private TextView shoppePriceTextView = null;
	/**
	 * 乐淘价格
	 */
	private TextView letaoPriceTextView = null;
	/**
	 * 收藏
	 */
	private TextView collectTextView = null;
    
	/**
	 * 商品图片展示
	 */
	private BrandGallery imagesGallery = null ;
	/**
	 * 左边箭头按钮
	 */
	private ImageView leftImageView = null ;
	/**
	 * 右边箭头按钮
	 */
	private ImageView rightImageView = null ;
	/**
	 * value_hassize
	 */
	private LinearLayout codeLinearLayout = null ; 
	private GalleryImageAdapter adapter = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除手机界面默认标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.letao_type_commodity_infor);
		// 手机界面标题设置
		super.textViewTitle = (TextView) findViewById(R.id.title);
		super.textViewTitle.setText(R.string.app_name);
		
		this.initTextViewAll();
	}

	@Override
	public void onClick(View view) {
		if (view == buyTextView) {
		   Toast toast = Toast.makeText(getApplicationContext(), "购买成功!", Toast.LENGTH_SHORT);
		   		toast.setGravity(Gravity.CENTER, 0, 0);
		   		toast.show();
		} else if (view == shoppingCarTextView) {
			Toast toast = Toast.makeText(getApplicationContext(), "添加成功!", Toast.LENGTH_SHORT);
	   		toast.setGravity(Gravity.CENTER, 0, 0);
	   		toast.show();
		} else if (view == collectTextView) {
			Toast toast = Toast.makeText(getApplicationContext(), "收藏成功!", Toast.LENGTH_SHORT);
	   		toast.setGravity(Gravity.CENTER, 0, 0);
	   		toast.show();
		}else if(view == codeLinearLayout){//
			//codeTextView.setBackgroundColor(Color.RED);
			new AlertDialog.Builder(this).setTitle("你的脚多大啊？").setIcon(
				     android.R.drawable.ic_dialog_info).setSingleChoiceItems(
				    		 new String[] { "41", "42","43","44","45" }, 0,
				     new DialogInterface.OnClickListener() {
				      public void onClick(DialogInterface dialog, int which) {
				       
				      }
				     }).setNegativeButton("确定", null).show();




			System.out.println("TanRuixiang");
		}
		;
		
	}
	
	
	/**
	 * 加载(绑定)所有控件
	 * @Author TanRuixiang
	 */
	private void initTextViewAll(){
		brandInforTextView = (TextView)findViewById(R.id.shoe_title);
		brandInforTextView.setText("美特斯邦威休闲鞋");
		priceTextView = (TextView)findViewById(R.id.lable_specially_price);
		priceTextView.setText("专享价格:");
		dollarTextView = (TextView)findViewById(R.id.value_specially_price);
		dollarTextView.setText("$120");
		//立即购买按钮
		buyTextView = (TextView)findViewById(R.id.detail_tobuymust_button);
		buyTextView.setOnClickListener(this);
		//加入购物车按钮
		shoppingCarTextView = (TextView)findViewById(R.id.detail_buy_button);
		shoppingCarTextView.setOnClickListener(this);
		timeTextView = (TextView)findViewById(R.id.value_listing_date);
		timeTextView.setText("2011年8月16日");
		styleTextView = (TextView)findViewById(R.id.value_style);
		styleTextView.setText("潮流款式");
		materialQualityTextView = (TextView)findViewById(R.id.value_textures);
		materialQualityTextView.setText("EVA");
		itemNoTextView = (TextView)findViewById(R.id.value_model);
		itemNoTextView.setText("ISO9001(湘)");
//		itemNoTextView = (TextView)findViewById(R.id);
		bandGstyleTextView = (TextView)findViewById(R.id.value_man_woman);
		bandGstyleTextView.setText("中性款式");
		colorTextView = (TextView)findViewById(R.id.value_colors);
		colorTextView.setText("红,黄,蓝,绿");
		shoppePriceTextView = (TextView)findViewById(R.id.value_counter_price);
		shoppePriceTextView.setText("$150");
		letaoPriceTextView = (TextView)findViewById(R.id.value_letao_price);
		letaoPriceTextView.setText("$120");
		//收藏按钮
		collectTextView = (TextView)findViewById(R.id.detail_favorite_button);
		collectTextView.setOnClickListener(this);
		//商品图片展示
		adapter = new GalleryImageAdapter(this);
		imagesGallery = (BrandGallery)findViewById(R.id.commodity_detail_gallery);
		imagesGallery.setAdapter(adapter);
		imagesGallery.setOnItemSelectedListener(this);
		leftImageView = (ImageView)findViewById(R.id.commodity_detail_left_Img);
		leftImageView.setVisibility(View.GONE);
		rightImageView = (ImageView)findViewById(R.id.commodity_detail_right_Img);
		codeLinearLayout = (LinearLayout)findViewById(R.id.detail_size_layout);
		codeLinearLayout.setOnClickListener(this);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if(position > 0){
			leftImageView.setVisibility(View.VISIBLE);
			if(position == adapter.images.length - 1){
				rightImageView.setVisibility(View.GONE);
				leftImageView.setVisibility(View.VISIBLE);
			}else{
				rightImageView.setVisibility(View.VISIBLE);
			}
		}else if(position == 0){
			leftImageView.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
}
