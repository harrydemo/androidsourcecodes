package src.youer.text;

import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MultiTouchTestActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView textView = (TextView) this.findViewById(R.id.text_view);
		try
		{
			textView.setText(readText());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float zoomScale = 0.5f;// Ëõ·Å±ÈÀý
		new ZoomTextView(textView, zoomScale);
	}

	/**
	 * ¶ÁÈ¡txt
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String readText() throws Exception
	{
		InputStream is = this.getClass()
				.getResourceAsStream("/assets/text.txt");
		int index = is.available();
		byte data[] = new byte[index];
		is.read(data);
		return new String(data, "UTF-8");
	}
}