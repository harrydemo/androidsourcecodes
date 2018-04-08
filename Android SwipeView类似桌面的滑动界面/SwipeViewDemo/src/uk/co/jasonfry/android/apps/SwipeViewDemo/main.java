package uk.co.jasonfry.android.apps.SwipeViewDemo;

import uk.co.jasonfry.android.apps.SwipeViewDemo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class main extends Activity
{
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        
        button1.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				Intent i = new Intent(main.this, SwipeViewDemo1.class);
				startActivity(i);
			}
		});
        
        button2.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				Intent i = new Intent(main.this, SwipeViewDemo2.class);
				startActivity(i);
			}
		});
        
        button3.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				Intent i = new Intent(main.this, SwipeViewDemo3.class);
				startActivity(i);
			}
		});
    }
}
