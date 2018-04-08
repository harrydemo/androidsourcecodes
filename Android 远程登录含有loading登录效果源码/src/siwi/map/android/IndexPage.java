package siwi.map.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class IndexPage extends Activity {
	private Button reback_button;
	private TextView view_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indexpage);
		findView();
		Bundle bundle=this.getIntent().getExtras();
		String userName=bundle.getString("MAP_USERNAME");
		view_result.setText("hello  "+userName+" ,login success!");
		setListener();
	}	
	
	private OnClickListener back = new OnClickListener() {
		@Override
		public void onClick(View v) {
			IndexPage.this.finish();
		}
	};
	
	private void setListener(){
		reback_button.setOnClickListener(back);
	}
	
	private void findView() {
		reback_button = (Button) findViewById(R.id.report_back);
		view_result = (TextView) findViewById(R.id.result); 
	}
}
