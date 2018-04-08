package best.Zoom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ZoomControls;

	public class Zoom extends Activity {
		private ZoomControls zoomControls;
		static long size = 12;
		private TextView text;
		@Override
	protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("ZoomControls");
		zoomControls = (ZoomControls) findViewById(R.id.zoomcontrols);
		text = (TextView) findViewById(R.id.text);
		zoomControls.setOnZoomInClickListener(new OnClickListener() {
		@Override
	public void onClick(View v) {
		size = size + 2;
		text.setTextSize(size);
		}
	});
		zoomControls.setOnZoomOutClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		size = size - 2;
		text.setTextSize(size);
		}
	});
   }
}
