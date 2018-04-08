package wgw.temp.test;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		text=(TextView) this.findViewById(R.id.text);
		text.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
	}
}
