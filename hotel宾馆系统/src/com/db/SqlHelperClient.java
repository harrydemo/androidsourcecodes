/*
 * 这是一个对数据库进行操作的类sqlHelper
 * Download by http://www.codefans.net
 */

package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SqlHelperClient
{
	// 定义操作数据库需要的东东
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;

	String url = "jdbc:sqlserver://localhost:1433;DatabaseName=restaurant";
	String user = "sa";
	String passwd = "root";
	String dirver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	// 关闭数据库资源
	public void close()
	{
		// 关闭资源

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
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public ResultSet queryExe(String sql)
	{
		try
		{
			// 1.加载驱动
			Class.forName(dirver);
			// 得到连接
			ct = DriverManager.getConnection(url, user, passwd);
			// 创建ps
			ps = ct.prepareStatement(sql);
			// 给ps的问号赋值

			rs = ps.executeQuery();

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		} finally
		{
			// 关闭资源？

		}
		return rs;

	}

	// 查询数据库的操作
	public ResultSet queryExe(String sql, String[] paras)
	{
		try
		{
			// 1.加载驱动
			Class.forName(dirver);
			// 得到连接
			ct = DriverManager.getConnection(url, user, passwd);
			// 创建ps
			ps = ct.prepareStatement(sql);
			// 给ps的问号赋值
			for (int i = 0; i < paras.length; i++)
			{
				ps.setString(i + 1, paras[i]);

			}
			rs = ps.executeQuery();

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		} finally
		{
			// 关闭资源？

		}
		return rs;

	}

	// 把增删改和在一起
	public boolean updExe(String sql, String[] paras)
	{
		boolean b = true;
		try
		{
			// 1.加载驱动
			Class.forName(dirver);
			// 得到连接
			ct = DriverManager.getConnection(url, user, passwd);
			// 创建ps
			ps = ct.prepareStatement(sql);
			// 给ps的问号赋值
			for (int i = 0; i < paras.length; i++)
			{
				ps.setString(i + 1, paras[i]);
			}
			if (ps.executeUpdate() != 1)
			{
				b = false;
			}
		} catch (Exception e)
		{
			b = false;
			e.printStackTrace();
			// TODO: handle exception
		} finally
		{
			this.close();

		}

		return b;
	}

}
