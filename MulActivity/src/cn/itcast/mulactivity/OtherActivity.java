package cn.itcast.mulactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OtherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);
		
		TextView resultView = (TextView)this.findViewById(R.id.result);
		Intent intent = getIntent();
		String name = intent.getExtras().getString("name");
		int age = intent.getIntExtra("age", 0);
		
		resultView.setText("���ƣ�"+ name+ ",���ޣ�"+ age);
		
		Button button = (Button)this.findViewById(R.id.button);
	        button.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					Intent data = new Intent();
					data.putExtra("result", "���Ƿ�������");
					setResult(900, data);
					finish();//�رյ�ǰActivity
				}
			});
	}

}
