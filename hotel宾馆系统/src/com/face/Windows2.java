/**
 * 这是用于员工操作的界面
 */


package com.face;
import com.Tools.*;

import  javax.swing.*;
import javax.swing.Timer;

import  java.awt.*;
import java.awt.event.*;

import javax.imageio.*;

import java.io.*;
import java.util.*;



public class Windows2  extends JFrame implements ActionListener,MouseListener{
	
	//定义组件
	Image win2_titleIcon,timebg;
	JMenuBar win2_jmb;
	//定义一级菜单
	JMenu win2_jm1,win2_jm2;
	//定义二级菜单
	JMenuItem win2_jmi1_1,win2_jmi1_2;
	//图标
	ImageIcon  win2_jmm1, win2_jmm2;//二级菜单的图标
	
	//工具栏
	JToolBar win2_jtb;
	JButton win2_jb1,win2_jb2,win2_jb3;
	//定义需要的五个面板
	JPanel p1,p2,p3,p4,p5;
	//javax.swing包中的TImer可以定时的触发Action事件
	javax.swing.Timer  time;
	JLabel  timeNow;
	
	ImagePanel p1_imagePanel;
	
	JLabel   p1_1, p1_2, p1_3, p1_4, p1_5, p1_6, p1_7, p1_8,p1_9;
	JLabel p2_1,p2_2;
	
	//窗口拆分时用
	JSplitPane jsp1;
	
