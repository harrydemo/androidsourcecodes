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
	 * Ʒ�ƽ���
	 */
	private TextView brandInforTextView = null;
	/**
	 * �۸���Ϣ
	 */
	private TextView priceTextView = null;
	/**
	 * ��ԪΪ��λ
	 */
	private TextView dollarTextView = null;
	/**
	 * ��������
	 */
	private TextView buyTextView = null;
	/**
	 * ���빺�ﳵ
	 */
	private TextView shoppingCarTextView = null;
	/**
	 * ����ʱ��
	 */
	private TextView timeTextView = null;
	/**
	 * ��ʽ
	 */
	private TextView styleTextView = null;
	/**
	 * ����
	 */
	private TextView materialQualityTextView = null;
	
	/**
	 * ����
	 */
	private TextView itemNoTextView = null;
	/**
	 * ��Ů��
	 */
	private TextView bandGstyleTextView = null;
	/**
	 * ��ɫ
	 */
	private TextView colorTextView = null;
	/**
	 * ר��۸�
	 */
	private TextView shoppePriceTextView = null;
	/**
	 * ���Լ۸�
	 */
	private TextView letaoPriceTextView = null;
	/**
	 * �ղ�
	 */
	private TextView collectTextView = null;
    
	/**
	 * ��ƷͼƬչʾ
	 */
	private BrandGallery imagesGallery = null ;
	/**
	 * ��߼�ͷ��ť
	 */
	private ImageView leftImageView = null ;
	/**
	 * �ұ߼�ͷ��ť
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
		// ȥ���ֻ�����Ĭ�ϱ���
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.letao_type_commodity_infor);
		// �ֻ������������
		super.textViewTitle = (TextView) findViewById(R.id.title);
		super.textViewTitle.setText(R.string.app_name);
		
		this.initTextViewAll();
	}

	@Override
	public void onClick(View view) {
		if (view == buyTextView) {
		   Toast toast = Toast.makeText(getApplicationContext(), "����ɹ�!", Toast.LENGTH_SHORT);
		   		toast.setGravity(Gravity.CENTER, 0, 0);
		   		toast.show();
		} else if (view == shoppingCarTextView) {
			Toast toast = Toast.makeText(getApplicationContext(), "��ӳɹ�!", Toast.LENGTH_SHORT);
	   		toast.setGravity(Gravity.CENTER, 0, 0);
	   		toast.show();
		} else if (view == collectTextView) {
			Toast toast = Toast.makeText(getApplicationContext(), "�ղسɹ�!", Toast.LENGTH_SHORT);
	   		toast.setGravity(Gravity.CENTER, 0, 0);
	   		toast.show();
		}else if(view == codeLinearLayout){//
			//codeTextView.setBackgroundColor(Color.RED);
			new AlertDialog.Builder(this).setTitle("��ĽŶ�󰡣�").setIcon(
				     android.R.drawable.ic_dialog_info).setSingleChoiceItems(
				    		 new String[] { "41", "42","43","44","45" }, 0,
				     new DialogInterface.OnClickListener() {
				      public void onClick(DialogInterface dialog, int which) {
				       
				      }
				     }).setNegativeButton("ȷ��", null).show();




			System.out.println("TanRuixiang");
		}
		;
		
	}
	
	
	/**
	 * ����(��)���пؼ�
	 * @Author TanRuixiang
	 */
	private void initTextViewAll(){
		brandInforTextView = (TextView)findViewById(R.id.shoe_title);
		brandInforTextView.setText("����˹��������Ь");
		priceTextView = (TextView)findViewById(R.id.lable_specially_price);
		priceTextView.setText("ר��۸�:");
		dollarTextView = (TextView)findViewById(R.id.value_specially_price);
		dollarTextView.setText("$120");
		//��������ť
		buyTextView = (TextView)findViewById(R.id.detail_tobuymust_button);
		buyTextView.setOnClickListener(this);
		//���빺�ﳵ��ť
		shoppingCarTextView = (TextView)findViewById(R.id.detail_buy_button);
		shoppingCarTextView.setOnClickListener(this);
		timeTextView = (TextView)findViewById(R.id.value_listing_date);
		timeTextView.setText("2011��8��16��");
		styleTextView = (TextView)findViewById(R.id.value_style);
		styleTextView.setText("������ʽ");
		materialQualityTextView = (TextView)findViewById(R.id.value_textures);
		materialQualityTextView.setText("EVA");
		itemNoTextView = (TextView)findViewById(R.id.value_model);
		itemNoTextView.setText("ISO9001(��)");
//		itemNoTextView = (TextView)findViewById(R.id);
		bandGstyleTextView = (TextView)findViewById(R.id.value_man_woman);
		bandGstyleTextView.setText("���Կ�ʽ");
		colorTextView = (TextView)findViewById(R.id.value_colors);
		colorTextView.setText("��,��,��,��");
		shoppePriceTextView = (TextView)findViewById(R.id.value_counter_price);
		shoppePriceTextView.setText("$150");
		letaoPriceTextView = (TextView)findViewById(R.id.value_letao_price);
		letaoPriceTextView.setText("$120");
		//�ղذ�ť
		collectTextView = (TextView)findViewById(R.id.detail_favorite_button);
		collectTextView.setOnClickListener(this);
		//��ƷͼƬչʾ
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
