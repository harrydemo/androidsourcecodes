package com.cellcom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

//�������RatingBar��ImageViewͼƬ�� ImageButtonͼƬ��ť
public class RatingBarActivity extends Activity {

	private RatingBar ratingBar;
	private ImageButton imageButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating_bar);
		setTitle("�������RatingBar��ImageViewͼƬ�� ImageButtonͼƬ��ť");
		ratingBar=(RatingBar)findViewById(R.id.rating_bar);
		imageButton=(ImageButton)findViewById(R.id.imageButton);
		
		imageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(RatingBarActivity.this, "����ͼƬ��ť!!", Toast.LENGTH_LONG).show();
			}
		});
	}
}
