package com.xiexj.ebook;

/**
 * һԪ����ʽ��
 * @author xiexj
 *
 */
public class MNLine {

	private float a;
	
	private float b;
	
	private MNLine(){
		
	}
	
	/**
	 * 2�����¹�������ֱ��
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void change(float x1,float y1,float x2,float y2){
		a = (y2-y1)/(x2-x1);
		b = (x2*y1-y2*x1)/(x2-x1);
	}
	
	/**
	 * 1�㲻�ı�б���ƶ�ֱ��
	 * @param x
	 * @param y
	 */
	public void change(float x,float y){
		b = y-a*x;
	}
	
	/**
	 * ��ʼ����ȡ����ֱ��
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static MNLine initLine(float x1,float y1,float x2,float y2){
		MNLine mnLine = new MNLine();
		mnLine.change(x1, y1, x2, y2);
		return mnLine;
	}
	
	/**
	 * ����X��ȡY
	 * @param x
	 * @return
	 */
	public float getYbyX(float x){
		return a*x+b;
	}
	
	/**
	 * ����Y����X
	 * @param y
	 * @return
	 */
	public float getXbyY(float y){
		return (y-b)/a;
	}
	
	public float getA(){
		return a;
	}
	
	public float getB(){
		return b;
	}
	
	/**
	 * ��ȡ����ֱ�ߵ��д���
	 * @param bx
	 * @param by
	 * @return
	 */
	public MNLine getPBLine(float bx,float by){
		MNLine line = new MNLine();
		line.a = -1/this.a;
		line.b = by-line.a*bx;
		return line;
	}
	
	/**
	 * ���ظ���һ��ֱ�ߵĽ���㣬float[0]=x,float[1]=y
	 * @param line
	 * @return
	 */
	public float[] getCross(MNLine line){
		float[] xAndY = new float[2];
		xAndY[0] = (b-line.b)/(line.a-a);
		xAndY[1] = getYbyX(xAndY[0]);
		return xAndY;
	}
}
