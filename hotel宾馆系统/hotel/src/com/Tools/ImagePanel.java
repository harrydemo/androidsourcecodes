/*
 * ����һ�����Զ�̬����һ��ͼƬ��������JPanel
 */


package com.Tools;


import  javax.swing.*;
import  java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;


public class ImagePanel extends JPanel{

	Image im;
	
	
	
	//���캯��ȥָ����Panel��С
	public  ImagePanel(Image im)
	{
		this.im=im;
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int height=Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setSize(width,height);	
	}
	
	public void paintComponent(Graphics g)
	{
		//����
		super.paintComponents(g);
		g.drawImage(im, 0, 0, this.getWidth(), this.getHeight(), this);
		
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
