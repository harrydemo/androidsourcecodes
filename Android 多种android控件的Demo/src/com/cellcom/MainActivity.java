package com.cellcom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author nwang
 * 
 *	android�������֡�����ȵȲ�����
 */
public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Button frameLayout;
	private Button relativeLayout;
	private Button relativeAndLinear;
	private Button tableLayout;
	//ѡ���ť
	private Button tabWidget;
	private Button checkbox;
	private Button radionGroup;
	private Button spinner;
	private Button autoCompleteTextView;
	private Button datePicker;
	private Button progressBar;
	private Button ratingBar;
	private Button imageShow;
	private Button gridView;
	private Button tabDemo;
	private Button menu1;
	private Button menu2;
	private Button menu3;
	private Button bundle;
	private Button alertDialog;
	private Button notification;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        frameLayout=(Button)findViewById(R.id.frameLayout);
        relativeLayout=(Button)findViewById(R.id.relativeLayout);
        relativeAndLinear=(Button)findViewById(R.id.relativeAndLinear);
        tableLayout=(Button)findViewById(R.id.tableLayout);
        tabWidget=(Button)findViewById(R.id.tabWidget);
        checkbox=(Button)findViewById(R.id.checkbox);
        radionGroup=(Button)findViewById(R.id.radioGroup);
        spinner=(Button)findViewById(R.id.spinner);
        autoCompleteTextView=(Button)findViewById(R.id.autoCompleteTextView);
        datePicker=(Button)findViewById(R.id.datePicker);
        progressBar=(Button)findViewById(R.id.progressBar);
        ratingBar=(Button)findViewById(R.id.ratingBar);
        imageShow=(Button)findViewById(R.id.imageShow);
        gridView=(Button)findViewById(R.id.gridView);
        tabDemo=(Button)findViewById(R.id.tabDemo);
        menu1=(Button)findViewById(R.id.menu1);
        menu2=(Button)findViewById(R.id.menu2);
        menu3=(Button)findViewById(R.id.menu3);
        bundle=(Button)findViewById(R.id.bundle);
        alertDialog=(Button)findViewById(R.id.alertDialog);
        notification=(Button)findViewById(R.id.notification);
        
        //FrameLayout����ʹ��
        frameLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, FrameLayoutActivity.class);
				startActivity(intent);
			}
		});
        
        //RelativeLayout����ʹ��
        relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, RelativeLayoutActivity.class);
				startActivity(intent);
			}
		});
        
        //RelativeLayout��LinearLayout�ۺ�ʹ��
        relativeAndLinear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, RelativeAndLinearActivity.class);
				startActivity(intent);
			}
		});
        
        //TableLayout����ʹ��
        tableLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, TableLayoutActivity.class);
				startActivity(intent);
			}
		});
        
        //�л�ѡ�TabWidget
        tabWidget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, TabWidgetActivity.class);
				startActivity(intent);
			}
		});
        
        //��ѡ�ؼ�CheckBox
        checkbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, CheckBoxActivity.class);
				startActivity(intent);
			}
		});
        
        //��ѡ�ؼ�RadioGroupʹ��
        radionGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, RadioGroupActivity.class);
				startActivity(intent);
			}
		});
        
        //������ʹ��
        spinner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, SpinnerActivity.class);
				startActivity(intent);
			}
		});
        
        //�Զ���ʾ��
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =new Intent();
				intent.setClass(MainActivity.this, AutoCompleteTextViewActivity.class);
				startActivity(intent);
			}
		});
        
        //����ѡ����ʹ��
        datePicker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, DatePickerActivity.class);
				startActivity(intent);
			}
		});
        
        //������ʹ��
        progressBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, ProgressBarHandlerActivity.class);
				startActivity(intent);
			}
		});
        
        //�������RatingBar
        ratingBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, RatingBarActivity.class);
				startActivity(intent);
			}
		});
        
        //���ͼƬ
        imageShow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, ImageShowActivity.class);
				startActivity(intent);
			}
		});
        
        //������ͼ
        gridView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, GridViewActivity.class);
				startActivity(intent);
			}
		});
        
        //��ǩ�ؼ�tab
        tabDemo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, TabDemoActivity.class);
				startActivity(intent);
			}
		});
        
        //OptionsMenu�˵�
        menu1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, OptionsMenuActivity.class);
				startActivity(intent);
			}
		});
        
      //ContextMenu�˵�
        menu2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, ContextMenuActivity.class);
				startActivity(intent);
			}
		});
        
      //SubMenu�˵�
        menu3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, SubMenuActivity.class);
				startActivity(intent);
			}
		});
        
        //Activityֵ����
        bundle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, Bundle1Activity.class);
				startActivity(intent);
			}
		});
        
        //4�жԻ���
        alertDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, AlertDialogActivity.class);
				startActivity(intent);
			}
		});
        
        //Notification״̬����ʾ
        notification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, NotificationActivity.class);
				startActivity(intent);
			}
		});
    }
}