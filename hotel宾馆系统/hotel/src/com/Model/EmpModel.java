/**
 * �������¹��������ģ���࣬��Ҫ��ɶ����±�ĸ��ֲ���
 */
package com.Model;

//Download by http://www.codefans.net
import java.sql.*;

import com.db.*;

import java.util.Vector;

import javax.swing.table.*;

public class EmpModel extends AbstractTableModel
{

	public static int count;
	public boolean b = false;
	Vector colums;// Ҳ��д����Vector <String>colums;
	Vector<Vector> rows;

	public boolean updEmp(String sql, String[] paras) // ������Ϣ
	{
		// ����sql���������Ĳ����Բ����ǣ����԰ѳ�������static�ģ��������ɵ�̬�ģ�
		SqlHelper sqlhelper = new SqlHelper();
		return sqlhelper.update(sql, paras);
	}

	// дһ�����������ڲ�ѯ��Ҫ��ʾ��������Ϣ
	// query������ͨ��,�����޸�
	public void query(String sql, String[] paras)// ����һ���������Ϊͳ�����ݱ��еļ�¼����׼��
	{

		// ��ʼ����
		this.colums = new Vector<String>();// <String>ָ������ΪString��������û�л�ɫ��̾�ž���
		this.rows = new Vector<Vector>();

		// ����һ��SqlHelper����
		SqlHelper sh = new SqlHelper();
		ResultSet rs = sh.query(sql, paras);// ����ResultSet������Ҫ�����import
											// java.sql.*;

		// ��rs�Ľ�����뵽rows��
		try
		{
			// ��rs�����п��Եõ�һ��ResultSetMetadata���󣬿��Ի�ò�ѯ�˶�����
			ResultSetMetaData rsmt = rs.getMetaData();// ���׳��쳣
			for (int i = 0; i < rsmt.getColumnCount(); i++)
			{
				this.colums.add(rsmt.getColumnName(i + 1));// ���Ǵ�1��ʼ���
			}
			while (rs.next())// ������Ҫ�����쳣���
			{
				Vector<String> temp = new Vector<String>();
				/*
				 * temp.add(rs.getString(1)); temp.add(rs.getString(2));
				 * temp.add(rs.getString(3)); temp.add(rs.getString(4));
				 */

				for (int i = 0; i < rsmt.getColumnCount(); i++)
				{
					temp.add(rs.getString(i + 1));
				}

				rows.add(temp);// �������ݷ��뵽row��
				b = true;
			}

			if (rs.last())// �е��������ֻ�ܷŵ�����
			{
				count = rs.getRow();
				System.out.print("����" + count + "����¼");
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			sh.close();// �ر���Դ
		}

	}

	// ��������
	public int getColumnCount()
	{
		// TODO Auto-generated method stub
		return this.colums.size();
	}

	// ��������
	public int getRowCount()
	{
		// TODO Auto-generated method stub
		return this.rows.size();
	}

	// ����ֵ
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		// TODO Auto-generated method stub
		return ((Vector) rows.get(rowIndex)).get(columnIndex);
	}

	@Override
	public String getColumnName(int column)
	{
		// TODO Auto-generated method stub
		return this.colums.get(column).toString();
	}

}
