package com.xiexj.ebook;

/**
 * 一元方程式类
 * @author xiexj
 *
 */
public class MNLine {

	private float a;
	
	private float b;
	
	private MNLine(){
		
	}
	
	/**
	 * 2点重新构造这条直线
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
	 * 1点不改变斜率移动直线
	 * @param x
	 * @param y
	 */
	public void change(float x,float y){
		b = y-a*x;
	}
	
	/**
	 * 初始化获取这条直线
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
	 * 根据X获取Y
	 * @param x
	 * @return
	 */
	public float getYbyX(float x){
		return a*x+b;
	}
	
	/**
	 * 根据Y计算X
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
	 * 获取这条直线的中垂线
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
	 * 返回跟另一条直线的交叉点，float[0]=x,float[1]=y
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
