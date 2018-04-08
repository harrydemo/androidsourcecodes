package com.activtiy;


    import android.app.Activity;  
    import android.os.Bundle;  
    import android.view.KeyEvent;  
    import android.view.View;  
    import android.view.View.OnClickListener;  
    import android.widget.Button;  
    import android.widget.EditText;  
    import android.widget.Toast;  
      
    public class UIActivityActivity extends Activity {  
        /** Called when the activity is first created. */  
        @Override  
        public void onCreate(Bundle savedInstanceState) {  
            super.onCreate(savedInstanceState);  
            setContentView(R.layout.main);  
            
        }  
          
        @Override  
        public boolean onKeyDown(int keyCode, KeyEvent event)  
        {  
            switch (keyCode)  
            {  
                case KeyEvent.KEYCODE_DPAD_CENTER:  
                    Toast.makeText(getBaseContext(),  
                            "Center was clicked",  
                            Toast.LENGTH_LONG).show();  
                    break;                  
                case KeyEvent.KEYCODE_DPAD_LEFT:  
                    Toast.makeText(getBaseContext(),  
                            "Left arrow was clicked",  
                            Toast.LENGTH_LONG).show();  
                    break;                  
                case KeyEvent.KEYCODE_DPAD_RIGHT:  
                    Toast.makeText(getBaseContext(),  
                            "Right arrow was clicked",  
                            Toast.LENGTH_LONG).show();  
                    break;                  
                case KeyEvent.KEYCODE_DPAD_UP:  
                    Toast.makeText(getBaseContext(),  
                            "Up arrow was clicked",  
                            Toast.LENGTH_LONG).show();  
                    break;  
                case KeyEvent.KEYCODE_DPAD_DOWN:  
                    Toast.makeText(getBaseContext(),  
                            "Down arrow was clicked",  
                            Toast.LENGTH_LONG).show();  
                    break;                  
            }  
            return false;  
        }  
      
    }  