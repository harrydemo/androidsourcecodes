/**
 * 在添加员工时，弹出输入错误
 */
package com.face;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class noNull extends JDialog implements ActionListener
{

	JPanel jp;
	JLabel jl;
	JButton jb;

	public noNull(JDialog jd, String string, boolean b)
	{

		super(jd, string, b);
		this.setBounds(400, 250, 150, 150);
		jp = new JPanel();
		jl = new JLabel("请正确输入信息");
		jp.add(jl);
		jb = new JButton("确定");
		jb.addActionListener(this);
		jp.add(jb);
		this.add(jp);
		this.setVisible(true);
	}

	/*
	 * public void noNull(JDialog owner,String title,boolean modal)
	 * //构造函数不能用void，否则会出现一个诡异的错误 { super(owner,title,modal); jp = new JPanel();
	 * jl = new JLabel("请正确输入信息"); jp.add(jl); jb = new JButton("确定");
	 * jb.addActionListener(this); jp.add(jb); }
	 */

	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getSource() == jb)
		{
			this.dispose();
		}

	}

}
