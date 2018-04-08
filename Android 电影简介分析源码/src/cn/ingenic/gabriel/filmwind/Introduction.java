package cn.ingenic.gabriel.filmwind;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Introduction extends Activity {
	private TextView mTxtView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introdunction);
		mTxtView = (TextView) findViewById(R.id.intro_info);
		mTxtView.setMovementMethod(ScrollingMovementMethod.getInstance()); //ÈÃTextView¶¯ÆðÀ´
		getData();
	}

	private void getData() {
		final Bundle bundle = getIntent().getExtras();
		final int pos = bundle.getInt(Parameters.BUNDLE_CLICK_POSITION);
		final Resources resources = getResources();
		mTxtView.setText(resources.getStringArray(R.array.wind_activity_info)[pos]);
		TypedArray ta = resources.obtainTypedArray(R.array.wind_activity_image);
		mTxtView.setCompoundDrawablesWithIntrinsicBounds(ta.getResourceId(pos, R.drawable.wind_list_image_all), 0, 0, 0);
		ta.recycle();
	}
}
