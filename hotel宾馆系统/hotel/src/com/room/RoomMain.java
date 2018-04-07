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
 * ���һ��min���ѧ������ϵͳ ����model2ģʽ 1.��ѯ 2.���
 * 
 * @author jiao
 * 
 */
public class RoomMain extends JPanel implements ActionListener
{

	// ����һЩ�ؼ�
	JPanel jp1, jp2, jp3;
	JLabel jl1, jl2;
	JButton jb1, jb2, jb3, jb4, jb5;
	JTable jt;
	JScrollPane jsp;
	JTextField jtf1, jtf2;
	RoomModel sm;

	// ���캯��
	public RoomMain()
	{
		jp1 = new JPanel();
		jtf1 = new JTextField(10);
		jb1 = new JButton("��ѯ");
		jb1.addActionListener(this);
		jl1 = new JLabel("�����");
		jtf2 = new JTextField(10);
		jb5 = new JButton("��ѯ");
		jb5.addActionListener(this);
		jl2 = new JLabel("��̬");

		// �Ѹ����ؼ����뵽��������
		jp1.add(jl1);
		jp1.add(jtf1);
		jp1.add(jb1);
		jp1.add(jl2);
		jp1.add(jtf2);
		jp1.add(jb5);

		jp2 = new JPanel();

		jb2 = new JButton("���");
		jb2.addActionListener(this);
		jb3 = new JButton("�޸�");
		jb3.addActionListener(this);
		jb4 = new JButton("ɾ��");
		jb4.addActionListener(this);

		// �Ѹ�����ť���뵽jp2
		jp2.add(jb2);
		jp2.add(jb3);
		jp2.add(jb4);

		// �м�

		// ����һ������ģ�Ͷ���
		sm = new RoomModel();
		String[] paras =
		{ "1" };

		sm.queryRoom("select *from room where 1=?", paras);

		// ��ʼ��JTable
		jt = new JTable(sm);
		// ��ʼ��jsp JSrollPane
		jsp = new JScrollPane(jt);
		// ��jsp�ŵ�Jframe
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
		// �������ݿ�ģ��
		sm = new RoomModel();
		String[] paras2 =
		{ "1" };
		sm.queryRoom("select * from room where 1=?", paras2);
		// ����JTable
		jt.setModel(sm);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		// �ж����ĸ���ť�����
		if (e.getSource() == jb1)
		{
			// System.out.println("�û�ϣ����ѯ");
			// ��Ϊ�ѶԱ�����ݷ�װ��stuModel�У����ǾͿ��ԱȽϼ򵥵���ɲ�ѯ��Ϊ
			String roomNo = this.jtf1.getText().trim();
			// дһ��sql���

			String sql = "select* from room where roomNo=?";

			String paras[] =
			{ roomNo };
			System.out.print(paras[0]);
			// System.out.println(sql);
			// �����µ�����ģ���࣬����
			if (roomNo.equals(""))
			{
				updaterModel();
			} else
			{
				sm = new RoomModel();

				sm.queryRoom(sql, paras);
				// System.out.print(sql);
				// ����JTable
				jt.setModel(sm);
			}

		} else if (e.getSource() == jb5)
		{
			// System.out.println("�û�ϣ����ѯ");
			// ��Ϊ�ѶԱ�����ݷ�װ��stuModel�У����ǾͿ��ԱȽϼ򵥵���ɲ�ѯ��Ϊ
			String status = this.jtf2.getText().trim();
			// дһ��sql���

			String sql = "select* from room where status=?";

			String paras[] =
			{ status };

			System.out.print(paras[0]);
			// System.out.println(sql);
			// �����µ�����ģ���࣬����
			if (status.equals(""))
			{
				updaterModel();
			} else
			{
				sm = new RoomModel();

				sm.queryRoom(sql, paras);
				// System.out.print(sql);
				// ����JTable
				jt.setModel(sm);
			}
		}

		// ���û�������
		else if (e.getSource() == jb2)
		{
			RoomAdd sa = new RoomAdd(Windows1.w1, "���", true);
			// ���»���µ�����ģ��

			// �����µ�����ģ���࣬����
			updaterModel();
			// ����JTable

		} else if (e.getSource() == jb3)
		{
			// �û�ϣ���޸�
			int rowNum = this.jt.getSelectedRow();// �����û����е��У����һ��ûѡ����-1
			if (rowNum == -1)
			{
				// ��ʾ
				JOptionPane.showMessageDialog(this, "��ѡ���޸���");
				return;
			}
			// ��ʾ�޸ĶԻ���
			new RoomUpdate(Windows1.w1, "�޸�", true, sm, rowNum);
			// �����µ�����ģ���࣬����
			updaterModel();
			// ����JTable
			jt.setModel(sm);
		}

		else if (e.getSource() == jb4)
		{
			// ˵���û�ϣ��ɾ����¼
			// 1.�õ���ѧ����id��

			int rowNum = this.jt.getSelectedRow();// �����û����е��У����һ��ûѡ����-1
			if (rowNum == -1)
			{
				// ��ʾ
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
				return;
			}

			// �õ�����ı��
			String roomNo = (String) sm.getValueAt(rowNum, 0);
			System.out.print(roomNo);
			// ����һ��sql
			String sql = "delete from room where roomNo=?";
			String[] paras =
			{ roomNo };
			RoomModel temp = new RoomModel();
			temp.updateRoom(sql, paras);
			updaterModel();

		}

	}

}
