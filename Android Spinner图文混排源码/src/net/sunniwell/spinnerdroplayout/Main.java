package net.sunniwell.spinnerdroplayout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        
        Spinner sp=new Spinner(this);
        
//        BaseAdapter<String> sa = new BaseAdapter<String>();
        
        AddAdapter adapter = new AddAdapter(this);
        
        
        sp.setAdapter(adapter);
        
        
        
        LinearLayout  ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.BLUE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.FILL_PARENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT
        );
        
        ll.addView(sp, params);
        
        this.setContentView(ll);
        
    }
}