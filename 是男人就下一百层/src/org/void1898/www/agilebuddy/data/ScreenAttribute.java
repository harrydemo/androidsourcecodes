package org.void1898.www.agilebuddy.data;

/**
 * 
 * @author void1898@gmail.com
 * 
 * @version 1.2.3
 * 
 */
public class ScreenAttribute {
	// ��СX����
	public int minX;
	// ��СY����
	public int minY;
	// ���X����
	public int maxX;
	// ���Y����
	public int maxY;

	public ScreenAttribute(int minX, int minY, int maxX, int maxY) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.minX = minX;
		this.minY = minY;
	}
}
