/**
 * fengsheng.yang@live.cn
 * ������������ʹ�ã��������ת�أ�����ϣ�������ҵĳɹ�������ϱ��˲���
 * http://huojv.blog.hexun.com(��ʱ��ȥ������Ҳ�������Ҫ�Ķ���)
 * ���������ޣ��һ����ܺõģ�лл���!
 * 2008-12-25
 */
package com.google.android.WaterWave;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class WaterWave extends Activity 
{
	DrawWaterWave	m_DrawWaterWave;
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        m_DrawWaterWave = new DrawWaterWave(this);
        setContentView(m_DrawWaterWave);
    }
    
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		m_DrawWaterWave.key();
		return super.onKeyDown(keyCode, event);
	}
}