package com.jackrex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class light extends Activity {
    /** Called when the activity is first created. */
	private LinearLayout mylayout;
	private Resources myColor;
	private int li;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HideStatusBase();
        setContentView(R.layout.light);
        
        //�ı�layout������ɫ
        mylayout=(LinearLayout)findViewById(R.id.mylayout);
        
        SetColor(R.color.white);
        //�����ı䱳����ɫ
        //�ı���Ļ����
        li=0;
        SetBright(1.0f);
        
    }
    
   
    /**
     * ��Ļ����¼���ʾ�˵�
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
    	//Toast.makeText(ColorLightActivity.this, "����", Toast.LENGTH_SHORT).show();
    	openOptionsMenu();
    	return true;
    }
    /**
     * �����˵�
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	getMenuInflater().inflate(R.menu.menu, menu);
    	//menu.findItem(R.id.about).setEnabled(false);
    	return true;
    }
    
    /**
     * ��׽�˵��¼�
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    switch(item.getItemId())
    {
  
    case R.id.setcolor:
    	//Toast.makeText(ColorLightActivity.this, R.string.setcolor, Toast.LENGTH_SHORT).show();
    	selectColor();
    	return true;
    case R.id.setbright:
    	selectBright();
    	//Toast.makeText(ColorLightActivity.this, "���ڲ˵�", Toast.LENGTH_LONG).show();
    	return true;
 
    
    }
    return false;
    }
    /**
     * ѡ����ɫ
     */
    public void selectColor()
    {
    	final String[] items = {"��ɫ", "��ɫ", "��ɫ","��ɫ","��ɫ"}; 
    	new AlertDialog.Builder(this) 
    	.setTitle("ѡ����ɫ") //�˴� this ����ǰActivity 
    	.setItems(items, new DialogInterface.OnClickListener() { 
    	public void onClick(DialogInterface dialog, int item) { 
    	Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show(); //��ѡ�е��ı����ݰ�����˾��ʾ ��ʽ��ʾ����, �˴���getApplicationContext() �õ���Ҳ�ǵ�ǰ��Activity���󣬿��õ�ǰActivity���������.this���棨Activity.this�� 
    	switch (item) {
		case 0:
			SetColor(R.color.white);
			break;
		case 1:
			SetColor(R.color.red);
			break;
		case 2:
			SetColor(R.color.black);
			break;
		case 3:
			SetColor(R.color.yellow);
			break;
		case 4:
			SetColor(R.color.fs);
			break;
		default:
			SetColor(R.color.white);
			break;
		}
    	} 
    	}).show();//��ʾ�Ի��� 
    }
    
    /**
     * ѡ������
     */
    public void selectBright()
    {
    	final String[] items = {"100%", "75%", "50%","25%","10%"}; 
    	new AlertDialog.Builder(this) 
    	.setTitle("ѡ������") 
    	.setSingleChoiceItems(items, li, new DialogInterface.OnClickListener() { //�˴�����Ϊѡ����±꣬��0��ʼ�� ��ʾĬ�����ѡ�� 
    	public void onClick(DialogInterface dialog, int item) { 
    	Toast.makeText(getApplicationContext(), items[item],Toast.LENGTH_SHORT).show(); 
    	li=item;
    	switch (item) {
		case 0:
			SetBright(1.0F);
			break;
		case 1:
			SetBright(0.75F);
			break;
		case 2:
			SetBright(0.5F);
			break;
		case 3:
			SetBright(0.25F);
			break;
		case 4:
			SetBright(0.1F);
			break;
		default:
			SetBright(1.0F);
			break;
		}	
    	dialog.cancel(); 
    	} 
    	}).show();//��ʾ�Ի��� 
    }

    
    /**
     * ȫ������
     */
    private void HideStatusBase()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
		Window myWindow=this.getWindow();
		myWindow.setFlags(flag,flag);
	}
    
    
    /**
     * ������Ļ��ɫ
     * @param color_M
     */
    private void SetColor(int color_1)
    {
    	myColor = getBaseContext().getResources();
		Drawable color_M = myColor.getDrawable(color_1);
    	mylayout.setBackgroundDrawable(color_M);
        //mylayout.setBackgroundColor(Color.argb(255, 255, 255, 255));
    }
    
    /**
     * ������Ļ����
     * @param light
     */
    private void SetBright(float light)
    {
    	WindowManager.LayoutParams lp=getWindow().getAttributes();
    	lp.screenBrightness=light;
    	getWindow().setAttributes(lp);
    	
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
		
		}
		return false;
	}
}