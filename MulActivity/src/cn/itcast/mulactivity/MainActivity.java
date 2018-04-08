package cn.itcast.mulactivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, OtherActivity.class);
				//intent.setClass(MainActivity.this, OtherActivity.class);
				//intent.setComponent(new ComponentName(MainActivity.this, OtherActivity.class));
				
				//intent.putExtra("name", "传智播客");
				//intent.putExtra("age", 4);
				Bundle bundle = new Bundle();
				bundle.putString("name", "传智播客");
				bundle.putInt("age", 4);
				intent.putExtras(bundle);
				
				//startActivity(intent);//打开(激活)Activity
				startActivityForResult(intent, 100);
			}
		});
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(this, data.getStringExtra("result"), 1).show();
		
		super.onActivityResult(requestCode, resultCode, data);
	}
    
    
}