package org.android.test;


import org.android.R;
import org.android.panel.Panel;
import org.android.panel.Panel.OnPanelListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Test extends Activity {

	Button btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(btnClick);
       
       
    }
    
    View.OnClickListener btnClick = new View.OnClickListener() {
		
		public void onClick(View v) {
			Log.i("","sjglsjglsjgl................click");
			
			Toast.makeText(Test.this,"button clicked", 100).show();
		}
	};

	
}
