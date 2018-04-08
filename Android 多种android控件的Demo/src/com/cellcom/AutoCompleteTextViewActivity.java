package com.cellcom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

//�Զ���ʾ��
public class AutoCompleteTextViewActivity extends Activity {

	private AutoCompleteTextView autoComplete;
	private Button cleanButton;
	static final String[] COUNTRIES = new String[] {
		"China" ,"Russia", "Germany",
		"Ukraine", "Belarus", "USA" ,"China1" ,"China12", "Germany",
		"Russia2", "Belarus", "USA" ,"UAA","UBC","UBB","CCC","BBB","����1","����2","����3","����4","�㶫1","�㶫2","�㶫3",
	    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_complete_text_view);
		setTitle("AutoCompleteTextViewʾ����");
		autoComplete=(AutoCompleteTextView)findViewById(R.id.auto_complete);
		cleanButton=(Button)findViewById(R.id.cleanButton);
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(AutoCompleteTextViewActivity.this,android.R.layout.simple_dropdown_item_1line,COUNTRIES);
		autoComplete.setAdapter(adapter);
		
		//���
		cleanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				autoComplete.setText("");
			}
		});
	}
	
}
