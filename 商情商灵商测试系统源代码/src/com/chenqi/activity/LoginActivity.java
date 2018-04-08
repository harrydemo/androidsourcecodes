package com.chenqi.activity;
import java.util.ArrayList;
import com.chenqi.service.UserService;
import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    EditText userText=null;
    EditText pwdText=null;
    Button loginBtn=null;
    Button cancelBtn=null;
    String username;
    String pwd;
    UserService userService;
    private boolean success;
    private GestureLibrary library;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userText=(EditText) this.findViewById(R.id.username);
        pwdText=(EditText) this.findViewById(R.id.pwd);
        
        loginBtn=(Button) this.findViewById(R.id.Login);
        cancelBtn=(Button) this.findViewById(R.id.cancel);
        loginBtn.setOnClickListener(new buttonClickListener());
        cancelBtn.setOnClickListener(new buttonClickListener());
        userService=new UserService(this);
        library = GestureLibraries.fromRawResource(this, R.raw.gestures);
        //加载手势库
        success = library.load();
        GestureOverlayView gestureView = (GestureOverlayView)this.findViewById(R.id.gestures);
        gestureView.addOnGesturePerformedListener(new GestureListener());

    }
    private final class GestureListener implements OnGesturePerformedListener{
		@Override
		public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
			if(success){
				//从手势库中查找匹配的手势,最匹配的记录会放在最前面
				ArrayList<Prediction> predictions = library.recognize(gesture);
				if(!predictions.isEmpty()){
					Prediction prediction = predictions.get(0);
					
					if(prediction.score>3){
						if("login".equals(prediction.name)){
							Intent intent=new Intent(LoginActivity.this,MainActivity.class);
							startActivity(intent);
						}else if("exit".equals(prediction.name)){
							finish();
						}
					}
				}
			}
		}
    	
    }
    private class buttonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0)
		{
			username=userText.getText().toString();
	        pwd=pwdText.getText().toString();
			switch(((Button)arg0).getId()){
			case R.id.Login:
			Boolean result=	new UserService(LoginActivity.this).checkUser(username, pwd);
			if(result){
				Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
			}
			else {
				Toast.makeText(LoginActivity.this, R.string.fail, Toast.LENGTH_LONG).show();
			}
			break;
			case R.id.cancel:
				finish();
				break;
			}
		}
    	
    }
}