//�û����ݱ�ģ�ͣ���ɶ��û��ĸ��ֲ���,������Ҫ��д��Ŀ��ҵ�����
//Download by http://www.codefans.net
package com.Model;

import java.sql.*;

import com.db.*;


public class UserModel{

	/**
	 * 
	 * @param uid �û����
	 * @param pass�û�����
	 * @return ���û���ְλ������û������ڷ��ؿ�
	 */
	public String checkUser(String uid,String pass)
	{
		
		String zhiwei = "";
		SqlHelper sp = null;//������뽫��ֵΪnull,���������ڹر���Դ��ʱ��sp.close()�����
		try{
			
			//��֯slq���
			String sql = "select zhiwei from login,renshiziliao " +
					"where login.userid = renshiziliao.userid and login.userid = ? and login.password = ?";
			String paras[] = {uid,pass};
			sp = new SqlHelper();
			ResultSet rs = sp.query(sql, paras); //����Ľ����rs��sqlhelpei����Ľ����rs��ͬһ����ӦΪjava�����ô���
			if(rs.next())
			{
				//��ȡ��ְλ
				zhiwei = rs.getString(1);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally{
			sp.close(); //�ر���Դһ��Ҫ�ŵ�finally����
		}
		return zhiwei;
			
	}
	
}
