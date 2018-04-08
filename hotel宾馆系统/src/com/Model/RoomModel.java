/**
 *
 * 这是我的一个Stu表的模型
 * 可以把对student表的各种操作封装在该数据模型中
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

	// rowData用来存放行数据
	// columnNames用来存放列明

	Vector rowData, columnNames;

	// 添加学生(增 删 改)
	public boolean updateRoom(String sql, String[] paras)
	{
		// 创建一个sqlHelper(如果程序并发性不考虑，可以吧sqlHelper做成静态的static)
		SqlHelperRoom sh = new SqlHelperRoom();
		return sh.updExe(sql, paras);

	}

	// 查询的本质就是初始化
	public void queryRoom(String sql, String[] paras)
	{
		SqlHelperRoom sh = null;

		columnNames = new Vector();
		columnNames.add("房间号");
		columnNames.add("房态");
		columnNames.add("类型");
		columnNames.add("价钱");

		// rowData可以存放多行
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

	// 得到共有多少列
	public int getColumnCount()
	{
		// TODO Auto-generated method stub
		return this.columnNames.size();
	}

	// 得到共有多少行
	public int getRowCount()
	{
		// TODO Auto-generated method stub
		return this.rowData.size();
	}

	// 得到某行某列的数据
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		// TODO Auto-generated method stub
		return ((Vector) this.rowData.get(rowIndex)).get(columnIndex);
	}

}
