package wzhg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import wzhg.dao.UserDao;
import wzhg.entity.User;
import wzhg.util.DBUtil;

public class UserDaoImpl implements UserDao {
	
	public User login(String username, String password) {

		String sql = " select id,username,password from UserTbl where username=? and password=? ";
		DBUtil util = new DBUtil();
		Connection conn = util.openConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, username);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(1);
				User u = new User();
				u.setId(id);
				u.setPassword(password);
				u.setUsername(username);
				return u;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.closeConn(conn);
		}
		return null;
	}

}
