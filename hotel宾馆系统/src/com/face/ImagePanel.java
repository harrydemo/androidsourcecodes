/*
 * 这是一个可以动态加载一个图片做背景的JPanel
 */

package com.face;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

//Download by http://www.codefans.net

public class ImagePanel extends JPanel
{

	Image im;

	// 构造函数去指定该Panel大小
	public ImagePanel(Image im)
	{
		this.im = im;
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setSize(width, height);
	}

	public void paintComponent(Graphics g)
	{
		// 清屏
		super.paintComponents(g);
		g.drawImage(im, 0, 0, this.getWidth(), this.getHeight(), this);

	}

}
