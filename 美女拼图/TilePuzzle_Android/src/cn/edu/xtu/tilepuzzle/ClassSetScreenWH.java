package cn.edu.xtu.tilepuzzle;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

/**
 * ������Ļ����
 * ��ȡ��Ļ�ĸ����
 * */
public class ClassSetScreenWH {
	/** ���ڵĿ��*/
	private int screenWidth = 0;
	/**  ���ڵĸ߶�*/
	private int screenHeight = 0;
	public ClassSetScreenWH(Activity activity){
		
		// ����ʾ����ı�����
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ʾϵͳ�ı�����
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// ����DisplayMetrics����
		DisplayMetrics dm = new DisplayMetrics();

		// ȡ�ô�������
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		// ���ڵĿ��
		screenWidth = dm.widthPixels;
		// ���ڵĸ߶�
		screenHeight = dm.heightPixels;		
    	
	/*	// ���ڵĿ��
		screenWidth = 320;
		// ���ڵĸ߶�
		screenHeight = 480;
		*/
		//System.out.println("��Ļ��ȣ�" + screenWidth + "\n��Ļ�߶ȣ�" + screenHeight);
	}
	/**
	 * ��ȡ��Ļ�Ŀ�
	 * */
	public int getScreenWidth() {
		return screenWidth;
	}
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	/**
	 * ��ȡ��Ļ�ĸ�
	 * */
	public int getScreenHeight() {
		return screenHeight;
	}
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	
}
