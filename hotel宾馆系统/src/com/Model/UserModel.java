//用户数据表模型，完成对用户的各种操作,这里主要编写项目的业务操作
//Download by http://www.codefans.net
package com.Model;

import java.sql.*;

import com.db.*;


public class UserModel{

	/**
	 * 
	 * @param uid 用户编号
	 * @param pass用户密码
	 * @return 该用户的职位，如果用户不存在返回空
	 */
	public String checkUser(String uid,String pass)
	{
		
		String zhiwei = "";
		SqlHelper sp = null;//这里必须将赋值为null,否则下面在关闭资源的时候sp.close()会出错
		try{
			
			//组织slq语句
			String sql = "select zhiwei from login,renshiziliao " +
					"where login.userid = renshiziliao.userid and login.userid = ? and login.password = ?";
			String paras[] = {uid,pass};
			sp = new SqlHelper();
			ResultSet rs = sp.query(sql, paras); //这里的结果集rs和sqlhelpei里面的结果集rs是同一个，应为java是引用传递
			if(rs.next())
			{
				//则取出职位
				zhiwei = rs.getString(1);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally{
			sp.close(); //关闭资源一定要放到finally里面
		}
		return zhiwei;
			
	}
	
}
