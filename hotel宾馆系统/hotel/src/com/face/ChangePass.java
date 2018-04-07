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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.Model.EmpModel;

import com.Tools.*;

public class ChangePass extends JDialog implements ActionListener
{

	JPanel jp1, jp2, jp3;
	JLabel jl1, jl2, jl3, jl4;
	JTextField jtf1, jtf2, jtf3, jtf4;
	JButton jb1, jb2;
	EmpModel em;
	boolean changpass_b;

	public ChangePass(JFrame owner, String title, boolean modal)
	{
		super(owner, title, modal);
		this.setBounds(400, 250, 400, 400);
		this.setLayout(null);// 将修改密码的对话框设置为空布局
		jp1 = new JPanel();

		jl4 = new JLabel("请输入用户名:");
		jl4.setFont(MyTools.f1);
		jl4.setBounds(40, 0, 120, 40);
		this.add(jl4);
		jl1 = new JLabel("请输入原密码:");
		jl1.setFont(MyTools.f1);
		jl1.setBounds(40, 60, 120, 40);
		this.add(jl1);
		jl2 = new JLabel("请输入新密码: ");
		jl2.setFont(MyTools.f1);
		jl2.setBounds(40, 120, 120, 40);
		this.add(jl2);
		jl3 = new JLabel("请再次输入新密码：");
		jl3.setFont(MyTools.f1);
		jl3.setBounds(40, 180, 150, 40);
		this.add(jl3);

		jtf4 = new JTextField();
		jtf4.setBounds(180, 6, 170, 25);
		this.add(jtf4);
		/*
		 * jtf1 = new JTextField(); jtf1.setBounds(180, 65, 170, 25);
		 * this.add(jtf1); jtf2 = new JTextField(); jtf2.setBounds(180, 125,
		 * 170, 25); this.add(jtf2); jtf3 = new JTextField();
		 * jtf3.setBounds(180, 185, 170, 25); this.add(jtf3);
		 */
		jtf1 = new JPasswordField();
		jtf1.setBounds(180, 65, 170, 25);
		this.add(jtf1);
		jtf2 = new JPasswordField();
		jtf2.setBounds(180, 125, 170, 25);
		this.add(jtf2);
		jtf3 = new JPasswordField();
		jtf3.setBounds(180, 185, 170, 25);
		this.add(jtf3);

		jb1 = new JButton("确定");
		jb1.addActionListener(this);
		jb1.setBounds(120, 280, 60, 30);
		this.add(jb1);
		jb2 = new JButton("取消");
		jb2.addActionListener(this);
		jb2.setBounds(240, 280, 60, 30);
		this.add(jb2);

		// jtf1.setBounds(4)

		/*
		 * jp1.setLayout(new GridLayout(3,1));
		 * 
		 * jl1 = new JLabel("请输入原密码：          "); jp1.add(jl1); jl2 = new
		 * JLabel("请输入新密码：          "); jp1.add(jl2); jl3 = new
		 * JLabel("请再次输入新密码："); jp1.add(jl3);
		 * 
		 * jp2 = new JPanel();
		 * 
		 * jp2.setLayout(new GridLayout(3,1)); jtf1 = new JTextField(20);
		 * jp2.add(jtf1); jtf2 = new JTextField(20); jp2.add(jtf2); jtf3 = new
		 * JTextField(20); jp2.add(jtf3);
		 * 
		 * jp3 = new JPanel(); jb1 = new JButton("确认");
		 * jb1.addActionListener(this); jp3.add(jb1); jb2 = new JButton("取消");
		 * jb2.addActionListener(this); jp3.add(jb2);
		 * 
		 * 
		 * this.add(jp1,BorderLayout.WEST); this.add(jp2,BorderLayout.CENTER);
		 * this.add(jp3,BorderLayout.SOUTH);
		 */
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0)
	{

		/*
		 * //这种修改密码的方式不要求在输对原密码 if(arg0.getSource()==jb1) {
		 * 
		 * this.remove(jtf1); //移除组件???????
		 * 
		 * this.getContentPane().remove(jtf1); this.getContentPane().repaint();
		 * 
		 * 
		 * 
		 * em = new EmpModel(); String get_jtf4 = jtf4.getText();
		 * System.out.print("     在文本框1中输入的新密码为:"+get_jtf4); String paras1 [] =
		 * {get_jtf4}; em.query("select * from login where userid = ?", paras1);
		 * changpass_b = em.b; if(!changpass_b) {
		 * JOptionPane.showMessageDialog(this, "您输入的用户名不存在，请重新输入！"); } else
		 * if(!jtf2.getText().equals(jtf3.getText())) // else if(2!=2) {
		 * System.out
		 * .println("jtf2中输入的值为:"+jtf2.getText()+"jtf3中输入的值为:"+jtf3.getText());
		 * JOptionPane.showMessageDialog(this, "两次输入的新密码不一致，请重新输入！"); } else
		 * if(changpass_b &&jtf2.getText().equals(jtf3.getText())) {
		 * System.out.println("jtf2.getText()==jtf3.getText()"); em = new
		 * EmpModel(); String [] paras_changepass = {jtf2.getText()}; String sql
		 * = "update login set password = ?"; em.updEmp(sql, paras_changepass);
		 * if(jtf2.getText().equals("")) { JOptionPane.showMessageDialog(this,
		 * "密码修改成功，新密码为：空值"); }else if(!jtf2.getText().equals("")) {
		 * JOptionPane.showMessageDialog(this, "密码修改成功，新密码为："+jtf2.getText()); }
		 * 
		 * this.dispose(); } }
		 */

		// 这种修改密码的方式要求在输对原密码的情况下才能修改
		if (arg0.getSource() == jb1)
		{
			em = new EmpModel();
			String get_jtf1 = jtf1.getText();
			System.out.print("     在文本框1中输入的新密码为:" + get_jtf1);
			String paras1[] =
			{ get_jtf1 };
			em.query("select * from login where password = ?", paras1);
			changpass_b = em.b;
			if (!changpass_b)
			{
				JOptionPane.showMessageDialog(this, "原密码输入错误，请重新输入！");
			}

			// else if(!jtf2.getText()==jtf3.getText()) //这种写法是cc++中写法，不是java的写法
			else if (!jtf2.getText().equals(jtf3.getText()))
			// else if(2!=2)
			{
				System.out.println("jtf2中输入的值为:" + jtf2.getText() + "jtf3中输入的值为:" + jtf3.getText());
				JOptionPane.showMessageDialog(this, "两次输入的新密码不一致，请重新输入！");
			}

			// else if(changpass_b &&jtf2.getText()==jtf3.getText())

			else if (changpass_b && jtf2.getText().equals(jtf3.getText()))
			{
				System.out.println("jtf2.getText()==jtf3.getText()");
				em = new EmpModel();
				String[] paras_changepass =
				{ jtf2.getText() };
				String sql = "update login set password = ?";
				em.updEmp(sql, paras_changepass);
				if (jtf2.getText().equals(""))
				{
					JOptionPane.showMessageDialog(this, "密码修改成功，新密码为：空值");
				} else if (!jtf2.getText().equals(""))
				{
					// JOptionPane.showMessageDialog(this,
					// "密码修改成功，新密码为："+jtf2.getText());
					JOptionPane.showMessageDialog(this, "密码修改成功");
				}

				this.dispose();
			}
		}

		else if (arg0.getSource() == jb2)
		{
			this.dispose();
		}

	}
}
