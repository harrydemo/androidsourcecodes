package fishjoy.control.help;


import fishjoy.control.menu.MainMenu;
import fishjoy.control.menu.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;

public class Help extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		//���Gallery����
		Gallery g = (Gallery) findViewById(R.id.Gallery01);

		//���ImageAdapter��Gallery����
		g.setAdapter(new ImageAdapter(this));

		//����Gallery�ı���
		g.setBackgroundResource(0);
				
		//����Gallery��ͼƬ���
		g.setSpacing(120);
		
		/* findViewById(R.id.button1)ȡ�ò���main.xml�е�help */
		ImageButton returnbutton = (ImageButton) findViewById(R.id.help_back);
		/* ����button���¼���Ϣ */
		returnbutton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
				// �½�һ��Intent���� 
				Intent intent = new Intent();
				// ָ��intentҪ�������� 
				intent.setClass(Help.this, MainMenu.class);
				// ����һ���µ�Activity 
				startActivity(intent);
				// �رյ�ǰ��Activity 
				Help.this.finish();
			}
		});
	}


}
