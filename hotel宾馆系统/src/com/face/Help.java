/**
 * 此类用于修改login表中的用户的密码
 */
package com.face;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.Model.EmpModel;

import com.Tools.*;

public class Help extends JDialog
{

	JPanel jp1, jp2, jp3;
	JLabel jl1, jl2, jl3, jl4;
	EmpModel em;
	boolean changpass_b;

	public Help(JFrame owner, String title, boolean modal)
	{
		super(owner, title, modal);
		this.setBounds(350, 230, 780, 300);
		this.setLayout(null);// 将修改密码的对话框设置为空布局
		jp1 = new JPanel();

		jl4 = new JLabel("点击“人事管理”，可对酒店员工的基本信息进行管理");
		jl4.setFont(MyTools.f1);
		jl4.setBounds(40, 0, 1000, 40);
		this.add(jl4);
		jl1 = new JLabel("点击“登录管理”，用于修改密码");
		jl1.setFont(MyTools.f1);
		jl1.setBounds(40, 60, 1000, 40);
		this.add(jl1);
		jl2 = new JLabel("点击“顾客信息管理“，可以管理已入住的顾客的基本信息，包括房间号等，还可以在此进行结算 ");
		jl2.setFont(MyTools.f1);
		jl2.setBounds(40, 120, 1000, 40);
		this.add(jl2);
		jl3 = new JLabel("点击”房间信息管理“，可以查看和管理酒店房间的状态及价格");
		jl3.setFont(MyTools.f1);
		jl3.setBounds(40, 180, 1000, 40);
		this.add(jl3);
		this.setVisible(true);
	}
}
