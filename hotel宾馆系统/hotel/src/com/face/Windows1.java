/*
 * 完成界面的顺序，从上到下，从左到右
 */


package com.face;
import com.Model.EmpModel;
import com.Tools.*;
import com.client.ClieMain;
import com.room.RoomMain;

import  javax.swing.*;

import  java.awt.*;
import java.awt.event.*;

import javax.imageio.*;
import java.io.*;
import java.util.*;

public class Windows1  extends JFrame implements ActionListener,MouseListener{

	public static JFrame w1;
	//定义需要的组件
	Image	 titleIcon,timebg;
	JMenuBar jmb;
	//一级菜单
	JMenu jm1,jm2,jm3,jm4,jm5,jm6;
	//二级菜单
	JMenuItem jmm1,jmm2,jmm3,jmm4,jmm5;
	JMenuItem jmm21,jmm22,jmm23,jmm24,jmm25;
	JMenuItem jmm31,jmm32;
	
	//图标类
	ImageIcon jmm1_1,jmm1_2,jmm1_3,jmm1_4,jmm1_5;
	ImageIcon jmm2_1,jmm2_2,jmm2_3,jmm2_4,jmm2_5;
	
	
	//工具栏
	JToolBar jtb;
	JButton  jb1,jb2,jb3,jb4,jb5,jb6,jb7,jb8,jb9,jb10;
	
	//定义需要的五个面板
	JPanel p1,p2,p3,p4,p5;
	//显示当前时间
	JLabel  timeNow;
	JLabel   p1_1, p1_2, p1_3, p1_4, p1_5, p1_6, p1_7, p1_8;
	//给P2面板定义需要的JLabel
	JLabel p2_1,p2_2;
	//javax.swing包中的TImer可以定时的触发Action事件
	javax.swing.Timer  t;
	
	ImagePanel p1_imagePanel;
	
	//窗口拆分时用
	JSplitPane jsp1;
	
	CardLayout cardP3;
	
	
	
	
	public static void main(String[] args) {	
		
		Windows1  w1=new Windows1();
	}
	
	
	
	//初始化菜单
	public void initMenu()
	{
		//创建图标
		jmm1_1=new ImageIcon("image/jmm1_1.jpg");
		jmm1_2=new ImageIcon("image/jmm1_2.jpg");
		jmm1_3=new ImageIcon("image/jmm1_3.jpg");
		//jmm1_4=new ImageIcon("image/jmm1_4.jpg");
		jmm1_5=new ImageIcon("image/jmm1_5.jpg");
		//创建一级菜单
		jm1=new JMenu("系统管理");
		jm1.setFont(MyTools.f1);//设置字体
		//系统管理 的创建二级菜单
		jmm1=new JMenuItem("切换用户",jmm1_1);
	//	jmm1.addMouseListener(this);
		jmm1.addActionListener(this);
		//jmm1.setFont(MyTools.f2);
		jmm2=new JMenuItem("切换到收款界面",jmm1_2);
		jmm2.addActionListener(this);
	//	jmm2.addMouseListener(this);
		jmm3=new JMenuItem("登陆管理",jmm1_3);	
		jmm3.addActionListener(this);
		jmm5=new JMenuItem("退出",jmm1_5);
		
		//加入
		jm1.add(jmm1);
		jm1.add(jmm2);
		jm1.add(jmm3);
		//jm1.add(jmm4);
		jm1.add(jmm5);
		
		
		
		
		jmm2_1=new ImageIcon("image/jmm2_1.png");
		jmm2_2=new ImageIcon("image/jmm2_2.jpg");
		jmm2_3=new ImageIcon("image/jmm2_3.jpg");
		jm2=new JMenu("人事管理");
		jm2.setFont(MyTools.f1);
		//人事管理的二级界面
		jmm21=new JMenuItem("查询",jmm2_1);
		jmm21.addActionListener(this);
		jmm22=new JMenuItem("添加");
		jmm22.addActionListener(this);
//		jmm22.addMouseListener(this);
		jmm23=new JMenuItem("修改");
		jmm23.addActionListener(this);
		jm2.add(jmm21);
		jm2.add(jmm22);
		jm2.add(jmm23);
		
		//jm3=new JMenu("菜单服务管理");
		//jm3.setFont(MyTools.f1);
		jm3=new JMenu("报表统计");
		jm3.setFont(MyTools.f1);
		jmm31=new JMenuItem("柱形图");
		jmm32=new JMenuItem("线性图");
		jm3.add(jmm31);
		jm3.add(jmm32);
		
		jm5=new JMenu("房间定价");
		jm5.addActionListener(this);
		jm5.setFont(MyTools.f1);
		
		jm6=new JMenu("帮助");
		jm6.addActionListener(this);
		jm6.setFont(MyTools.f1);
		
		//把一级菜单加入到JMenuBar
		jmb=new JMenuBar();
		jmb.add(jm1);
		jmb.add(jm2);
		//jmb.add(jm3);
		jmb.add(jm3);
		jmb.add(jm5);
		jmb.add(jm6);		
		
		//把JMenuBar添加到JFrame
		this.setJMenuBar(jmb);
	}
	
