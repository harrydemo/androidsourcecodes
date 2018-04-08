package cn.itcast.file;

import cn.itcast.service.FileService;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
    private EditText filenameText;
    private EditText filecontentText;
    private FileService service;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        service = new FileService(this);
        
        filenameText = (EditText)this.findViewById(R.id.filename);
        filecontentText = (EditText)this.findViewById(R.id.filecontent);
        Button button = (Button)this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String filename = filenameText.getText().toString();
				String content = filecontentText.getText().toString();
				try {
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
						service.saveToSDCard(filename, content);
						Toast.makeText(MainActivity.this, R.string.success, 1).show();
					}else{
						Toast.makeText(MainActivity.this, R.string.sdcarderror, 1).show();
					}
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					Toast.makeText(MainActivity.this, R.string.error, 1).show();
				}
			}
		});
    }
}