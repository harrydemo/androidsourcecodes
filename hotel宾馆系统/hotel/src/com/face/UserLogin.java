package com.face;

import com.Tools.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;

import com.Tools.MyTools;

import java.io.*;
import java.util.*;
import java.sql.*;

//import		 java.awt.font.*;
public class UserLogin extends JDialog implements ActionListener
{

	static UserLogin junshuaizhang;
	// ������Ҫ�����
	JLabel jl1, jl2, jl3;
	JTextField jName;
	JPasswordField jPasswd;
	JButton jCon, jCancel;
	JPanel panel = new JPanel();
	ButtonGroup bgp = new ButtonGroup();

	// ���嵥ѡ��ť
	JRadioButton Manager = new JRadioButton();
	JRadioButton Staff = new JRadioButton();

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		UserLogin ukl = new UserLogin();
	}

	public UserLogin()
	{
		Container ct = this.getContentPane();
		// �����������
		// ����û�����
		jl1 = new JLabel("�������û�����");
		jl1.setFont(MyTools.f1);
		jl1.setBounds(60, 190, 150, 30);
		// ����
		ct.add(jl1);
		jName = new JTextField(20);
		jName.setFont(MyTools.f1);
		jName.setBounds(180, 190, 120, 30);
		// �����°�
		jName.setBorder(BorderFactory.createLoweredBevelBorder());
		// ����
		ct.add(jName);
		// ���С��
		jl2 = new JLabel("(��Ա����)");
		jl2.setFont(MyTools.f2);
		// ����ǰ��ɫ
		jl2.setForeground(Color.red);
		jl2.setBounds(105, 205, 100, 30);
		ct.add(jl2);

		// ��������
		jl3 = new JLabel("���������룺");
		jl3.setFont(MyTools.f1);
		jl3.setBounds(60, 225, 150, 30);
		// ����
		ct.add(jl3);
		// �����
		jPasswd = new JPasswordField(20);
		jPasswd.setBounds(180, 225, 120, 30);
		// �����°�
		jPasswd.setBorder(BorderFactory.createLoweredBevelBorder());
		ct.add(jPasswd);

		// ���嵥ѡ��ť
		// ����Ա��
		Staff = new JRadioButton("Ա��");
		Staff.setFont(MyTools.f1);
		Staff.setBounds(190, 255, 70, 30);
		Manager.addActionListener(this);
		ct.add(Staff);

		// ���塰����
		Manager = new JRadioButton("����");
		Manager.setFont(MyTools.f1);
		Manager.setBounds(90, 255, 70, 30);
		Manager.addActionListener(this);
		ct.add(Manager);

		// ȷ����ť
		jCon = new JButton("ȷ��");
		jCon.setFont(MyTools.f1);
		jCon.setBounds(110, 300, 70, 30);
		jCon.addActionListener(this);
		// ��������
		ct.add(jCon);

		// ȡ����ť
		jCancel = new JButton("ȡ��");
		jCancel.setFont(MyTools.f1);
		jCancel.setBounds(220, 300, 70, 30);
		jCancel.addActionListener(this);
		// ��������
		ct.add(jCancel);

		this.setLayout(null);
		// ����һ��BackImage����
		BackImage bi = new BackImage();

		// ȷ��ͼƬ��λ��
		bi.setBounds(0, 0, 360, 360);
		// ���
		this.add(bi);
		// ��ʹ�����¿�
		// ��һ��������뵽JFrame����JDialog�п���ֱ�ӷ���
		this.setUndecorated(true);
		// ���ô�С
		this.setSize(360, 360);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(width / 2 - 200, height / 2 - 150);
		this.setVisible(true);

	}

	// �ڲ���
	class BackImage extends JPanel
	{
		Image im;

		public BackImage()
		{
			try
			{
				im = ImageIO.read(new File("image//login.jpg"));
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void paintComponent(Graphics g)
		{
			g.drawImage(im, 0, 0, 360, 180, this);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		String username, password;// �����û���������
		username = jName.getText().trim();
		password = new String(this.jPasswd.getPassword());
		if (e.getSource() == jCon)
		{

			try
			{
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException ce)
			{
				JOptionPane.showMessageDialog(junshuaizhang, ce.getMessage()); // �ڵ�14����Ҫ����junshuaizhang
			}

			// ���ѡ����
			if (Manager.isSelected())
			{
				try
				{
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/housing management?useUnicode=true&character=GBK", "root", "root");
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("select * from login");
					while (rs.next())
					{
						if ((rs.getString("userid").equals(username)) && (rs.getString("password")).equals(password))
						{
							// JOptionPane.showMessageDialog(junshuaizhang,
							// "��½�ɹ�");
							// this.setVisible(false);
							this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);// �˴�����Exception
																					// in
																					// thread
																					// "AWT-EventQueue-0"
																					// java.lang.IllegalArgumentException:
																					// defaultCloseOperation
																					// mus�Ĵ�������Exite_to_close
							// FindScore s=new FindScore();
							Windows1 manager = new Windows1();
						} else
						{
							JOptionPane.showMessageDialog(junshuaizhang, "��¼ʧ��");
						}
					}
					rs.close();
					stmt.close();
				} catch (SQLException sqlWrong)
				{
					JOptionPane.showMessageDialog(junshuaizhang, sqlWrong.getMessage());// ���������ݿ��Ժ�����������

				}
				System.out.println("����");
			}

			// ѡ��Ա��
			else if (Staff.isSelected())
			{
				try
				{
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/housing management?useUniCode=true&character=GBK", "root", "root");
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("select * from login");
					while (rs.next())
					{
						if ((rs.getString("userid").equals(username)) && (rs.getString("password").equals(password)))
						{
							// JOptionPane.showMessageDialog(junshuaizhang,"��¼�ɹ�");//�������ӳ����ɾ��
							this.setVisible(false);// ���ȷ�����˳�
							// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							Windows2 manager2 = new Windows2();
						} else
						{
							JOptionPane.showMessageDialog(junshuaizhang, "��¼ʧ��");
						}
					}
				} catch (SQLException sqlWrong)
				{
					JOptionPane.showMessageDialog(junshuaizhang, sqlWrong.getMessage());
				}
				System.out.println("Ա��");
			}
			System.out.println("ȷ��");
			this.dispose();
		} else if (e.getSource() == jCancel)
		{
			System.out.println("ȡ��");
			this.dispose();
		}
	}
}
