/**
 *
 * �����ҵ�һ��Stu���ģ��
 * ���԰Ѷ�student��ĸ��ֲ�����װ�ڸ�����ģ����
 */

package com.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.table.*;

import com.db.SqlHelperRoom;

public class RoomModel extends AbstractTableModel
{

	// rowData�������������
	// columnNames�����������

	Vector rowData, columnNames;

	// ���ѧ��(�� ɾ ��)
	public boolean updateRoom(String sql, String[] paras)
	{
		// ����һ��sqlHelper(������򲢷��Բ����ǣ����԰�sqlHelper���ɾ�̬��static)
		SqlHelperRoom sh = new SqlHelperRoom();
		return sh.updExe(sql, paras);

	}

	// ��ѯ�ı��ʾ��ǳ�ʼ��
	public void queryRoom(String sql, String[] paras)
	{
		SqlHelperRoom sh = null;

		columnNames = new Vector();
		columnNames.add("�����");
		columnNames.add("��̬");
		columnNames.add("����");
		columnNames.add("��Ǯ");

		// rowData���Դ�Ŷ���
		rowData = new Vector();

		try
		{
			sh = new SqlHelperRoom();
			ResultSet rs = sh.queryExe(sql, paras);

			while (rs.next())
			{

				Vector hang = new Vector();
				hang.add(rs.getString(1));
				// System.out.println(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getString(3));
				hang.add(rs.getString(4));

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
