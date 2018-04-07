/**
 * 显示人事管理的界面
 */
package com.face;

//import com.mysql.jdbc.ResultSet;
import com.Model.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

import com.Tools.MyTools;
import com.face.*;
import java.sql.*;

public class EmpInfo extends JPanel implements ActionListener
{
	// public class EmpInfo extends JFrame implements ActionListener{

	JPanel p1, p2, p3, p4, p5;
	JLabel p1_lab1, p3_lab1;
	JTextField p1_jtf1, p3_jtf;// p3_jtf里面装记录数
	JButton p1_jb1, p4_jb1, p4_jb2, p4_jb3, p4_jb4;
	JTable jtable;// 用于显示人事信息的table
	JScrollPane jsp;// 必须将表格放到一个滚动的Panel里面才能看到列名

	EmpModel em;

	// java.sql.ResultSet rs1;//从结果集中可以统计出有多少条记录
	ResultSet rs1;

	int count;// 结果集中的记录数

	// 构造函数
	public EmpInfo()
	{
		// 创建组件(顺序：从上到下，从左到右)
		// 首先处理最上面的

		p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));// 流布局分为：左、中、右
		p1_lab1 = new JLabel("请输入姓名（员工号或职位）");
		p1_lab1.setFont(MyTools.f2);
		p1_jtf1 = new JTextField(20);
		p1_jtf1.setBorder(BorderFactory.createLoweredBevelBorder());// 设置下凹
		p1_jb1 = new JButton("查询");
		p1_jb1.addActionListener(this);
		p1_jb1.setFont(MyTools.f2);// 必须用滚动面板才能显示列名
		// 将以上组件加入到面板p1
		p1.add(p1_lab1);
		p1.add(p1_jtf1);
		p1.add(p1_jb1);

		// 处理中间的
		em = new EmpModel();
		String[] paras =
		{ "1" };
		// em.query("select * from renshiziliao where 1 = ? ", paras);//where 1
		// = ?不能写作where1 = ?否则会报错

		em.query("select  userid , username  ,sex, birth, id , xueli ," + " married , phone1, phone2 ,youxiang, zhuceshijian, " + "beizu  ,zhiwei   from renshiziliao where 1 = ? ", paras);
		count = em.count;
		// System.out.print("     在EmpInfo中的信息  共有"+count+"条记录");

		jtable = new JTable(em);
		jsp = new JScrollPane(jtable);
		p2 = new JPanel(new BorderLayout());
		p2.add(jsp);
		p2.setBorder(BorderFactory.createLoweredBevelBorder());
		/*
		 * try { while(rs1.last()) { count = rs1.getRow();
		 * System.out.print("共有"+count+"条记录"); } } catch (SQLException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */

		// 处理南面的
		p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p3_jtf = new JTextField(10);
		p3_jtf.setText(Integer.toString(count));// 此句用到将整形转换成String
		p3_jtf.setEditable(false);
		p3_lab1 = new JLabel("员工表中的总记录数: ");
		p3_lab1.setFont(MyTools.f2);
		p3.add(p3_lab1);
		p3.add(p3_jtf);

		p4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p4_jb1 = new JButton("详细信息");
		p4_jb1.addActionListener(this);
		p4_jb1.setFont(MyTools.f2);
		p4_jb2 = new JButton("添加");
		p4_jb2.addActionListener(this);
		p4_jb2.setFont(MyTools.f2);
		p4_jb3 = new JButton("修改");
		p4_jb3.addActionListener(this);
		p4_jb3.setFont(MyTools.f2);
		p4_jb4 = new JButton("删除");
		p4_jb4.addActionListener(this);
		p4_jb4.setFont(MyTools.f2);
		p4.add(p4_jb1);
		p4.add(p4_jb2);
		p4.add(p4_jb3);
		p4.add(p4_jb4);
		p5 = new JPanel(new BorderLayout());
		p5.add(p3, "West");
		p5.add(p4, "East");

		// 把总的JPanel设成BorderLayout因为默认的是流布局，回事的本应该呆在南方的p5面板跑到查询按钮的后面
		this.setLayout(new BorderLayout());
		// 把p1加入到总的JPanel中
		this.add(p1, "North");
		this.add(p2, "Center");
		this.add(p5, "South");

		// this.setBackground(Color.pink);//设置背景为粉红色
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{

		if (arg0.getSource() == p1_jb1)// 查询员工信息
		{
			String name = this.p1_jtf1.getText().trim();
			System.out.println("输入的用户名：" + name);
			if (name.equals(""))
			{
				em = new EmpModel();
				String paras1[] =
				{ "1" };
				em.query("select * from renshiziliao where 1 = ?", paras1);

				count = em.count;
				// System.out.print("     在EmpInfo中的信息  共有"+count+"条记录");
				p3_jtf.setText(Integer.toString(count));// 此句用到将整形转换成String

				jtable.setModel(em); // 更新jtable
			} else
			{
				String[] paras2 =
				{ name };
				String sql = "select * from renshiziliao where username = ?";
				EmpModel em = new EmpModel();
				em.query(sql, paras2);
				jtable.setModel(em);
			}
		} else if (arg0.getSource() == p4_jb1)
		{

		}

		else if (arg0.getSource() == p4_jb2)// 新增加员工
		{
			EmpAddDialog ea = new EmpAddDialog(Windows1.w1, "添加员工", true);

			// EmpAddDialog eu = new EmpAddDialog();
			em = new EmpModel();
			String paras1[] =
			{ "1" };
			em.query("select * from renshiziliao where 1 = ?", paras1);

			count = em.count;
			System.out.print("     在EmpInfo中的信息  共有" + count + "条记录");
			p3_jtf.setText(Integer.toString(count));// 此句用到将整形转换成String

			jtable.setModel(em);
			// JOptionPane.showMessageDialog(this, "添加员工成功");
		}

		else if (arg0.getSource() == p4_jb3)// 修改员工
		{
			int rowNum = this.jtable.getSelectedRow();
			System.out.println("选择的行号为：" + rowNum);
			if (rowNum == -1)
			{
				JOptionPane.showMessageDialog(this, "请选择一行");
				return;
			}
			new EmpUpdDialog(Windows1.w1, "修改员工", true, em, rowNum);

			em = new EmpModel();
			String[] paras2 =
			{ "1" };
			em.query("select * from renshiziliao where 1 = ?", paras2);
			jtable.setModel(em);
		}

		else if (arg0.getSource() == p4_jb4) // 删除员工
		{

			int rowNum = this.jtable.getSelectedRow();
			if (rowNum == -1)
			{
				JOptionPane.showMessageDialog(this, "请选择将要删除的记录");
				return;
			}
			// 要删除员工，首先要获得其员工号
			String userid = (String) em.getValueAt(rowNum, 0);
			String[] paras =
			{ userid };
			String sql = "delete from renshiziliao where userid = ?";
			EmpModel temp = new EmpModel();
			temp.updEmp(sql, paras);

			// 执行后，重新查询数据表，达到刷新信息的目的
			em = new EmpModel();
			String[] paras2 =
			{ "1" };
			em.query("select * from renshiziliao where 1 = ?", paras2);

			count = em.count;
			System.out.print("     在EmpInfo中的信息  共有" + count + "条记录");
			p3_jtf.setText(Integer.toString(count));// 此句用到将整形转换成String

			jtable.setModel(em);
			JOptionPane.showMessageDialog(this, "删除员工成功");
		}

	}
}
