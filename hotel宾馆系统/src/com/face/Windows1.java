/*
 * ��ɽ����˳�򣬴��ϵ��£�������
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
	//������Ҫ�����
	Image	 titleIcon,timebg;
	JMenuBar jmb;
	//һ���˵�
	JMenu jm1,jm2,jm3,jm4,jm5,jm6;
	//�����˵�
	JMenuItem jmm1,jmm2,jmm3,jmm4,jmm5;
	JMenuItem jmm21,jmm22,jmm23,jmm24,jmm25;
	JMenuItem jmm31,jmm32;
	
	//ͼ����
	ImageIcon jmm1_1,jmm1_2,jmm1_3,jmm1_4,jmm1_5;
	ImageIcon jmm2_1,jmm2_2,jmm2_3,jmm2_4,jmm2_5;
	
	
	//������
	JToolBar jtb;
	JButton  jb1,jb2,jb3,jb4,jb5,jb6,jb7,jb8,jb9,jb10;
	
	//������Ҫ��������
	JPanel p1,p2,p3,p4,p5;
	//��ʾ��ǰʱ��
	JLabel  timeNow;
	JLabel   p1_1, p1_2, p1_3, p1_4, p1_5, p1_6, p1_7, p1_8;
	//��P2��嶨����Ҫ��JLabel
	JLabel p2_1,p2_2;
	//javax.swing���е�TImer���Զ�ʱ�Ĵ���Action�¼�
	javax.swing.Timer  t;
	
	ImagePanel p1_imagePanel;
	
	//���ڲ��ʱ��
	JSplitPane jsp1;
	
	CardLayout cardP3;
	
	
	
	
	public static void main(String[] args) {	
		
		Windows1  w1=new Windows1();
	}
	
	
	
	//��ʼ���˵�
	public void initMenu()
	{
		//����ͼ��
		jmm1_1=new ImageIcon("image/jmm1_1.jpg");
		jmm1_2=new ImageIcon("image/jmm1_2.jpg");
		jmm1_3=new ImageIcon("image/jmm1_3.jpg");
		//jmm1_4=new ImageIcon("image/jmm1_4.jpg");
		jmm1_5=new ImageIcon("image/jmm1_5.jpg");
		//����һ���˵�
		jm1=new JMenu("ϵͳ����");
		jm1.setFont(MyTools.f1);//��������
		//ϵͳ���� �Ĵ��������˵�
		jmm1=new JMenuItem("�л��û�",jmm1_1);
	//	jmm1.addMouseListener(this);
		jmm1.addActionListener(this);
		//jmm1.setFont(MyTools.f2);
		jmm2=new JMenuItem("�л����տ����",jmm1_2);
		jmm2.addActionListener(this);
	//	jmm2.addMouseListener(this);
		jmm3=new JMenuItem("��½����",jmm1_3);	
		jmm3.addActionListener(this);
		jmm5=new JMenuItem("�˳�",jmm1_5);
		
		//����
		jm1.add(jmm1);
		jm1.add(jmm2);
		jm1.add(jmm3);
		//jm1.add(jmm4);
		jm1.add(jmm5);
		
		
		
		
		jmm2_1=new ImageIcon("image/jmm2_1.png");
		jmm2_2=new ImageIcon("image/jmm2_2.jpg");
		jmm2_3=new ImageIcon("image/jmm2_3.jpg");
		jm2=new JMenu("���¹���");
		jm2.setFont(MyTools.f1);
		//���¹���Ķ�������
		jmm21=new JMenuItem("��ѯ",jmm2_1);
		jmm21.addActionListener(this);
		jmm22=new JMenuItem("���");
		jmm22.addActionListener(this);
//		jmm22.addMouseListener(this);
		jmm23=new JMenuItem("�޸�");
		jmm23.addActionListener(this);
		jm2.add(jmm21);
		jm2.add(jmm22);
		jm2.add(jmm23);
		
		//jm3=new JMenu("�˵��������");
		//jm3.setFont(MyTools.f1);
		jm3=new JMenu("����ͳ��");
		jm3.setFont(MyTools.f1);
		jmm31=new JMenuItem("����ͼ");
		jmm32=new JMenuItem("����ͼ");
		jm3.add(jmm31);
		jm3.add(jmm32);
		
		jm5=new JMenu("���䶨��");
		jm5.addActionListener(this);
		jm5.setFont(MyTools.f1);
		
		jm6=new JMenu("����");
		jm6.addActionListener(this);
		jm6.setFont(MyTools.f1);
		
		//��һ���˵����뵽JMenuBar
		jmb=new JMenuBar();
		jmb.add(jm1);
		jmb.add(jm2);
		//jmb.add(jm3);
		jmb.add(jm3);
		jmb.add(jm5);
		jmb.add(jm6);		
		
		//��JMenuBar��ӵ�JFrame
		this.setJMenuBar(jmb);
	}
	
	//��ʼ��������
	public void initToolBar()
	{
		jtb=new JToolBar();
		//���ù����������Ը���
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
	
	
	//��ʼ���м�����Panel
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
		
		
		Cursor MyCursor=new Cursor(Cursor.HAND_CURSOR);//����һ���������
		
		
		this.p1_imagePanel=new ImagePanel(p1_bg);
		this.p1_imagePanel.setLayout(new GridLayout(8,1));//���񲼾ְ���һ��
		p1_imagePanel.add(new JLabel(new ImageIcon("image/login1.png")));
		//p1�ĵ�һ��Label
		p1_1=new JLabel("��  ��  ��  ��",new ImageIcon("image/p1_1.jpg"),0);
		p1_1.setFont(MyTools.f3);//������ʾ����		
		p1_1.setCursor(MyCursor);//����������
		p1_1.setEnabled(false);//�øð�ť������
		p1_1.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_1);
		//p1�ĵڶ���Label
		p1_2=new JLabel("��  ¼  ��  ��",new ImageIcon("image/p1_2.jpg"),0);
		p1_2.setFont(MyTools.f3);	
		p1_2.setCursor(MyCursor);//����������
		p1_2.setEnabled(false);//�øð�ť������
		p1_2.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_2);
		
		//p1�ĵڶ���Label
		p1_4=new JLabel("�˿���Ϣ����",new ImageIcon("image/p1_4.jpg"),0);
		p1_4.setFont(MyTools.f3);
		p1_4.setCursor(MyCursor);//����������
		p1_4.setEnabled(false);//�øð�ť������
		p1_4.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_4);
		//p1�ĵڶ���Label
		p1_5=new JLabel("������Ϣ��ѯ)",new ImageIcon("image/p1_5.jpg"),0);
		p1_5.setFont(MyTools.f3);
		p1_5.setCursor(MyCursor);//����������
		p1_5.setEnabled(false);//�øð�ť������
		p1_5.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_5);
		
		//p1�ĵڶ���Label
		p1_3=new JLabel("����",new ImageIcon("image/p1_3.jpg"),0);
		p1_3.setFont(MyTools.f3);
		p1_3.setCursor(MyCursor);//����������
		p1_3.setEnabled(false);//�øð�ť������
		p1_3.addMouseListener(this);//ע��������
		p1_imagePanel.add(p1_3);

		
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
		//***********************��p3�����ӿ�Ƭ************************
		//p3��һ�ſ�Ƭ����ҳ�棩
		ImagePanel ip=new ImagePanel(zhu_image);
		p3.add(ip,"0");
		//��P3����ӣ���Ӽ���JLabel
		//JLabel  renshi=new JLabel(new ImageIcon("image/Person.jpg"));
		//����WmpImfo����
		
		//p3�ڶ��ſ�Ƭ��Ա����Ϣ��
		EmpInfo  p3EmpInfo=new EmpInfo();
		p3.add(p3EmpInfo,"1");
		
		//p3�����ſ�Ƭ����¼�����޸ĵ�¼���룩
		JLabel denglu=new JLabel(new ImageIcon("image/denglu.jpg"));
		p3.add(denglu,"2");
		
		//p3�����ſ�Ƭ��������Ϣ��
		ClieMain  ClieMain=new ClieMain();
		p3.add(ClieMain,"3");
		
		//p3�ĵ����ſ�Ƭ���ͷ���Ϣ��
		RoomMain RoomMain=new RoomMain();
		p3.add(RoomMain,"4");
		
		//��p2,p3���뵽p4
		p4.add(p2,"West");
		p4.add(p3,"Center");
		
		//��һ����ִ��ڣ��ֱ���P1��P4
		jsp1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,p1,p4);
		//ָ����ߵ����վ���
		jsp1.setDividerLocation(135);
		//�ѱ߽�����Ϊ0
		jsp1.setDividerSize(0);		
		
	}
	

	public Windows1()
	{
		//�������
		try {
			  titleIcon=ImageIO.read(new File("image/jiubei.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//���ó�ʼ���˵�����
		this.initMenu();		
    //���ó�ʼ������
		this.initToolBar();		
	//����P1���  �м�����		
		this.initAllPanels();
		
	//����p5���
		p5=new JPanel(new BorderLayout());
		t=new javax.swing.Timer(1000,this);//ÿ��һ�봥��ActionEvent
		//����t
		t.start();		
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
				
		
		//��JFrame��ȡ��Container
		Container  ct=this.getContentPane();		
		
		ct.add(jtb,"North");
		ct.add(jsp1,"Center");		
		ct.add(p5,"South");
			
		
		//�õ���Ļ�Ŀ�͸�
		int windows_width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int windows_height=Toolkit.getDefaultToolkit().getScreenSize().height;
		
		
		//���ڹر�ʱ���˳�ϵͳ
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//���ô��ڵ�ͼƬ�����Ͻǣ�
		this.setIconImage(titleIcon);
		this.setTitle("ϣ���پƵ�ס������ϵͳ");
		//���ô��ڴ�С
		this.setSize(windows_width,windows_height-45);
		this.setVisible(true);	
		
	}






	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.timeNow.setText("��ǰʱ�䣺"+Calendar.getInstance().getTime().toLocaleString()+"      ");//�ı�ʱ����ʾ��ʽ
	
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

//������
	@Override
	public void mouseClicked(MouseEvent argp3) {
		// TODO Auto-generated method stub
		//�ж��û�����Ǹ�����JLabel
		if(argp3.getSource()==this.p1_1)
		{
			this.cardP3.show(p3, "1");
		}else if(argp3.getSource()==this.p1_2)
		{
			new ChangePass(Windows1.w1,"�޸�����", true);/////////////////////////////////////////
			this.cardP3.show(p3,"2");
		}else if(argp3.getSource()==this.jmm22)  //���Ա��
		{
			System.out.println("���һ���˵���ʶ����Ķ����˵������Ա����");
			EmpAddDialog ea = new EmpAddDialog(this,"���Ա��",true);
			//	JOptionPane.showMessageDialog(this, "���Ա���ɹ�");
		}else if(argp3.getSource()==this.p1_4)  
		{
			this.cardP3.show(p3,"3");
		}
		else if(argp3.getSource()==this.p1_5)  
		{
			this.cardP3.show(p3,"4");
		}else if (argp3.getSource()==this.p1_3)
		{
			new Help(Windows1.w1,"����", true);
		}
		
		
		
		
	}

	@Override
	public void mouseEntered(MouseEvent p1) {
		// TODO Auto-generated method stub
		//����û�ѡ��ĳ��JLabel,�����
		if(p1.getSource()==this.p1_1)
		{
			this.p1_1.setEnabled(true);
		}
		else if(p1.getSource()==this.p1_2)
		{
			
		//	ChangePass cp=new ChangePass(Windows1.w1,"�޸�����", boolean true);
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

	@Override
	public void mousePressed(MouseEvent p1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
