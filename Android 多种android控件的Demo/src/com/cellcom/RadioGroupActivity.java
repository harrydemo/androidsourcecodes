package com.cellcom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

//��ѡ�ؼ�
public class RadioGroupActivity extends Activity {

	private RadioButton radioFemale;
	private RadioButton radioMale;
	private Button ok;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.radio_group);
		
		radioFemale=(RadioButton)findViewById(R.id.radioFemale);
		radioMale=(RadioButton)findViewById(R.id.radioMale);
		ok=(Button)findViewById(R.id.ok);
		
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String result="��ѡ����ǣ�";
				if(radioFemale.isChecked()){
					setTitle(result+"Ů");
				}else{
					setTitle(result+"��");
				}
			}
		});
	}

}
