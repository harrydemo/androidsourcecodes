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
	// 定义需要的组件
	JLabel jl1, jl2, jl3;
	JTextField jName;
	JPasswordField jPasswd;
	JButton jCon, jCancel;
	JPanel panel = new JPanel();
	ButtonGroup bgp = new ButtonGroup();

	// 定义单选按钮
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
		// 创建各个组件
		// 添加用户名框
		jl1 = new JLabel("请输入用户名：");
		jl1.setFont(MyTools.f1);
		jl1.setBounds(60, 190, 150, 30);
		// 放入
		ct.add(jl1);
		jName = new JTextField(20);
		jName.setFont(MyTools.f1);
		jName.setBounds(180, 190, 120, 30);
		// 设置下奥
		jName.setBorder(BorderFactory.createLoweredBevelBorder());
		// 放入
		ct.add(jName);
		// 添加小字
		jl2 = new JLabel("(或员工号)");
		jl2.setFont(MyTools.f2);
		// 设置前景色
		jl2.setForeground(Color.red);
		jl2.setBounds(105, 205, 100, 30);
		ct.add(jl2);

		// 添加密码框
		jl3 = new JLabel("请输入密码：");
		jl3.setFont(MyTools.f1);
		jl3.setBounds(60, 225, 150, 30);
		// 放入
		ct.add(jl3);
		// 密码框
		jPasswd = new JPasswordField(20);
		jPasswd.setBounds(180, 225, 120, 30);
		// 设置下凹
		jPasswd.setBorder(BorderFactory.createLoweredBevelBorder());
		ct.add(jPasswd);

		// 定义单选按钮
		// 定义员工
		Staff = new JRadioButton("员工");
		Staff.setFont(MyTools.f1);
		Staff.setBounds(190, 255, 70, 30);
		Manager.addActionListener(this);
		ct.add(Staff);

		// 定义“经理”
		Manager = new JRadioButton("经理");
		Manager.setFont(MyTools.f1);
		Manager.setBounds(90, 255, 70, 30);
		Manager.addActionListener(this);
		ct.add(Manager);

		// 确定按钮
		jCon = new JButton("确定");
		jCon.setFont(MyTools.f1);
		jCon.setBounds(110, 300, 70, 30);
		jCon.addActionListener(this);
		// 放入容器
		ct.add(jCon);

		// 取消按钮
		jCancel = new JButton("取消");
		jCancel.setFont(MyTools.f1);
		jCancel.setBounds(220, 300, 70, 30);
		jCancel.addActionListener(this);
		// 放入容器
		ct.add(jCancel);

		this.setLayout(null);
		// 创建一个BackImage对象
		BackImage bi = new BackImage();

		// 确定图片的位置
		bi.setBounds(0, 0, 360, 360);
		// 添加
		this.add(bi);
		// 不使用上下框
		// 把一个组件放入到JFrame或者JDialog中可以直接放入
		this.setUndecorated(true);
		// 设置大小
		this.setSize(360, 360);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(width / 2 - 200, height / 2 - 150);
		this.setVisible(true);

	}

	// 内部类
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
		String username, password;// 定义用户名，密码
		username = jName.getText().trim();
		password = new String(this.jPasswd.getPassword());
		if (e.getSource() == jCon)
		{

			try
			{
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException ce)
			{
				JOptionPane.showMessageDialog(junshuaizhang, ce.getMessage()); // 在第14行需要定义junshuaizhang
			}

			// 如果选择经理
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
							// "登陆成功");
							// this.setVisible(false);
							this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);// 此处产生Exception
																					// in
																					// thread
																					// "AWT-EventQueue-0"
																					// java.lang.IllegalArgumentException:
																					// defaultCloseOperation
																					// mus的错误如用Exite_to_close
							// FindScore s=new FindScore();
							Windows1 manager = new Windows1();
						} else
						{
							JOptionPane.showMessageDialog(junshuaizhang, "登录失败");
						}
					}
					rs.close();
					stmt.close();
				} catch (SQLException sqlWrong)
				{
					JOptionPane.showMessageDialog(junshuaizhang, sqlWrong.getMessage());// 不连接数据库以后这里会出错误

				}
				System.out.println("经理");
			}

			// 选择员工
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
							// JOptionPane.showMessageDialog(junshuaizhang,"登录成功");//先有链接程序后删除
							this.setVisible(false);// 点击确定后退出
							// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							Windows2 manager2 = new Windows2();
						} else
						{
							JOptionPane.showMessageDialog(junshuaizhang, "登录失败");
						}
					}
				} catch (SQLException sqlWrong)
				{
					JOptionPane.showMessageDialog(junshuaizhang, sqlWrong.getMessage());
				}
				System.out.println("员工");
			}
			System.out.println("确定");
			this.dispose();
		} else if (e.getSource() == jCancel)
		{
			System.out.println("取消");
			this.dispose();
		}
	}
}
