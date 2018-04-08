/**
 *
 * �����ҵ�һ��Stu���ģ��
 * ���԰Ѷ�student��ĸ��ֲ�����װ�ڸ�����ģ����
 */

package com.Model;

//Download by http://www.codefans.net
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.table.*;

import com.db.SqlHelperClient;

public class ClieModel extends AbstractTableModel
{

	// rowData�������������
	// columnNames�����������

	Vector rowData, columnNames;

	// ���ѧ��(�� ɾ ��)
	public boolean updateClie(String sql, String[] paras)
	{
		// ����һ��sqlHelper(������򲢷��Բ����ǣ����԰�sqlHelper���ɾ�̬��static)
		SqlHelperClient sh = new SqlHelperClient();
		return sh.updExe(sql, paras);

	}

	// ��ѯ�ı��ʾ��ǳ�ʼ��
	public void queryClie(String sql, String[] paras)
	{
		SqlHelperClient sh = null;

		columnNames = new Vector();
		columnNames.add("���");
		columnNames.add("����");
		columnNames.add("�Ա�");
		columnNames.add("����");
		columnNames.add("���֤��");
		columnNames.add("����");
		columnNames.add("�绰");
		columnNames.add("�����");
		columnNames.add("�ͻ�״̬");
		columnNames.add("��סʱ��");
		columnNames.add("�˷�ʱ��");
		columnNames.add("����");
		columnNames.add("����");

		// rowData���Դ�Ŷ���
		rowData = new Vector();

		try
		{
			sh = new SqlHelperClient();
			ResultSet rs = sh.queryExe(sql, paras);

			while (rs.next())
			{

				Vector hang = new Vector();
				hang.add(rs.getString(1));
				// System.out.println(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getString(3));
				hang.add(rs.getInt(4));
				hang.add(rs.getString(5));
				hang.add(rs.getString(6));
				hang.add(rs.getString(7));
				hang.add(rs.getString(8));
				hang.add(rs.getString(9));
				hang.add(rs.getString(10));
				hang.add(rs.getString(11));
				hang.add(rs.getString(12));
				hang.add(rs.getString(13));

				// System.out.println(rs.getString(11));

				rowData.add(hang);
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		} finally
		{
			sh.close();

		}
	}

	@Override
	public String getColumnName(int column)
	{
		// TODO Auto-generated method stub
		return (String) this.columnNames.get(column);
	}

	// �õ����ж�����
	public int getColumnCount()
	{
		// TODO Auto-generated method stub
		return this.columnNames.size();
	}

	// �õ����ж�����
	public int getRowCount()
	{
		// TODO Auto-generated method stub
		return this.rowData.size();
	}

	// �õ�ĳ��ĳ�е�����
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		// TODO Auto-generated method stub
		return ((Vector) this.rowData.get(rowIndex)).get(columnIndex);
	}

}
