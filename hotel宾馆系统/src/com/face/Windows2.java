/**
 * ��������Ա�������Ľ���
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
	
	//�������
	Image win2_titleIcon,timebg;
	JMenuBar win2_jmb;
	//����һ���˵�
	JMenu win2_jm1,win2_jm2;
	//��������˵�
	JMenuItem win2_jmi1_1,win2_jmi1_2;
	//ͼ��
	ImageIcon  win2_jmm1, win2_jmm2;//�����˵���ͼ��
	
	//������
	JToolBar win2_jtb;
	JButton win2_jb1,win2_jb2,win2_jb3;
	//������Ҫ��������
	JPanel p1,p2,p3,p4,p5;
	//javax.swing���е�TImer���Զ�ʱ�Ĵ���Action�¼�
	javax.swing.Timer  time;
	JLabel  timeNow;
	
	ImagePanel p1_imagePanel;
	
	JLabel   p1_1, p1_2, p1_3, p1_4, p1_5, p1_6, p1_7, p1_8,p1_9;
	JLabel p2_1,p2_2;
	
	//���ڲ��ʱ��
	JSplitPane jsp1;
	
	CardLayout cardP3;
	
	
	
	
	//��ʼ���˵�����
	public void win2_initMenu()
	{
		//����ͼ��
		win2_jmm1=new ImageIcon("image/jmm1_3.jpg");
		win2_jmm2=new ImageIcon("image/jmm1_5.jpg");
		//���������˵�
		win2_jmi1_1=new JMenuItem("�޸�����",win2_jmm1);
		win2_jmi1_2=new JMenuItem("�˳�",win2_jmm2);
		//����һ���˵�
		win2_jm1=new JMenu("ϵͳ");
		win2_jm1.setFont(MyTools.f1);
		win2_jm1.add(win2_jmi1_1);
		win2_jm1.add(win2_jmi1_2);
		
		win2_jm2=new JMenu("����");
		win2_jm2.setFont(MyTools.f1);
		
		//��ӵ����
		win2_jmb=new JMenuBar();
		win2_jmb.add(win2_jm1);
		win2_jmb.add(win2_jm2);
		//��JMenuBar��ӵ�JFrame
		this.setJMenuBar(win2_jmb);
		
		//win2_jmi1_1.setFont(MyTools.f2);
		
	}
	
	
	//��ʼ��������
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

	
	//��ʼ���м�����Panel
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
		
       Cursor MyCursor=new Cursor(Cursor.HAND_CURSOR);//����һ���������
		
		
		this.p1_imagePanel=new ImagePanel(p1_bg);
		this.p1_imagePanel.setLayout(new GridLayout(3,3));//���񲼾ְ���һ��
		//p1_imagePanel.add(new JLabel(new ImageIcon("image/room1.png")));
	//p1�ĵ�һ��Label
		p1_1=new JLabel("101",new ImageIcon("image/room1.png"),0);
		p1_1.setFont(MyTools.f3);//������ʾ����		
		p1_1.setCursor(MyCursor);//����������
		p1_1.setEnabled(false);//�øð�ť������
		p1_1.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_1);
		//p1�ĵڶ���Label
		p1_2=new JLabel("102",new ImageIcon("image/room1.png"),0);
		p1_2.setFont(MyTools.f3);	
		p1_2.setCursor(MyCursor);//����������
		p1_2.setEnabled(false);//�øð�ť������
		p1_2.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_2);
		//p1�ĵڶ���Label
		p1_3=new JLabel("103",new ImageIcon("image/room1.png"),0);
		p1_3.setFont(MyTools.f3);
		p1_3.setCursor(MyCursor);//����������
		p1_3.setEnabled(false);//�øð�ť������
		p1_3.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_3);
		//p1�ĵڶ���Label
		p1_4=new JLabel("201",new ImageIcon("image/room1.png"),0);
		p1_4.setFont(MyTools.f3);
		p1_4.setCursor(MyCursor);//����������
		p1_4.setEnabled(false);//�øð�ť������
		p1_4.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_4);
		//p1�ĵڶ���Label
		p1_5=new JLabel("202",new ImageIcon("image/room1.png"),0);
		p1_5.setFont(MyTools.f3);
		p1_5.setCursor(MyCursor);//����������
		p1_5.setEnabled(false);//�øð�ť������
		p1_5.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_5);
		//p1�ĵڶ���Label
		p1_6=new JLabel("203",new ImageIcon("image/room1.png"),0);
		p1_6.setFont(MyTools.f3);
		p1_6.setCursor(MyCursor);//����������
		p1_6.setEnabled(false);//�øð�ť������
		p1_6.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_6);
		//p1�ĵ��߸�Label
		p1_7=new JLabel("301",new ImageIcon("image/room1.png"),0);
		p1_7.setFont(MyTools.f3);
		p1_7.setCursor(MyCursor);//����������
		p1_7.setEnabled(false);//�øð�ť������
		p1_7.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_7);
		//p1�ĵڶ���Label
		p1_8=new JLabel("302",new ImageIcon("image/room1.png"),0);
		p1_8.setFont(MyTools.f3);
		p1_8.setCursor(MyCursor);//����������
		p1_8.setEnabled(false);//�øð�ť������
		p1_8.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_8);
		//p1�ĵڶ���Label
		p1_9=new JLabel("303",new ImageIcon("image/room1.png"),0);
		p1_9.setFont(MyTools.f3);
		p1_9.setCursor(MyCursor);//����������
		p1_9.setEnabled(false);//�øð�ť������
		p1_9.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_9);
		
		

		
		p1.add(this.p1_imagePanel);
		
		//����p2,p3,p4 ���
		p4=new JPanel(new BorderLayout());
		p2=new JPanel(new CardLayout());//��Ƭ����
		//��p2����ӿ�Ƭ
		p2_1=new JLabel(new ImageIcon("image/p2_left.jpg"));
		p2_2=new JLabel(new ImageIcon("image/p2_right.jpg"));
		p2.add(p2_1,"0");//�ɿ�Ƭ�������
		p2.add(p2_2,"1");
		//��p3����ӿ�Ƭ
		this.cardP3=new CardLayout();
		p3=new JPanel(this.cardP3);//��Ƭ����
		Image zhu_image=null;
		try {
			zhu_image = ImageIO.read(new File("image/zhu_bg.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ImagePanel ip=new ImagePanel(zhu_image);
		p3.add(ip,"0");
		//��P3����ӣ���Ӽ���JLabel
		//JLabel  renshi=new JLabel(new ImageIcon("image/Person.jpg"));
		//����WmpImfo����
		EmpInfo  p3EmpInfo=new EmpInfo();
		p3.add(p3EmpInfo,"1");
		JLabel denglu=new JLabel(new ImageIcon("image/denglu.jpg"));
		p3.add(denglu,"2");
		//��p2,p3���뵽p4
		p4.add(p2,"West");
		p4.add(p3,"Center");
		
		//��һ����ִ��ڣ��ֱ���P1��P4
		jsp1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,p1,p4);
		//ָ����ߵ����վ���
		jsp1.setDividerLocation(935);
		//�ѱ߽�����Ϊ0
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
		
		//���ó�ʼ���˵�����
		this.win2_initMenu();	
		//���ó�ʼ������
		this.win2_initToolBar();
		this.win2_initAllPanels();
		
		
		//����p5���
		p5=new JPanel(new BorderLayout());
		time=new javax.swing.Timer(1000,this);//ÿ��һ�봥��ActionEvent
		//����t
		time.start();		
		timeNow=new JLabel(Calendar.getInstance().getTime().toLocaleString());//�ı���ʱ���ʵ�ַ�ʽ
		//ʱ����屳��������
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
		//��JFrame��ȡ������
		Container win2_ct=this.getContentPane();	
		
		//��JPanel��������
		win2_ct.add(win2_jtb,"North");
		win2_ct.add(jsp1,"Center");
		win2_ct.add(p5,"South");
		
		
		
		
		
		
		
		
		
		
		
		//�õ���Ļ�Ĵ�С
		int windows2_width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int windows2_height=Toolkit.getDefaultToolkit().getScreenSize().height;
		//���ڹر�ʱ���˳�ϵͳ
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//���ô��ڵ�ͼƬ�����Ͻǣ�
		this.setIconImage(win2_titleIcon);
		this.setTitle("ϣ���پƵ�ס������ϵͳ");
		this.setSize(windows2_width,windows2_height-45);
		this.setVisible(true);		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.timeNow.setText("��ǰʱ�䣺"+Calendar.getInstance().getTime().toLocaleString()+"      ");//�ı�ʱ����ʾ��ʽ
	}


	//������

	public void mouseClicked(MouseEvent argp3) {
		// TODO Auto-generated method stub
		//�ж��û�����Ǹ�����JLabel
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
		//����û�ѡ��ĳ��JLabel,�����
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
		//����û�ѡ��ĳ��JLabel,�����
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
