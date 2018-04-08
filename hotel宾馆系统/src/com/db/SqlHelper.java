/**
 * 对数据库操作的类
 */
package com.db;

import java.util.*;
import java.sql.*;

//Download by http://www.codefans.net
public class SqlHelper
{

	PreparedStatement ps = null;
	ResultSet rs = null;
	Connection ct = null;

	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost/housing management?user=root&password=root";

	// 连接sql server 2008
	// String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	// String
	// url="jdbc:sqlserver://localhost:1433;DatabaseName=restaurant?user=sa&password=root";

	// String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	// String
	// url="jdbc:sqlserver://localhost:1433;databaseName=restaurant;user=sa;password=root";

	// 构造函数初始化ct

	public boolean update(String sql, String[] paras)
	{
		boolean b = true;
		try
		{
			Class.forName(driver);
			ct = DriverManager.getConnection(url);
			ps = ct.prepareStatement(sql);
			for (int i = 0; i < paras.length; i++)
			{
				ps.setString(i + 1, paras[i]);
			}
			if (ps.executeUpdate() != 1)
			{
				b = false;
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();

		} finally
		{
			this.close();
		}
		return b;
	}

	public SqlHelper()
	{
		try
		{
			Class.forName(driver);
			ct = DriverManager.getConnection(url);
			// ps = ct.prepareStatement(sql);
			// rs = ps.executeQuery();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{

		}
	}

	public ResultSet query(String sql, String[] paras)// 查询操作
	{
		try
		{
			ps = ct.prepareStatement(sql);
			// 对sql的参数赋值
			for (int i = 0; i < paras.length; i++)
			{
				ps.setString(i + 1, paras[i]);
			}
			rs = ps.executeQuery();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return rs;
	}

	// 关闭资源
	public void close()
	{
		try
		{
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (ct != null)
				ct.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