	CardLayout cardP3;
	
	
	
	
	//初始化菜单函数
	public void win2_initMenu()
	{
		//创建图标
		win2_jmm1=new ImageIcon("image/jmm1_3.jpg");
		win2_jmm2=new ImageIcon("image/jmm1_5.jpg");
		//创建二级菜单
		win2_jmi1_1=new JMenuItem("修改密码",win2_jmm1);
		win2_jmi1_2=new JMenuItem("退出",win2_jmm2);
		//创建一级菜单
		win2_jm1=new JMenu("系统");
		win2_jm1.setFont(MyTools.f1);
		win2_jm1.add(win2_jmi1_1);
		win2_jm1.add(win2_jmi1_2);
		
		win2_jm2=new JMenu("帮助");
		win2_jm2.setFont(MyTools.f1);
		
		//添加到面板
		win2_jmb=new JMenuBar();
		win2_jmb.add(win2_jm1);
		win2_jmb.add(win2_jm2);
		//把JMenuBar添加到JFrame
		this.setJMenuBar(win2_jmb);
		
		//win2_jmi1_1.setFont(MyTools.f2);
		
	}
	
	
	//初始化工具栏
	public void win2_initToolBar()
	{
		
		win2_jtb=new JToolBar();
		win2_jtb.setFloatable(false);
		win2_jb1=new JButton(new ImageIcon("image/jb3.jpg"));
		win2_jb2=new JButton(new ImageIcon("image/jb5.jpg"));
		win2_jb3=new JButton(new ImageIcon("image/jb10.jpg"));
		
		win2_jtb.add(win2_jb1);
		win2_jtb.add(win2_jb2);
		win2_jtb.add(win2_jb3);
		
	}

	
	//初始化中间区域Panel
	public void win2_initAllPanels()
	{
		p1=new JPanel(new BorderLayout());
		Image p1_bg=null;
		try {
			p1_bg = ImageIO.read(new  File("image/login5.jpg") );
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
       Cursor MyCursor=new Cursor(Cursor.HAND_CURSOR);//创建一个手形鼠标
		
		
		this.p1_imagePanel=new ImagePanel(p1_bg);
		this.p1_imagePanel.setLayout(new GridLayout(3,3));//网格布局八行一列
		//p1_imagePanel.add(new JLabel(new ImageIcon("image/room1.png")));
	//p1的第一个Label
		p1_1=new JLabel("101",new ImageIcon("image/room1.png"),0);
		p1_1.setFont(MyTools.f3);//设置显示字体		
		p1_1.setCursor(MyCursor);//添加手形鼠标
		p1_1.setEnabled(false);//让该按钮不可用
		p1_1.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_1);
		//p1的第二个Label
		p1_2=new JLabel("102",new ImageIcon("image/room1.png"),0);
		p1_2.setFont(MyTools.f3);	
		p1_2.setCursor(MyCursor);//添加手形鼠标
		p1_2.setEnabled(false);//让该按钮不可用
		p1_2.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_2);
		//p1的第二个Label
		p1_3=new JLabel("103",new ImageIcon("image/room1.png"),0);
		p1_3.setFont(MyTools.f3);
		p1_3.setCursor(MyCursor);//添加手形鼠标
		p1_3.setEnabled(false);//让该按钮不可用
		p1_3.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_3);
		//p1的第二个Label
		p1_4=new JLabel("201",new ImageIcon("image/room1.png"),0);
		p1_4.setFont(MyTools.f3);
		p1_4.setCursor(MyCursor);//添加手形鼠标
		p1_4.setEnabled(false);//让该按钮不可用
		p1_4.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_4);
		//p1的第二个Label
		p1_5=new JLabel("202",new ImageIcon("image/room1.png"),0);
		p1_5.setFont(MyTools.f3);
		p1_5.setCursor(MyCursor);//添加手形鼠标
		p1_5.setEnabled(false);//让该按钮不可用
		p1_5.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_5);
		//p1的第二个Label
		p1_6=new JLabel("203",new ImageIcon("image/room1.png"),0);
		p1_6.setFont(MyTools.f3);
		p1_6.setCursor(MyCursor);//添加手形鼠标
		p1_6.setEnabled(false);//让该按钮不可用
		p1_6.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_6);
		//p1的第七个Label
		p1_7=new JLabel("301",new ImageIcon("image/room1.png"),0);
		p1_7.setFont(MyTools.f3);
		p1_7.setCursor(MyCursor);//添加手形鼠标
		p1_7.setEnabled(false);//让该按钮不可用
		p1_7.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_7);
		//p1的第二个Label
		p1_8=new JLabel("302",new ImageIcon("image/room1.png"),0);
		p1_8.setFont(MyTools.f3);
		p1_8.setCursor(MyCursor);//添加手形鼠标
		p1_8.setEnabled(false);//让该按钮不可用
		p1_8.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_8);
		//p1的第二个Label
		p1_9=new JLabel("303",new ImageIcon("image/room1.png"),0);
		p1_9.setFont(MyTools.f3);
		p1_9.setCursor(MyCursor);//添加手形鼠标
		p1_9.setEnabled(false);//让该按钮不可用
		p1_9.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_9);
		
		

		
		p1.add(this.p1_imagePanel);
		
		//处理p2,p3,p4 面板
		p4=new JPanel(new BorderLayout());
		p2=new JPanel(new CardLayout());//卡片布局
		//在p2中添加卡片
		p2_1=new JLabel(new ImageIcon("image/p2_left.jpg"));
		p2_2=new JLabel(new ImageIcon("image/p2_right.jpg"));
		p2.add(p2_1,"0");//吧卡片加入面板
		p2.add(p2_2,"1");
		//在p3中添加卡片
		this.cardP3=new CardLayout();
		p3=new JPanel(this.cardP3);//卡片布局
		Image zhu_image=null;
		try {
			zhu_image = ImageIO.read(new File("image/zhu_bg.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ImagePanel ip=new ImagePanel(zhu_image);
		p3.add(ip,"0");
		//对P3做添加，添加几个JLabel
		//JLabel  renshi=new JLabel(new ImageIcon("image/Person.jpg"));
		//创建WmpImfo对象
		EmpInfo  p3EmpInfo=new EmpInfo();
		p3.add(p3EmpInfo,"1");
		JLabel denglu=new JLabel(new ImageIcon("image/denglu.jpg"));
		p3.add(denglu,"2");
		//吧p2,p3加入到p4
		p4.add(p2,"West");
		p4.add(p3,"Center");
		
		//做一个拆分窗口，分别存放P1和P4
		jsp1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,p1,p4);
		//指定左边的面板站多大
		jsp1.setDividerLocation(935);
		//把边界线设为0
		jsp1.setDividerSize(0);		
		
		
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Windows2 w2=new Windows2();
	}
	
	
	
	public  Windows2()
	{
		try {
			win2_titleIcon=ImageIO.read(new File("image/jiubei.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//调用初始化菜单函数
		this.win2_initMenu();	
		//调用初始工具栏
		this.win2_initToolBar();
		this.win2_initAllPanels();
		
		
		//处理p5面板
		p5=new JPanel(new BorderLayout());
		time=new javax.swing.Timer(1000,this);//每隔一秒触发ActionEvent
		//启动t
		time.start();		
		timeNow=new JLabel(Calendar.getInstance().getTime().toLocaleString());//改变了时间的实现方式
		//时间面板背景的设置
		try {
			timebg=ImageIO.read(new File("image/jp1_bg.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImagePanel ip1=new ImagePanel(timebg);		
		ip1.setLayout(new BorderLayout());
		ip1.add(timeNow,"East");
		p5.add(ip1);
		//从JFrame中取得容器
		Container win2_ct=this.getContentPane();	
		
		//将JPanel放入容器
		win2_ct.add(win2_jtb,"North");
		win2_ct.add(jsp1,"Center");
		win2_ct.add(p5,"South");
		
		
		
		
		
		
		
		
		
		
		
		//得到屏幕的大小
		int windows2_width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int windows2_height=Toolkit.getDefaultToolkit().getScreenSize().height;
		//窗口关闭时，退出系统
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置窗口的图片（左上角）
		this.setIconImage(win2_titleIcon);
		this.setTitle("希尔顿酒店住房管理系统");
		this.setSize(windows2_width,windows2_height-45);
		this.setVisible(true);		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.timeNow.setText("当前时间："+Calendar.getInstance().getTime().toLocaleString()+"      ");//改变时间显示格式
	}


	//鼠标监听

	public void mouseClicked(MouseEvent argp3) {
		// TODO Auto-generated method stub
		//判断用户点击那个操作JLabel
		if(argp3.getSource()==this.p1_1)
		{
			this.cardP3.show(p3, "1");
		}else if(argp3.getSource()==this.p1_2)
		{
			this.cardP3.show(p3,"2");
		}
		
	}

	
	public void mouseEntered(MouseEvent p1) {
		// TODO Auto-generated method stub
		//如果用户选中某个JLabel,则高亮
		if(p1.getSource()==this.p1_1)
		{
			this.p1_1.setEnabled(true);
		}
		else if(p1.getSource()==this.p1_2)
		{
			this.p1_2.setEnabled(true);
		}
		else if(p1.getSource()==this.p1_3)
		{
			this.p1_3.setEnabled(true);
		}
		else if(p1.getSource()==this.p1_4)
		{
			this.p1_4.setEnabled(true);
		}
		else if(p1.getSource()==this.p1_5)
		{
			this.p1_5.setEnabled(true);
		}
	}

	public void mouseExited(MouseEvent p1) {
		// TODO Auto-generated method stub
		//如果用户选中某个JLabel,则禁用
		if(p1.getSource()==this.p1_1)
		{
			this.p1_1.setEnabled(false);
		}
		else if(p1.getSource()==this.p1_2)
		{
			this.p1_2.setEnabled(false);
		}
		else if(p1.getSource()==this.p1_3)
		{
			this.p1_3.setEnabled(false);
		}
		else if(p1.getSource()==this.p1_4)
		{
			this.p1_4.setEnabled(false);
		}
		else if(p1.getSource()==this.p1_5)
		{
			this.p1_5.setEnabled(false);
		}
	}

	
	public void mousePressed(MouseEvent p1) {
		// TODO Auto-generated method stub
		
	}


	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
