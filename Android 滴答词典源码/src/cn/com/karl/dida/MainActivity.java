package cn.com.karl.dida;


import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends TabActivity {

	private TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		tabHost=this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        
        intent=new Intent().setClass(this, DidaActivity.class);
        spec=tabHost.newTabSpec("�ʵ�").setIndicator("�ʵ�").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this,WordsBookActivity.class);
        spec=tabHost.newTabSpec("����").setIndicator("����").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this, TransActivity.class);
        spec=tabHost.newTabSpec("����").setIndicator("����").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this,TestActivity.class);
        spec=tabHost.newTabSpec("����").setIndicator("����").setContent(intent);
        tabHost.addTab(spec);
     
        intent=new Intent().setClass(this, MoreActivity.class);
        spec=tabHost.newTabSpec("����").setIndicator("����").setContent(intent);
        tabHost.addTab(spec);
        
        RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.main_radio);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radio_button0://�ʵ�
					tabHost.setCurrentTabByTag("�ʵ�");
					break;
				case R.id.radio_button1://����
					tabHost.setCurrentTabByTag("����");
					break;
				case R.id.radio_button2://����
					tabHost.setCurrentTabByTag("����");
					break;
				case R.id.radio_button3://����
					tabHost.setCurrentTabByTag("����");
					break;
				case R.id.radio_button4://����
					tabHost.setCurrentTabByTag("����");
					break;
				default:
					tabHost.setCurrentTabByTag("�ʵ�");
					break;
				}
			}
		});
	}
}
