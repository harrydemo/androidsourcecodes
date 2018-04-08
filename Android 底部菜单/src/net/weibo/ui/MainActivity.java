package net.weibo.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	public TabHost mth;
	public static final String TAB_HOME="Ê×Ò³";
	public static final String TAB_MSG="ÐÅÏ¢";
	public RadioGroup radioGroup;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mth=this.getTabHost();
        
        TabSpec ts1=mth.newTabSpec(TAB_HOME).setIndicator(TAB_HOME);
        ts1.setContent(new Intent(MainActivity.this,FirstActivity.class));
        mth.addTab(ts1);
        
        TabSpec ts2=mth.newTabSpec(TAB_MSG).setIndicator(TAB_MSG);
        ts2.setContent(new Intent(MainActivity.this,SecondActivity.class));
        mth.addTab(ts2);
        
        this.radioGroup=(RadioGroup)findViewById(R.id.main_radio);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
				switch(checkedId){
				case R.id.radio_button0:
					mth.setCurrentTabByTag(TAB_HOME);
					break;
				case R.id.radio_button1:
					mth.setCurrentTabByTag(TAB_MSG);
					break;
				case R.id.radio_button2:
					Log.d("select ID", "===={"+checkedId);
					break;
				case R.id.radio_button3:
					Log.d("select ID", "===={"+checkedId);
					break;
				case R.id.radio_button4:
					Log.d("select ID", "===={"+checkedId);
					break;
				}
			}
		});
        
    }
}