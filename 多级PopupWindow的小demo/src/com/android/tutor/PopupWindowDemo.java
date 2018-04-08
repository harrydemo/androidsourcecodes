package com.android.tutor;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PopupWindowDemo extends Activity{
	
    ImageButton img;
    TextView tv;
    PopupWindow mPopupWindow;
    Button bt;
	String str="";
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bt=(Button)findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener()
        {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow();
			}
		}); 
    }


    
	public void showPopupWindow() 
	{
		Context mContext = PopupWindowDemo.this;
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View music_popunwindwow = mLayoutInflater.inflate(
				R.layout.music_popwindow, null);
		mPopupWindow= new PopupWindow(music_popunwindwow,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		mPopupWindow.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
		img=(ImageButton)music_popunwindwow.findViewById(R.id.control_play);
	    img.setOnClickListener(new View.OnClickListener() 
	    {
			
			@Override
			public void onClick(View v)
			{
				secondPop();
			}
		});
	  }
	    
	  public void secondPop() 
	  {
			Context mContext = PopupWindowDemo.this;
			LayoutInflater mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View second= mLayoutInflater.inflate(
					R.layout.secondpopwindow, null);
			PopupWindow mPopupWindow= new PopupWindow(second,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			mPopupWindow.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 50);
			ImageButton img1=(ImageButton)second.findViewById(R.id.control_play);
		    img1.setOnClickListener(new View.OnClickListener() 
		    {
				
				@Override
				public void onClick(View v)
				{
					Toast.makeText(PopupWindowDemo.this, "second", Toast.LENGTH_LONG).show();
				    
				}
			});
	   }
			
		
	}
