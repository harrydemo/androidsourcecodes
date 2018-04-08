package cn.edu.xtu.tilepuzzle;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
/**
 * ���ʶ�����
 * */
public class ClassPaint {
	/**
	 *  Paint���󣺱�������  
	 * */
	public static Paint paintBackgrount = null;
	/**
	 *  Paint����ΪͼƬ�������б�ʶ�Ļ���  
	 * */
	public static Paint paintAddStr = null;
	/**
	 *  Paint���󣺸�����������  
	 * */
	public Paint paintHighBGColor = null;
	/**
	 *  Paint���󣺸������廭��  
	 * */
	public static Paint paintHighColor = null;
	/**
	 *  Paint���󣺰���ɫ���廭��  
	 * */
	public static Paint paintLowColor = null;
	
	/**
	 *  Paint���󣺴󡢴֡���ɫ 
	 * */
	public  Paint paintBigBoldDefault = null;
	
	/**
	 * Paint���󣺴󡢺�ɫ 
	 */
	public  Paint paintBigDefault = null;
	/**
	 * Paint���󣺴󡢺�ɫ 
	*/
	public Paint paintBigRed = null;
	/**
	 * Paint�����С��֡���ɫ 
	 */
	public  Paint paintNormalBoldDefault = null; 
	/**
	 * Paint�����С��֡���ɫ 
	 */
	public  Paint paintNormalBoldRed = null;	
	/**
	 * Paint�����С���ɫ 
	 */
	public  Paint paintNormalDefault = null; 
	/**
	 * Paint�����С���ɫ 
	 */
	public Paint paintNormalRed = null;
	/**
	 * Paint����С���֡���ɫ 
	 */
	public  Paint paintMinBoldDefault = null;
	/**
	 * Paint����С���֡���ɫ 
	 */
	public  Paint paintMinBoldRed = null;
	
	/**
	 * Paint����С����ɫ 
	 */
	public  Paint paintMinDefault = null;
	/**
	 * Paint����С����ɫ 
	 */
	public  Paint paintMinRed = null;
	
	//��ʼ������
	public void initPaint() {
		System.out.println("ClassPaint����ʼ������");
		
		paintAddStr=new Paint();
		paintAddStr.setTextSize(24);
		paintAddStr.setARGB(255,100, 100, 245);
		paintAddStr.setStyle(Paint.Style.FILL);
		paintAddStr.setStrokeWidth(1);
		
		paintBackgrount=new Paint();
		paintBackgrount.setARGB(255, 255, 255, 190);
		paintBackgrount.setStyle(Paint.Style.FILL);
		
		paintHighBGColor=new Paint();
		paintHighBGColor.setColor(0x00CCCCCC);
		paintHighBGColor.setStyle(Paint.Style.FILL);
		
		paintHighColor=new Paint();
		paintHighColor.setColor(0x00FF0000);
		paintHighColor.setStyle(Paint.Style.FILL);
		paintHighColor.setAntiAlias(true);/* ȡ����� */
		paintHighColor.setStrokeWidth(1);
		paintHighColor.setTextSize(12);
		
		paintLowColor=new Paint();
		paintLowColor.setColor(0x000000FF);
		paintLowColor.setStyle(Paint.Style.FILL);
		paintLowColor.setAntiAlias(true);/* ȡ����� */
		paintLowColor.setStrokeWidth(1);
		paintLowColor.setTextSize(12);			
		
		paintBigDefault = new Paint();
		paintBigDefault.setAntiAlias(true);/* ȡ����� */
		paintBigDefault.setStyle(Paint.Style.FILL);
		paintBigDefault.setColor(Color.BLACK);//��ɫ
		paintBigDefault.setStrokeWidth(1);//���
		paintBigDefault.setTextSize(24);//�����С
		
		paintBigRed=new Paint();
		paintBigRed.setAntiAlias(true);/* ȡ����� */
		paintBigRed.setStyle(Paint.Style.FILL);
		paintBigRed.setColor(Color.RED);//��ɫ
		paintBigRed.setStrokeWidth(1);//���
		paintBigRed.setTextSize(24);//�����С	
		paintBigRed.setTextAlign(Align.CENTER); 
		
		paintNormalDefault = new Paint();
		paintNormalDefault.setAntiAlias(true);
		paintNormalDefault.setStyle(Paint.Style.FILL);
		paintNormalDefault.setColor(Color.BLACK);
		paintNormalDefault.setStrokeWidth(1);
		paintNormalDefault.setTextSize(12);
		
		paintNormalRed=paintNormalDefault;
		paintNormalRed.setColor(Color.BLACK);
		
		paintMinDefault=new Paint();	
		paintMinDefault.setAntiAlias(true);
		paintMinDefault.setStyle(Paint.Style.FILL);
		paintMinDefault.setColor(Color.BLACK);
		paintMinDefault.setStrokeWidth(1);
		paintMinDefault.setTextSize(1);
		
		paintMinRed=paintMinDefault;
		paintMinRed.setColor(Color.BLACK);
		System.out.println("ClassPaint�����ʳ�ʼ�����");
		
	}
}
