/**
 * 这是人事管理的数据模型类，主要完成对人事表的各种操作
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
	Vector colums;// 也可写作：Vector <String>colums;
	Vector<Vector> rows;

	public boolean updEmp(String sql, String[] paras) // 更新信息
	{
		// 创建sql（如果程序的并发性不考虑，可以把程序做成static的，甚至做成单态的）
		SqlHelper sqlhelper = new SqlHelper();
		return sqlhelper.update(sql, paras);
	}

	// 写一个方法，用于查询需要显示的人事信息
	// query方法不通用,故需修改
	public void query(String sql, String[] paras)// 返回一个结果集，为统计数据表中的记录数做准备
	{

		// 初始化列
		this.colums = new Vector<String>();// <String>指明泛型为String，这样就没有黄色感叹号警告
		this.rows = new Vector<Vector>();

		// 创建一个SqlHelper对象
		SqlHelper sh = new SqlHelper();
		ResultSet rs = sh.query(sql, paras);// 有了ResultSet，就需要引入包import
											// java.sql.*;

		// 把rs的结果放入到rows中
		try
		{
			// 从rs对象中可以得到一个ResultSetMetadata对象，可以获得查询了多少列
			ResultSetMetaData rsmt = rs.getMetaData();// 会抛出异常
			for (int i = 0; i < rsmt.getColumnCount(); i++)
			{
				this.colums.add(rsmt.getColumnName(i + 1));// 列是从1开始编号
			}
			while (rs.next())// 这里需要捕获异常情况
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

				rows.add(temp);// 将行数据放入到row中
				b = true;
			}

			if (rs.last())// 有点诡异嘛，这句只能放到这里
			{
				count = rs.getRow();
				System.out.print("共有" + count + "条记录");
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			sh.close();// 关闭资源
		}

	}

	// 返回列数
	public int getColumnCount()
	{
		// TODO Auto-generated method stub
		return this.colums.size();
	}

	// 返回行数
	public int getRowCount()
	{
		// TODO Auto-generated method stub
		return this.rows.size();
	}

	// 返回值
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
