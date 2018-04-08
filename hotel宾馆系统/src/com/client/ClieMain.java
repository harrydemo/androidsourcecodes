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
 * ���һ��min���ѧ������ϵͳ ����model2ģʽ 1.��ѯ 2.���
 * 
 * @author jiao
 * 
 */
public class ClieMain extends JPanel implements ActionListener
{

	// ����һЩ�ؼ�
	JPanel jp1, jp2;
	JLabel jl1, jl2;
	JButton jb1, jb2, jb3, jb4, jb5, jb6;// 1.��ѯ����2.���3.�޸�4.ɾ��5.��ѯ�����6.����
	JTable jt;
	JScrollPane jsp;
	JTextField jtf1, jtf2;
	ClieModel sm;
	RoomModel rm;

	// ���캯��
	public ClieMain()
	{
		jp1 = new JPanel();
		jtf1 = new JTextField(10);
		jb1 = new JButton("��ѯ");
		jb1.addActionListener(this);
		jl1 = new JLabel("����������");
		jtf2 = new JTextField(10);
		jb5 = new JButton("��ѯ");
		jb5.addActionListener(this);
		jl2 = new JLabel("�����뷿���");

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
		jb6 = new JButton("����");
		jb6.addActionListener(this);

		// �Ѹ�����ť���뵽jp2
		jp2.add(jb2);
		jp2.add(jb3);
		jp2.add(jb4);
		jp2.add(jb6);
		// �м�

		// ����һ������ģ�Ͷ���
		sm = new ClieModel();
		String[] paras =
		{ "1" };

		sm.queryClie("select *from client where 1=?", paras);

		// ��ʼ��JTable
		jt = new JTable(sm);
		// ��ʼ��jsp JSrollPane
		jsp = new JScrollPane(jt);
		// ��jsp�ŵ�Jframe
		this.setLayout(new BorderLayout());

		this.add(jsp, "Center");
		this.add(jp1, "North");
		this.add(jp2, "South");
		this.setSize(600, 300);
		// this.setDefaultCloseOperation(.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	// ��ѯ���У�����
	public void updateModel()
	{
		// �������ݿ�ģ��
		sm = new ClieModel();
		String[] paras2 =
		{ "1" };
		sm.queryClie("select * from client where 1=?", paras2);
		// ����JTable
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
		// �ж����ĸ���ť�����
		if (e.getSource() == jb1)
		{

			// System.out.println("�û�ϣ����ѯ");
			// ��Ϊ�ѶԱ�����ݷ�װ��stuModel�У����ǾͿ��ԱȽϼ򵥵���ɲ�ѯ��Ϊ
			String cName = this.jtf1.getText().trim();
			// дһ��sql���
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
			// �����µ�����ģ���࣬����
			if (cName.equals(""))
			{
				updateModel();
			} else
			{
				sm = new ClieModel();

				sm.queryClie(sql, paras);
				// System.out.print(sql);
				// ����JTable
				jt.setModel(sm);
			}

		} else if (e.getSource() == jb5)
		{
			System.out.println("�û�ϣ����ѯ");
			// ��Ϊ�ѶԱ�����ݷ�װ��stuModel�У����ǾͿ��ԱȽϼ򵥵���ɲ�ѯ��Ϊ
			String rNo = this.jtf2.getText().trim();
			// дһ��sql���

			String sql = "select* from client where rNo=?";

			String paras[] =
			{ rNo };

			// System.out.print(paras[0]);
			// System.out.println(sql);
			// �����µ�����ģ���࣬����
			if (rNo.equals(""))
			{
				updateModel();
			} else
			{
				sm = new ClieModel();

				sm.queryClie(sql, paras);
				// System.out.print(sql);
				// ����JTable
				jt.setModel(sm);
			}

		}

		// ���û�������
		else if (e.getSource() == jb2)
		{
			ClieAdd sa = new ClieAdd(Windows1.w1, "���", true);
			// ���»���µ�����ģ��

			// �����µ�����ģ���࣬����
			updateModel();
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
			new ClieUpdate(Windows1.w1, "�޸�", true, sm, rowNum);
			// �����µ�����ģ���࣬����
			updateModel();
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

			// �õ�ѧ���ı��
			String cId = (String) sm.getValueAt(rowNum, 0);

			// ����һ��sql
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
		// //�û�ϣ���޸�
		// int rowNum=this.jt.getSelectedRow();//�����û����е��У����һ��ûѡ����-1
		// if(rowNum==-1)
		// {
		// //��ʾ
		// JOptionPane.showMessageDialog(this, "��ѡ�����");
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
		// //�������ݿ⣬�ж��û��Ƿ�Ϸ�
		// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		// ct=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant","sa","root");
		// //�����������
		// String
		// sql="update client set num=(select datediff(day,(select startT from client where cId=?),GETDATE())+1)"
		// +
		// "where cId=? ";
		// ps=ct.prepareStatement(sql);
		//
		// //��������ֵ
		// String cId=(String)sm.getValueAt(rowNum, 0);
		// ps.setString(1,cId);
		// ps.setString(2,cId);
		// ps.executeUpdate();
		// int money;
		// int price;
		// int num;
		// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		// ct=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant","sa","881221");
		// //�����������
		// sql="select price from room where roomNo=?";
		// ps=ct.prepareStatement(sql);
		//
		// num=Integer.parseInt((String)sm.getValueAt(rowNum, 11));
		// System.out.print(num);
		//
		//
		//
		//
		// //��������ֵ
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
		// price=Integer.parseInt((String)rs.getString(3));/*������������Ⱑ�������㿴����ô�㰡����������*/
		// System.out.print(price);
		// money=price*num;
		// System.out.print(money);
		//
		//
		// //4ִ�в���
		//
		// // this.dispose();//�رնԻ���
		// } catch (ClassNotFoundException e1) {
		// // TODO: handle exception
		// e1.printStackTrace();
		// } catch (SQLException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }finally{
		// //�ͷ����������Ӷ���
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
		// //��ʾ�޸ĶԻ���
		// // new ClieUpdate(this,"�޸�",true,sm,rowNum);
		// //�����µ�����ģ���࣬����
		// updateModel();
		//
		// }
		//
		//
		//

		else if (e.getSource() == jb6)
		{
			// �û�ϣ���޸�
			int rowNum = this.jt.getSelectedRow();// �����û����е��У����һ��ûѡ����-1
			if (rowNum == -1)
			{
				// ��ʾ
				JOptionPane.showMessageDialog(this, "��ѡ�����");
				return;
			}

			PreparedStatement ps = null;
			Connection ct = null;
			ResultSet rs = null, rs1 = null;
			// Statement st=null;

			try
			{

				// �������ݿ⣬�ж��û��Ƿ�Ϸ�
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				ct = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant", "sa", "root");
				// �����������
				String sql = "update client set num=(select datediff(day,(select startT from client where cId=?),GETDATE())+1)" + "where cId=? ";
				ps = ct.prepareStatement(sql);

				// ��������ֵ
				String cId = (String) sm.getValueAt(rowNum, 0);
				ps.setString(1, cId);
				ps.setString(2, cId);
				ps.executeUpdate();
				int money = 0;
				int price = 0;
				int num = 0;
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				ct = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant", "sa", "root");
				// �����������

				num = Integer.parseInt((String) sm.getValueAt(rowNum, 11));
				System.out.print(num);
				sql = "select price from room where roomNo=?";
				ps = ct.prepareStatement(sql);

				// ��������ֵ
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

				JOptionPane.showMessageDialog(this, "�����踶��" + money);

				// 4ִ�в���

				// this.dispose();//�رնԻ���
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
				// �ͷ����������Ӷ���
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
			// ��ʾ�޸ĶԻ���
			// new ClieUpdate(this,"�޸�",true,sm,rowNum);
			// �����µ�����ģ���࣬����
			updateModel();

		}

	}

}
