package cn.itcast.intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("cn.itcast.laozhang");//“˛ Ω“‚Õº
				intent.addCategory("cn.itcast.category.java");
				intent.setDataAndType(Uri.parse("itcast://www.itcast.cn/liming"), "image/gif");
				startActivity(intent);
			}
		});
    }
}