package com.room;

import java.sql.*;
import javax.swing.*;

import com.Model.RoomModel;
import com.face.Windows1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 完成一个min版的学生管理系统 此乃model2模式 1.查询 2.添加
 * 
 * @author jiao
 * 
 */
public class RoomMain extends JPanel implements ActionListener
{

	// 定义一些控件
	JPanel jp1, jp2, jp3;
	JLabel jl1, jl2;
	JButton jb1, jb2, jb3, jb4, jb5;
	JTable jt;
	JScrollPane jsp;
	JTextField jtf1, jtf2;
	RoomModel sm;

	// 构造函数
	public RoomMain()
	{
		jp1 = new JPanel();
		jtf1 = new JTextField(10);
		jb1 = new JButton("查询");
		jb1.addActionListener(this);
		jl1 = new JLabel("房间号");
		jtf2 = new JTextField(10);
		jb5 = new JButton("查询");
		jb5.addActionListener(this);
		jl2 = new JLabel("房态");

		// 把各个控件加入到流布局中
		jp1.add(jl1);
		jp1.add(jtf1);
		jp1.add(jb1);
		jp1.add(jl2);
		jp1.add(jtf2);
		jp1.add(jb5);

		jp2 = new JPanel();

		jb2 = new JButton("添加");
		jb2.addActionListener(this);
		jb3 = new JButton("修改");
		jb3.addActionListener(this);
		jb4 = new JButton("删除");
		jb4.addActionListener(this);

		// 把各个按钮加入到jp2
		jp2.add(jb2);
		jp2.add(jb3);
		jp2.add(jb4);

		// 中间

		// 创建一个数据模型对象
		sm = new RoomModel();
		String[] paras =
		{ "1" };

		sm.queryRoom("select *from room where 1=?", paras);

		// 初始化JTable
		jt = new JTable(sm);
		// 初始化jsp JSrollPane
		jsp = new JScrollPane(jt);
		// 把jsp放到Jframe
		jp3 = new JPanel();
		jp3.add(jsp);

		this.setLayout(new BorderLayout());
		this.add(jp1, "North");
		this.add(jsp, "Center");
		// this.add(jp3,"Center");
		this.add(jp2, "South");
		this.setSize(500, 400);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		RoomMain test3 = new RoomMain();

	}

	public void updaterModel()
	{
		// 更新数据库模型
		sm = new RoomModel();
		String[] paras2 =
		{ "1" };
		sm.queryRoom("select * from room where 1=?", paras2);
		// 更新JTable
		jt.setModel(sm);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		// 判断是哪个按钮被点击
		if (e.getSource() == jb1)
		{
			// System.out.println("用户希望查询");
			// 因为把对表的数据封装到stuModel中，我们就可以比较简单的完成查询认为
			String roomNo = this.jtf1.getText().trim();
			// 写一个sql语句

			String sql = "select* from room where roomNo=?";

			String paras[] =
			{ roomNo };
			System.out.print(paras[0]);
			// System.out.println(sql);
			// 构建新的数据模型类，并更
			if (roomNo.equals(""))
			{
				updaterModel();
			} else
			{
				sm = new RoomModel();

				sm.queryRoom(sql, paras);
				// System.out.print(sql);
				// 更新JTable
				jt.setModel(sm);
			}

		} else if (e.getSource() == jb5)
		{
			// System.out.println("用户希望查询");
			// 因为把对表的数据封装到stuModel中，我们就可以比较简单的完成查询认为
			String status = this.jtf2.getText().trim();
			// 写一个sql语句

			String sql = "select* from room where status=?";

			String paras[] =
			{ status };

			System.out.print(paras[0]);
			// System.out.println(sql);
			// 构建新的数据模型类，并更
			if (status.equals(""))
			{
				updaterModel();
			} else
			{
				sm = new RoomModel();

				sm.queryRoom(sql, paras);
				// System.out.print(sql);
				// 更新JTable
				jt.setModel(sm);
			}
		}

		// 当用户点击添加
		else if (e.getSource() == jb2)
		{
			RoomAdd sa = new RoomAdd(Windows1.w1, "添加", true);
			// 重新获得新的数据模型

			// 构建新的数据模型类，并更
			updaterModel();
			// 更新JTable

		} else if (e.getSource() == jb3)
		{
			// 用户希望修改
			int rowNum = this.jt.getSelectedRow();// 返回用户点中的行，如果一行没选返回-1
			if (rowNum == -1)
			{
				// 提示
				JOptionPane.showMessageDialog(this, "请选择修改行");
				return;
			}
			// 显示修改对话框
			new RoomUpdate(Windows1.w1, "修改", true, sm, rowNum);
			// 构建新的数据模型类，并更
			updaterModel();
			// 更新JTable
			jt.setModel(sm);
		}

		else if (e.getSource() == jb4)
		{
			// 说明用户希望删除记录
			// 1.得到该学生的id号

			int rowNum = this.jt.getSelectedRow();// 返回用户点中的行，如果一行没选返回-1
			if (rowNum == -1)
			{
				// 提示
				JOptionPane.showMessageDialog(this, "请选择一行");
				return;
			}

			// 得到房间的编号
			String roomNo = (String) sm.getValueAt(rowNum, 0);
			System.out.print(roomNo);
			// 创建一个sql
			String sql = "delete from room where roomNo=?";
			String[] paras =
			{ roomNo };
			RoomModel temp = new RoomModel();
			temp.updateRoom(sql, paras);
			updaterModel();

		}

	}

}
