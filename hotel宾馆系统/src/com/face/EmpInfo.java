/**
 * ��ʾ���¹���Ľ���
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
	JTextField p1_jtf1, p3_jtf;// p3_jtf����װ��¼��
	JButton p1_jb1, p4_jb1, p4_jb2, p4_jb3, p4_jb4;
	JTable jtable;// ������ʾ������Ϣ��table
	JScrollPane jsp;// ���뽫���ŵ�һ��������Panel������ܿ�������

	EmpModel em;

	// java.sql.ResultSet rs1;//�ӽ�����п���ͳ�Ƴ��ж�������¼
	ResultSet rs1;

	int count;// ������еļ�¼��

	// ���캯��
	public EmpInfo()
	{
		// �������(˳�򣺴��ϵ��£�������)
		// ���ȴ����������

		p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));// �����ַ�Ϊ�����С���
		p1_lab1 = new JLabel("������������Ա���Ż�ְλ��");
		p1_lab1.setFont(MyTools.f2);
		p1_jtf1 = new JTextField(20);
		p1_jtf1.setBorder(BorderFactory.createLoweredBevelBorder());// �����°�
		p1_jb1 = new JButton("��ѯ");
		p1_jb1.addActionListener(this);
		p1_jb1.setFont(MyTools.f2);// �����ù�����������ʾ����
		// ������������뵽���p1
		p1.add(p1_lab1);
		p1.add(p1_jtf1);
		p1.add(p1_jb1);

		// �����м��
		em = new EmpModel();
		String[] paras =
		{ "1" };
		// em.query("select * from renshiziliao where 1 = ? ", paras);//where 1
		// = ?����д��where1 = ?����ᱨ��

		em.query("select  userid , username  ,sex, birth, id , xueli ," + " married , phone1, phone2 ,youxiang, zhuceshijian, " + "beizu  ,zhiwei   from renshiziliao where 1 = ? ", paras);
		count = em.count;
		// System.out.print("     ��EmpInfo�е���Ϣ  ����"+count+"����¼");

		jtable = new JTable(em);
		jsp = new JScrollPane(jtable);
		p2 = new JPanel(new BorderLayout());
		p2.add(jsp);
		p2.setBorder(BorderFactory.createLoweredBevelBorder());
		/*
		 * try { while(rs1.last()) { count = rs1.getRow();
		 * System.out.print("����"+count+"����¼"); } } catch (SQLException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */

		// ���������
		p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p3_jtf = new JTextField(10);
		p3_jtf.setText(Integer.toString(count));// �˾��õ�������ת����String
		p3_jtf.setEditable(false);
		p3_lab1 = new JLabel("Ա�����е��ܼ�¼��: ");
		p3_lab1.setFont(MyTools.f2);
		p3.add(p3_lab1);
		p3.add(p3_jtf);

		p4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p4_jb1 = new JButton("��ϸ��Ϣ");
		p4_jb1.addActionListener(this);
		p4_jb1.setFont(MyTools.f2);
		p4_jb2 = new JButton("���");
		p4_jb2.addActionListener(this);
		p4_jb2.setFont(MyTools.f2);
		p4_jb3 = new JButton("�޸�");
		p4_jb3.addActionListener(this);
		p4_jb3.setFont(MyTools.f2);
		p4_jb4 = new JButton("ɾ��");
		p4_jb4.addActionListener(this);
		p4_jb4.setFont(MyTools.f2);
		p4.add(p4_jb1);
		p4.add(p4_jb2);
		p4.add(p4_jb3);
		p4.add(p4_jb4);
		p5 = new JPanel(new BorderLayout());
		p5.add(p3, "West");
		p5.add(p4, "East");

		// ���ܵ�JPanel���BorderLayout��ΪĬ�ϵ��������֣����µı�Ӧ�ô����Ϸ���p5����ܵ���ѯ��ť�ĺ���
		this.setLayout(new BorderLayout());
		// ��p1���뵽�ܵ�JPanel��
		this.add(p1, "North");
		this.add(p2, "Center");
		this.add(p5, "South");

		// this.setBackground(Color.pink);//���ñ���Ϊ�ۺ�ɫ
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{

		if (arg0.getSource() == p1_jb1)// ��ѯԱ����Ϣ
		{
			String name = this.p1_jtf1.getText().trim();
			System.out.println("������û�����" + name);
			if (name.equals(""))
			{
				em = new EmpModel();
				String paras1[] =
				{ "1" };
				em.query("select * from renshiziliao where 1 = ?", paras1);

				count = em.count;
				// System.out.print("     ��EmpInfo�е���Ϣ  ����"+count+"����¼");
				p3_jtf.setText(Integer.toString(count));// �˾��õ�������ת����String

				jtable.setModel(em); // ����jtable
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

		else if (arg0.getSource() == p4_jb2)// ������Ա��
		{
			EmpAddDialog ea = new EmpAddDialog(Windows1.w1, "���Ա��", true);

			// EmpAddDialog eu = new EmpAddDialog();
			em = new EmpModel();
			String paras1[] =
			{ "1" };
			em.query("select * from renshiziliao where 1 = ?", paras1);

			count = em.count;
			System.out.print("     ��EmpInfo�е���Ϣ  ����" + count + "����¼");
			p3_jtf.setText(Integer.toString(count));// �˾��õ�������ת����String

			jtable.setModel(em);
			// JOptionPane.showMessageDialog(this, "���Ա���ɹ�");
		}

		else if (arg0.getSource() == p4_jb3)// �޸�Ա��
		{
			int rowNum = this.jtable.getSelectedRow();
			System.out.println("ѡ����к�Ϊ��" + rowNum);
			if (rowNum == -1)
			{
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
				return;
			}
			new EmpUpdDialog(Windows1.w1, "�޸�Ա��", true, em, rowNum);

			em = new EmpModel();
			String[] paras2 =
			{ "1" };
			em.query("select * from renshiziliao where 1 = ?", paras2);
			jtable.setModel(em);
		}

		else if (arg0.getSource() == p4_jb4) // ɾ��Ա��
		{

			int rowNum = this.jtable.getSelectedRow();
			if (rowNum == -1)
			{
				JOptionPane.showMessageDialog(this, "��ѡ��Ҫɾ���ļ�¼");
				return;
			}
			// Ҫɾ��Ա��������Ҫ�����Ա����
			String userid = (String) em.getValueAt(rowNum, 0);
			String[] paras =
			{ userid };
			String sql = "delete from renshiziliao where userid = ?";
			EmpModel temp = new EmpModel();
			temp.updEmp(sql, paras);

			// ִ�к����²�ѯ���ݱ��ﵽˢ����Ϣ��Ŀ��
			em = new EmpModel();
			String[] paras2 =
			{ "1" };
			em.query("select * from renshiziliao where 1 = ?", paras2);

			count = em.count;
			System.out.print("     ��EmpInfo�е���Ϣ  ����" + count + "����¼");
			p3_jtf.setText(Integer.toString(count));// �˾��õ�������ת����String

			jtable.setModel(em);
			JOptionPane.showMessageDialog(this, "ɾ��Ա���ɹ�");
		}

	}
}
