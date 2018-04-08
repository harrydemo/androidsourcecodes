/*
 * ����һ�������ݿ���в�������sqlHelper
 * Download by http://www.codefans.net
 */

package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SqlHelperClient
{
	// ����������ݿ���Ҫ�Ķ���
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;

	String url = "jdbc:sqlserver://localhost:1433;DatabaseName=restaurant";
	String user = "sa";
	String passwd = "root";
	String dirver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	// �ر����ݿ���Դ
	public void close()
	{
		// �ر���Դ

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
			// 1.��������
			Class.forName(dirver);
			// �õ�����
			ct = DriverManager.getConnection(url, user, passwd);
			// ����ps
			ps = ct.prepareStatement(sql);
			// ��ps���ʺŸ�ֵ

			rs = ps.executeQuery();

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		} finally
		{
			// �ر���Դ��

		}
		return rs;

	}

	// ��ѯ���ݿ�Ĳ���
	public ResultSet queryExe(String sql, String[] paras)
	{
		try
		{
			// 1.��������
			Class.forName(dirver);
			// �õ�����
			ct = DriverManager.getConnection(url, user, passwd);
			// ����ps
			ps = ct.prepareStatement(sql);
			// ��ps���ʺŸ�ֵ
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
			// �ر���Դ��

		}
		return rs;

	}

	// ����ɾ�ĺ���һ��
	public boolean updExe(String sql, String[] paras)
	{
		boolean b = true;
		try
		{
			// 1.��������
			Class.forName(dirver);
			// �õ�����
			ct = DriverManager.getConnection(url, user, passwd);
			// ����ps
			ps = ct.prepareStatement(sql);
			// ��ps���ʺŸ�ֵ
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
