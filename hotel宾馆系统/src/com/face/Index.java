package com.face;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Index extends JWindow{
	paint p;
	
	public static void main(String[] args) {
		Index index = new Index();
	}
	public Index()
	{
		p = new paint();
		this.add(p);
		
		this.setSize(400,250);
		//确定初始位置
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(width/2-200,height/2-150);
		this.setVisible(true);
	}

}

//开发闪屏类
class  paint extends JPanel implements Runnable
{
	Thread t;
	int x = 10;
	int i = 0,j = 40,u = 10;
	String gg[] = {"系","统","正","在","加","载"};
	int k = 0,tt = 0;
	String shi[] = {"餐","饮","系","统","唐","亮"};
	Font f = new Font("隶书",Font.PLAIN,18);
	
	boolean ifok = true;
	int width = 180;
	int height = 0;
	int dian = 0;
	
	paint()
	{
		t = new Thread(this);
		t.start();
		
	}
	
	public void run()
	{
		while(true)
		{
			if(x<=380)  
				repaint();
			try
			{
				Thread.sleep(70);
				i++;
				j = j-6;
				u = u+10;
				
				if(tt == 3) width = width=20;
				if(i == 4)
				{
					tt++;
					if(ifok && x>120+k*20) k++;
					if(k >=gg.length-1) ifok = false;
					x = x+10;
					i = 0;
					j = 40;
					u = 10;
					dian++;
					if(dian > 3) dian = 0;
				}
			}catch(InterruptedException e)
			{
				System.out.println("线程中断");
			}
		}
	}
	
