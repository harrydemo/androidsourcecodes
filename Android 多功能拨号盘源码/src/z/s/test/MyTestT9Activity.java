package z.s.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MyTestT9Activity extends Activity {
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Intent intent = new Intent();
		intent.setClass(MyTestT9Activity.this, T9.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		MyTestT9Activity.this.startActivity(intent);
    }
}