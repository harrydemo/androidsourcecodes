package cn.itcast.image;

import cn.itcast.service.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
    private EditText pathText;
    private ImageView imageView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        pathText = (EditText)this.findViewById(R.id.path);
        imageView = (ImageView)this.findViewById(R.id.imageView);
        Button button = (Button)this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				try {
					byte[] data = ImageService.getImage(pathText.getText().toString());//得到图片的二进制数据
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//生成位图
					imageView.setImageBitmap(bitmap);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					Toast.makeText(MainActivity.this, R.string.error, 1).show();
				}
			}
		});
    }
}