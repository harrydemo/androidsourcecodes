package com.client;

import com.Model.ClieModel;
import com.Model.RoomModel;
import com.face.Windows1;
import com.room.*;
import java.sql.*;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 完成一个min版的学生管理系统 此乃model2模式 1.查询 2.添加
 * 
 * @author jiao
 * 
 */
public class ClieMain extends JPanel implements ActionListener
{

	// 定义一些控件
	JPanel jp1, jp2;
	JLabel jl1, jl2;
	JButton jb1, jb2, jb3, jb4, jb5, jb6;// 1.查询姓名2.添加3.修改4.删除5.查询房间号6.结算
	JTable jt;
	JScrollPane jsp;
	JTextField jtf1, jtf2;
	ClieModel sm;
	RoomModel rm;

	// 构造函数
	public ClieMain()
	{
		jp1 = new JPanel();
		jtf1 = new JTextField(10);
		jb1 = new JButton("查询");
		jb1.addActionListener(this);
		jl1 = new JLabel("请输入名字");
		jtf2 = new JTextField(10);
		jb5 = new JButton("查询");
		jb5.addActionListener(this);
		jl2 = new JLabel("请输入房间号");

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
		jb6 = new JButton("结算");
		jb6.addActionListener(this);

		// 把各个按钮加入到jp2
		jp2.add(jb2);
		jp2.add(jb3);
		jp2.add(jb4);
		jp2.add(jb6);
		// 中间

		// 创建一个数据模型对象
		sm = new ClieModel();
		String[] paras =
		{ "1" };

		sm.queryClie("select *from client where 1=?", paras);

		// 初始化JTable
		jt = new JTable(sm);
		// 初始化jsp JSrollPane
		jsp = new JScrollPane(jt);
		// 把jsp放到Jframe
		this.setLayout(new BorderLayout());

		this.add(jsp, "Center");
		this.add(jp1, "North");
		this.add(jp2, "South");
		this.setSize(600, 300);
		// this.setDefaultCloseOperation(.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	// 查询所有，更新
	public void updateModel()
	{
		// 更新数据库模型
		sm = new ClieModel();
		String[] paras2 =
		{ "1" };
		sm.queryClie("select * from client where 1=?", paras2);
		// 更新JTable
		jt.setModel(sm);
	}

	/*
	 * 
	 * public static void main(String[] args) { // TODO Auto-generated method
	 * stub ClieMain test3=new ClieMain();
	 * 
	 * }
	 */

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		// 判断是哪个按钮被点击
		if (e.getSource() == jb1)
		{

			// System.out.println("用户希望查询");
			// 因为把对表的数据封装到stuModel中，我们就可以比较简单的完成查询认为
			String cName = this.jtf1.getText().trim();
			// 写一个sql语句
			String sql;
			if (cName == null)
			{
				sql = "select* from client where";
			} else
			{
				sql = "select* from client where cName=?";
			}
			// String sql="select* from client where cName=?";

			String paras[] =
			{ cName };

			System.out.print(paras[0]);
			// System.out.println(sql);
			// 构建新的数据模型类，并更
			if (cName.equals(""))
			{
				updateModel();
			} else
			{
				sm = new ClieModel();

				sm.queryClie(sql, paras);
				// System.out.print(sql);
				// 更新JTable
				jt.setModel(sm);
			}

		} else if (e.getSource() == jb5)
		{
			System.out.println("用户希望查询");
			// 因为把对表的数据封装到stuModel中，我们就可以比较简单的完成查询认为
			String rNo = this.jtf2.getText().trim();
			// 写一个sql语句

			String sql = "select* from client where rNo=?";

			String paras[] =
			{ rNo };

			// System.out.print(paras[0]);
			// System.out.println(sql);
			// 构建新的数据模型类，并更
			if (rNo.equals(""))
			{
				updateModel();
			} else
			{
				sm = new ClieModel();

				sm.queryClie(sql, paras);
				// System.out.print(sql);
				// 更新JTable
				jt.setModel(sm);
			}

		}

		// 当用户点击添加
		else if (e.getSource() == jb2)
		{
			ClieAdd sa = new ClieAdd(Windows1.w1, "添加", true);
			// 重新获得新的数据模型

			// 构建新的数据模型类，并更
			updateModel();
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
			new ClieUpdate(Windows1.w1, "修改", true, sm, rowNum);
			// 构建新的数据模型类，并更
			updateModel();
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

			// 得到学生的编号
			String cId = (String) sm.getValueAt(rowNum, 0);

			// 创建一个sql
			String sql = "delete from client where cId=?";
			String[] paras =
			{ cId };
			ClieModel temp = new ClieModel();
			temp.updateClie(sql, paras);
			updateModel();
		}

		//
		// else if(e.getSource()==jb6)
		// {
		// //用户希望修改
		// int rowNum=this.jt.getSelectedRow();//返回用户点中的行，如果一行没选返回-1
		// if(rowNum==-1)
		// {
		// //提示
		// JOptionPane.showMessageDialog(this, "请选择对象");
		// return ;
		// }
		//
		// PreparedStatement ps=null;
		// Connection ct=null;
		// ResultSet rs=null;
		// // Statement st=null;
		//
		// try {
		//
		// //连接数据库，判断用户是否合法
		// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		// ct=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant","sa","root");
		// //与编译语句对象
		// String
		// sql="update client set num=(select datediff(day,(select startT from client where cId=?),GETDATE())+1)"
		// +
		// "where cId=? ";
		// ps=ct.prepareStatement(sql);
		//
		// //给参数赋值
		// String cId=(String)sm.getValueAt(rowNum, 0);
		// ps.setString(1,cId);
		// ps.setString(2,cId);
		// ps.executeUpdate();
		// int money;
		// int price;
		// int num;
		// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		// ct=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant","sa","881221");
		// //与编译语句对象
		// sql="select price from room where roomNo=?";
		// ps=ct.prepareStatement(sql);
		//
		// num=Integer.parseInt((String)sm.getValueAt(rowNum, 11));
		// System.out.print(num);
		//
		//
		//
		//
		// //给参数赋值
		// String rNo=(String)sm.getValueAt(rowNum, 7);
		// ps.setString(1, rNo);
		// ps.executeQuery();
		//
		//
		//
		// updateModel();
		//
		// // System.out.print(rNo);
		//
		//
		// // System.out.print((String)rs.getString(0));
		// price=Integer.parseInt((String)rs.getString(3));/*就是这句有问题啊。。。你看看怎么搞啊。。无语了*/
		// System.out.print(price);
		// money=price*num;
		// System.out.print(money);
		//
		//
		// //4执行操作
		//
		// // this.dispose();//关闭对话框
		// } catch (ClassNotFoundException e1) {
		// // TODO: handle exception
		// e1.printStackTrace();
		// } catch (SQLException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }finally{
		// //释放语句对象，连接对象
		// try {
		// if(rs!=null)rs.close();
		// if(ps!=null)ps.close();
		// if(ct!=null)ct.close();
		//
		// } catch (Exception ex) {
		// // TODO: handle exception
		// ex.printStackTrace();
		// }
		//
		// }
		//
		//
		//
		// // String cId=(String)sm.getValueAt(rowNum, 0);
		// // String sql="delete from client where cId=?";
		// // String[] paras={cId};
		// // ClieModel temp=new ClieModel();
		// // temp.updateClie(sql, paras);
		// // updateModel();
		// //显示修改对话框
		// // new ClieUpdate(this,"修改",true,sm,rowNum);
		// //构建新的数据模型类，并更
		// updateModel();
		//
		// }
		//
		//
		//

		else if (e.getSource() == jb6)
		{
			// 用户希望修改
			int rowNum = this.jt.getSelectedRow();// 返回用户点中的行，如果一行没选返回-1
			if (rowNum == -1)
			{
				// 提示
				JOptionPane.showMessageDialog(this, "请选择对象");
				return;
			}

			PreparedStatement ps = null;
			Connection ct = null;
			ResultSet rs = null, rs1 = null;
			// Statement st=null;

			try
			{

				// 连接数据库，判断用户是否合法
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				ct = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant", "sa", "root");
				// 与编译语句对象
				String sql = "update client set num=(select datediff(day,(select startT from client where cId=?),GETDATE())+1)" + "where cId=? ";
				ps = ct.prepareStatement(sql);

				// 给参数赋值
				String cId = (String) sm.getValueAt(rowNum, 0);
				ps.setString(1, cId);
				ps.setString(2, cId);
				ps.executeUpdate();
				int money = 0;
				int price = 0;
				int num = 0;
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				ct = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant", "sa", "root");
				// 与编译语句对象

				num = Integer.parseInt((String) sm.getValueAt(rowNum, 11));
				System.out.print(num);
				sql = "select price from room where roomNo=?";
				ps = ct.prepareStatement(sql);

				// 给参数赋值
				String rNo = (String) sm.getValueAt(rowNum, 7);
				System.out.print(rNo);
				ps.setString(1, rNo);
				ps.executeQuery();
				updateModel();

				if (rNo.equals("A1") || rNo.equals("A2") || rNo.equals("A3") || rNo.equals("A4"))
					price = 238;
				if (rNo.equals("B1") || rNo.equals("B2") || rNo.equals("B3") || rNo.equals("B4"))
					price = 258;
				if (rNo.equals("C1") || rNo.equals("C2") || rNo.equals("C3"))
					price = 278;
				if (rNo.equals("D1"))
					price = 238;

				money = num * price;
				String sql2 = "update client  set  money=? where rNo=?";
				String[] paras =
				{ money + "", rNo };

				ClieModel temp = new ClieModel();
				temp.updateClie(sql2, paras);
				updateModel();

				JOptionPane.showMessageDialog(this, "你所需付费" + money);

				// 4执行操作

				// this.dispose();//关闭对话框
			} catch (ClassNotFoundException e1)
			{
				// TODO: handle exception
				e1.printStackTrace();
			} catch (SQLException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally
			{
				// 释放语句对象，连接对象
				try
				{
					if (rs != null)
						rs.close();
					if (ps != null)
						ps.close();
					if (ct != null)
						ct.close();

				} catch (Exception ex)
				{
					// TODO: handle exception
					ex.printStackTrace();
				}

			}

			// String cId=(String)sm.getValueAt(rowNum, 0);
			// String sql="delete from client where cId=?";
			// String[] paras={cId};
			// ClieModel temp=new ClieModel();
			// temp.updateClie(sql, paras);
			// updateModel();
			// 显示修改对话框
			// new ClieUpdate(this,"修改",true,sm,rowNum);
			// 构建新的数据模型类，并更
			updateModel();

		}

	}

}
