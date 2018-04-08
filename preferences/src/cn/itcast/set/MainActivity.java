package cn.itcast.set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText nameText;
    private EditText ageText;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        nameText = (EditText)this.findViewById(R.id.name);
        ageText = (EditText)this.findViewById(R.id.age);
        Button button = (Button)this.findViewById(R.id.button);
        Button button2 = (Button)this.findViewById(R.id.button2);
        ButtonClickListener listener = new ButtonClickListener();
        button2.setOnClickListener(listener);        
        button.setOnClickListener(listener);
    }
    
    private final class ButtonClickListener implements View.OnClickListener{
    	@Override
		public void onClick(View v) {
    		SharedPreferences preferences = getSharedPreferences("itcast", Context.MODE_WORLD_READABLE);
    		switch (v.getId()) {
			case R.id.button:
				String name = nameText.getText().toString();
				String age = ageText.getText().toString();
				Editor editor = preferences.edit();
				editor.putString("name", name);
				editor.putInt("age", new Integer(age));
				editor.commit();
				Toast.makeText(MainActivity.this, R.string.success, 1).show();
				break;

			case R.id.button2:
				nameText.setText(preferences.getString("name", ""));
				ageText.setText(String.valueOf(preferences.getInt("age", 20)));
				break;
			}
			
		}
    }
}