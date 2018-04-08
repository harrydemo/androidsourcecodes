package cn.zz.activity;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

public class MainApp extends Activity implements OnClickListener{
	Button list=null;
	Button about=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        list=(Button)findViewById(R.id.foodlistbtn);
        about=(Button)findViewById(R.id.aboutbutton);
//        list.setText(text);
//        list.setCompoundDrawables(left, top, right, bottom);
        list.setOnClickListener(this);
        about.setOnClickListener(this);
        
    }
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
    	if(v.getId()==R.id.foodlistbtn){
		Intent intent=new Intent();
		intent.setClass(MainApp.this, FoodListView.class);
		startActivity(intent);
		list.setBackgroundResource(R.drawable.btn_food_list_active);
    	}else if(v.getId()==R.id.aboutbutton){
    		Intent intent=new Intent(this, About.class);
    		startActivity(intent);
    		about.setBackgroundResource(R.drawable.btn_food_about_active);
    	}
    }
}