	public void painComponent(Graphics g)
	{
		Image image;
		image = Toolkit.getDefaultToolkit().getImage("image\\");//这里要在image中加入一张图片
		g.drawImage(image, 0, 0,this.getWidth(), 200, this);
		
		int r = (int)(Math.random()*255);
		int b = (int)(Math.random()*255);
		int y = (int)(Math.random()*255);
		
		g.setColor(new Color(255,250,250));
		g.fillRect(x, 210, 390-x, 30);
		g.setColor(new Color(255,250,250));
		if(i > 1) g.fillRect(x, 250-(j+20)/2, 10 ,j+20);
		if(i > 25) g.setColor(new Color(r,b,y));
		else g.setColor(new Color(123,194,252));
		if(i > 1) g.fillRect(x, 250-(j+20)/2, 10 ,j+20);
		if(i > 25) g.setColor(new Color(r,b,y));
		else g.setColor(new Color(123,194,252));
		
		if(x < 120)
		{
			for(int i = 0;i < gg.length;i++)
			{
				g.setColor(new Color(0,0,0));
				g.drawString(gg[i], 120+i*20, 230);
			}
			for(int i = 0;i < dian;i++)
			{
				g.setColor(new Color(0,0,0));
				g.drawString("*", 300+i*10, 235);
			}
			g.drawString("*", 300+10*dian, 235);
			
		}
		else
		{
			g.setColor(new Color(23,23,230));
			g.drawString(gg[k], 120+k*20, 230);
			
			for(int i = 0;i < gg.length;i++)
			{
				g.setColor(new Color(0,0,0));
				g.drawString(gg[i], 120+i*20, 230);
			}
			if(x > 30+dian*10) g.setColor(new Color(23,23,230));
			for(int i = 0;i < dian;i++)
			{
				g.drawString("*", 300+i*10, 235);
			}
			g.drawString("*", 300+10*dian, 235);
		}
		
		//逐字写诗
		if(tt<3)
		{
			for(int rr = 0;rr < tt;rr++)
			{
				g.setColor(new Color(r,b,y));
				g.drawString(shi[i], 180, 60+rr+20);
			}
			g.drawString(shi[i], 180, 60+tt*20);
		}
		
		if(tt >= 3 && tt < 7)
		{
			g.setColor(new Color(230,0,0));
			for(int rr = 0;rr<3;rr++) g.drawString(shi[rr], 180, 60+rr*20);
			for(int rr = 3;rr<=7;rr++) g.drawString(shi[rr], 150, rr*20-20);
			g.setColor(new Color(r,b,y));
			if(tt < 13)
			{
				for(int rr = 8;rr <= tt;rr++)
				{
					g.drawString(shi[rr], 120, rr);//没写完
				}
			}
			if(tt >= 13)
			{
				for(int rr = 8;rr < 13;rr++)
				{
					g.drawString(shi[rr], 120, rr);//没写完
				}
			}
		}
		
		if(tt >= 13 && tt < 18)
		{
			g.setColor(new Color(230,0,0));
			for(int rr = 0;rr<3;rr++) g.drawString(shi[rr], 180, 60+rr*20);
			for(int rr = 3;rr<=7;rr++) g.drawString(shi[rr], 150, rr*20-20);
			for(int rr = 8;rr<=13;rr++) g.drawString(shi[rr], 120, rr*20-120);
			g.setColor(new Color(r,b,y));
			if(tt < 18)
			{
				for(int rr = 13;rr <= tt;rr++)
				{
					g.drawString(shi[rr], 90, rr);//没写完
				}
			}
			if(tt >= 18)
			{
				for(int rr = 13;rr < 13;rr++)
				{
					g.drawString(shi[rr], 90, rr);//没写完
				}
			}			
		}
		
		if(tt >= 18 && tt < 23)
		{
			g.setColor(new Color(230,0,0));
			for(int rr = 0;rr<3;rr++) g.drawString(shi[rr], 180, 60+rr*20);
			for(int rr = 3;rr<=7;rr++) g.drawString(shi[rr], 150, rr*20-20);
			for(int rr = 8;rr<=13;rr++) g.drawString(shi[rr], 120, rr*20-120);
			for(int rr = 14;rr<=18;rr++) g.drawString(shi[rr], 90, rr*20-220);
			g.setColor(new Color(r,b,y));
			if(tt < 23)
			{
				for(int rr = 18;rr <= tt;rr++)
				{
					g.drawString(shi[rr], 60, rr);//没写完
				}
			}
			if(tt >= 23)
			{
				for(int rr = 18;rr < 23;rr++)
				{
					g.drawString(shi[rr], 60, rr);//没写完
				}
			}			
		}
		if(tt >= 23 && tt < 30)
		{
			g.setColor(new Color(230,0,0));
			for(int rr = 0;rr<3;rr++) g.drawString(shi[rr], 180, 60+rr*20);
			for(int rr = 3;rr<=7;rr++) g.drawString(shi[rr], 150, rr*20-20);
			for(int rr = 8;rr<=13;rr++) g.drawString(shi[rr], 120, rr*20-120);
			for(int rr = 14;rr<=18;rr++) g.drawString(shi[rr], 90, rr*20-220);
			for(int rr = 19;rr<=23;rr++) g.drawString(shi[rr], 60, rr*20-320);
			g.setColor(new Color(r,b,y));
			if(tt < 30)
			{
				for(int rr = 23;rr <= tt;rr++)
				{
					g.drawString(shi[rr], 30, rr);//没写完
				}
			}
			if(tt >= 30)
			{
				for(int rr = 23;rr < 30;rr++)
				{
					g.drawString(shi[rr], 30, rr);//没写完
				}
			}	
		}
		
		if(tt >= 30)
		{
			g.setColor(new Color(230,0,0));
			for(int rr = 0;rr<3;rr++) g.drawString(shi[rr], 180, 60+rr*20);
			for(int rr = 3;rr<=7;rr++) g.drawString(shi[rr], 150, rr*20-20);
			for(int rr = 8;rr<=13;rr++) g.drawString(shi[rr], 120, rr*20-120);
			for(int rr = 14;rr<=18;rr++) g.drawString(shi[rr], 90, rr*20-220);
			for(int rr = 19;rr<=23;rr++) g.drawString(shi[rr], 60, rr*20-320);
			for(int rr = 24;rr<=30;rr++) g.drawString(shi[rr], 30, rr*20-400);
		}	
	}	
}