	//初始化工具栏
	public void initToolBar()
	{
		jtb=new JToolBar();
		//设置工具栏不可以浮动
		jtb.setFloatable(false);
		jb1=new JButton(new ImageIcon("image/jb1.jpg"));
		jb2=new JButton(new ImageIcon("image/jb2.jpg"));
		jb3=new JButton(new ImageIcon("image/jb3.jpg"));
		jb4=new JButton(new ImageIcon("image/jb4.jpg"));
		jb5=new JButton(new ImageIcon("image/jb5.jpg"));
		jb6=new JButton(new ImageIcon("image/jb6.jpg"));
		jb7=new JButton(new ImageIcon("image/jb7.jpg"));
		jb8=new JButton(new ImageIcon("image/jb8.jpg"));
		jb9=new JButton(new ImageIcon("image/jb9.jpg"));
		jb10=new JButton(new ImageIcon("image/jb10.jpg"));
		
		jtb.add(jb1);
		jtb.add(jb2);
		jtb.add(jb3);
		jtb.add(jb4);
		jtb.add(jb5);
		jtb.add(jb6);
		jtb.add(jb7);
		jtb.add(jb8);
		jtb.add(jb9);
		jtb.add(jb10);
	}
	
	
	//初始化中间区域Panel
	public void initAllPanels()
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
		this.p1_imagePanel.setLayout(new GridLayout(8,1));//网格布局八行一列
		p1_imagePanel.add(new JLabel(new ImageIcon("image/login1.png")));
		//p1的第一个Label
		p1_1=new JLabel("人  事  管  理",new ImageIcon("image/p1_1.jpg"),0);
		p1_1.setFont(MyTools.f3);//设置显示字体		
		p1_1.setCursor(MyCursor);//添加手形鼠标
		p1_1.setEnabled(false);//让该按钮不可用
		p1_1.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_1);
		//p1的第二个Label
		p1_2=new JLabel("登  录  管  理",new ImageIcon("image/p1_2.jpg"),0);
		p1_2.setFont(MyTools.f3);	
		p1_2.setCursor(MyCursor);//添加手形鼠标
		p1_2.setEnabled(false);//让该按钮不可用
		p1_2.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_2);
		
		//p1的第二个Label
		p1_4=new JLabel("顾客信息管理",new ImageIcon("image/p1_4.jpg"),0);
		p1_4.setFont(MyTools.f3);
		p1_4.setCursor(MyCursor);//添加手形鼠标
		p1_4.setEnabled(false);//让该按钮不可用
		p1_4.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_4);
		//p1的第二个Label
		p1_5=new JLabel("房间信息查询)",new ImageIcon("image/p1_5.jpg"),0);
		p1_5.setFont(MyTools.f3);
		p1_5.setCursor(MyCursor);//添加手形鼠标
		p1_5.setEnabled(false);//让该按钮不可用
		p1_5.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_5);
		
		//p1的第二个Label
		p1_3=new JLabel("帮助",new ImageIcon("image/p1_3.jpg"),0);
		p1_3.setFont(MyTools.f3);
		p1_3.setCursor(MyCursor);//添加手形鼠标
		p1_3.setEnabled(false);//让该按钮不可用
		p1_3.addMouseListener(this);//注册鼠标监听
		p1_imagePanel.add(p1_3);

		
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
		//***********************给p3面板添加卡片************************
		//p3第一张卡片（主页面）
		ImagePanel ip=new ImagePanel(zhu_image);
		p3.add(ip,"0");
		//对P3做添加，添加几个JLabel
		//JLabel  renshi=new JLabel(new ImageIcon("image/Person.jpg"));
		//创建WmpImfo对象
		
		//p3第二张卡片（员工信息）
		EmpInfo  p3EmpInfo=new EmpInfo();
		p3.add(p3EmpInfo,"1");
		
		//p3第三张卡片（登录管理：修改登录密码）
		JLabel denglu=new JLabel(new ImageIcon("image/denglu.jpg"));
		p3.add(denglu,"2");
		
		//p3第四张卡片（房客信息）
		ClieMain  ClieMain=new ClieMain();
		p3.add(ClieMain,"3");
		
		//p3的第五张卡片（客房信息）
		RoomMain RoomMain=new RoomMain();
		p3.add(RoomMain,"4");
		
		//吧p2,p3加入到p4
		p4.add(p2,"West");
		p4.add(p3,"Center");
		
		//做一个拆分窗口，分别存放P1和P4
		jsp1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,p1,p4);
		//指定左边的面板站多大
		jsp1.setDividerLocation(135);
		//把边界线设为0
		jsp1.setDividerSize(0);		
		
	}
	

	public Windows1()
	{
		//创建组件
		try {
			  titleIcon=ImageIO.read(new File("image/jiubei.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//调用初始化菜单函数
		this.initMenu();		
    //调用初始工具栏
		this.initToolBar();		
	//处理P1面板  中间区域		
		this.initAllPanels();
		
	//处理p5面板
		p5=new JPanel(new BorderLayout());
		t=new javax.swing.Timer(1000,this);//每隔一秒触发ActionEvent
		//启动t
		t.start();		
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
				
		
		//从JFrame中取得Container
		Container  ct=this.getContentPane();		
		
		ct.add(jtb,"North");
		ct.add(jsp1,"Center");		
		ct.add(p5,"South");
			
		
		//得到屏幕的宽和高
		int windows_width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int windows_height=Toolkit.getDefaultToolkit().getScreenSize().height;
		
		
		//窗口关闭时，退出系统
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置窗口的图片（左上角）
		this.setIconImage(titleIcon);
		this.setTitle("希尔顿酒店住房管理系统");
		//设置窗口大小
		this.setSize(windows_width,windows_height-45);
		this.setVisible(true);	
		
	}






	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.timeNow.setText("当前时间："+Calendar.getInstance().getTime().toLocaleString()+"      ");//改变时间显示格式
	
		if(e.getSource()==this.jmm1)
		{
			new UserLogin();
			this.dispose();
		}else if (e.getSource()==this.jmm2)
		{
			this.cardP3.show(p3, "3");
		}else if(e.getSource()==this.jmm3)
		{
			new UserLogin();
			this.dispose();
		}else if (e.getSource()==this.jmm21)
		{
			this.cardP3.show(p3, "1");
		}else if (e.getSource()==this.jmm22)
		{
			this.cardP3.show(p3, "1");
		}else if (e.getSource()==this.jmm23)
		{
			this.cardP3.show(p3, "1");
		}else if (e.getSource()==this.jm5)
		{
			this.cardP3.show(p3, "4");
		}
		
	}

//鼠标监听
	@Override
	public void mouseClicked(MouseEvent argp3) {
		// TODO Auto-generated method stub
		//判断用户点击那个操作JLabel
		if(argp3.getSource()==this.p1_1)
		{
			this.cardP3.show(p3, "1");
		}else if(argp3.getSource()==this.p1_2)
		{
			new ChangePass(Windows1.w1,"修改密码", true);/////////////////////////////////////////
			this.cardP3.show(p3,"2");
		}else if(argp3.getSource()==this.jmm22)  //添加员工
		{
			System.out.println("点击一级菜单认识管理的二级菜单”添加员工“");
			EmpAddDialog ea = new EmpAddDialog(this,"添加员工",true);
			//	JOptionPane.showMessageDialog(this, "添加员工成功");
		}else if(argp3.getSource()==this.p1_4)  
		{
			this.cardP3.show(p3,"3");
		}
		else if(argp3.getSource()==this.p1_5)  
		{
			this.cardP3.show(p3,"4");
		}else if (argp3.getSource()==this.p1_3)
		{
			new Help(Windows1.w1,"帮助", true);
		}
		
		
		
		
	}

	@Override
	public void mouseEntered(MouseEvent p1) {
		// TODO Auto-generated method stub
		//如果用户选中某个JLabel,则高亮
		if(p1.getSource()==this.p1_1)
		{
			this.p1_1.setEnabled(true);
		}
		else if(p1.getSource()==this.p1_2)
		{
			
		//	ChangePass cp=new ChangePass(Windows1.w1,"修改密码", boolean true);
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
	@Override
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

	@Override
	public void mousePressed(MouseEvent p1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
