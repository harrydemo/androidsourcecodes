package wpf;
import static wpf.ConstantUtil.ALBUM;
import static wpf.ConstantUtil.CHAR_ENCODING;
import static wpf.ConstantUtil.COMMENT;
import static wpf.ConstantUtil.CONNECTION_OUT;
import static wpf.ConstantUtil.DELETE_FAIL;
import static wpf.ConstantUtil.DELETE_SUCCESS;
import static wpf.ConstantUtil.DIARY;
import static wpf.ConstantUtil.DIARY_FAIL;
import static wpf.ConstantUtil.DIARY_SUCCESS;
import static wpf.ConstantUtil.FEIEND;
import static wpf.ConstantUtil.HEAD;
import static wpf.ConstantUtil.LOGIN_FAIL;
import static wpf.ConstantUtil.PHOTO;
import static wpf.ConstantUtil.P_COMMENT;
import static wpf.ConstantUtil.REGISTER_FAIL;
import static wpf.ConstantUtil.UPDATE_STATE_FAIL;
import static wpf.ConstantUtil.UPDATE_STATE_SUCCESS;
import static wpf.ConstantUtil.USER;
import static wpf.ConstantUtil.VISIT;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * 该类为数据库工具类，提供一系列的静态方法实现对数据库的操作
 */
public class DBUtil {
	 /*
	  * date_format(v_date,'%Y年%c月%e日 %k:%i:%s')
		date_format(v_date,'%Y-%c-%e %k:%i:%s')
	  */
	//方法：使用数据源连接池访问数据库
	public static Connection getConnection(){
		Connection con = null;
		//使用数据源连接池
//		try{
//			Context initial = new InitialContext();    
//			//其中mysql为数据源jndi名称      	
//			DataSource ds = (DataSource)initial.lookup("java:comp/env/jdbc/mysql");
//			con=ds.getConnection();
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
		//使用JDBC直接访问数据库
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost/kdwb","root","123");			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	//方法:获取指定表中的当前最大编号，该方法为同步方法
	public static synchronized int getMax(String table){
		int max = -1;
		Connection con = null;			//声明数据库连接对象
		Statement st = null;
		ResultSet rs = null;
		try{
			con = getConnection();		//获取数据库连接
			st = con.createStatement();	//创建一个Statement对象
			String sql = "update max set "+table+"="+table+"+1";
			st.executeUpdate(sql);					//更新最大编号
			rs = st.executeQuery("select "+table+" from max");				//查询最大编号
			if(rs.next()){
				max = rs.getInt(1);
				return max;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(st != null){
					st.close();
					st = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return max;
	}
	//方法：检查用户名和密码是否正确
	public static ArrayList<String> checkLogin(String u_no,String u_pwd){
		ArrayList<String> result = new ArrayList<String>();
		Connection con = null;		//声明获取数据库连接
		PreparedStatement ps = null;					//声明Statement对象
		ResultSet rs = null;					//声明ResultSet对象
		try{
			con = getConnection();		//获取数据库连接
			if(con == null){			//判断数据库连接对象是否
				result.add(CONNECTION_OUT);		//
				return result;
			}
			ps = con.prepareStatement("select u_no,u_name,u_email,u_state,h_id from user where u_no=? and u_pwd=?");
			ps.setString(1, u_no);				//设置预编译语句的参数
			ps.setString(2, u_pwd);				//设置预编译语句的参数
			rs = ps.executeQuery();
			if(rs.next()){				//判断结果集是否为空
				for(int i=1;i<=5;i++){
					result.add(rs.getString(i));	//将结果集中数据存放到ArrayList中
				}
			}
			else{						//如果数据库查无此人
				result.add(LOGIN_FAIL);	//返回登录出错信息
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：注册用户
	public static String registerUser(String u_name,String u_pwd,String u_email,String u_state,String h_id){
		String result=null;
		Connection con = null;		//声明数据库连接对象
		PreparedStatement ps = null;		//声明语句对象
		try{
			con = getConnection();
			if(con == null){			//判断是否成功获取连接
				result = CONNECTION_OUT;
				return result;		//返回方法的执行
			}
			ps = con.prepareStatement("insert into user(u_no,u_name,u_pwd,u_email,u_state,h_id)" +
					"values(?,?,?,?,?,?)");		//构建SQL语句
			String u_no = String.valueOf(getMax(USER));	//获得分配给用户的帐号
			u_name = new String(u_name.getBytes(),"ISO-8859-1");		//转成ISO-8859-1以插入数据库
			u_state = new String(u_state.getBytes(),"ISO-8859-1");		//转成ISO-8859-1以插入数据库
			int no = Integer.valueOf(u_no);
			int hid = Integer.valueOf(h_id);
			ps.setInt(1, no);			//设置PreparedStatement的参数
			ps.setString(2, u_name);
			ps.setString(3, u_pwd);
			ps.setString(4, u_email);
			ps.setString(5, u_state);
			ps.setInt(6,hid);
			int count = ps.executeUpdate();			//执行插入
			if(count == 1){		//如果插入成功
				result = u_no;		//获得玩家的帐号
			}
			else{						//如果没有插入数据
				result = REGISTER_FAIL;		//获得出错信息
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：查询用户所有的好友，返回列表
	public static ArrayList<User> getFiendList(String u_no){
		ArrayList<User> result = new ArrayList<User>();
		Connection con = null;			//声明数据库连接对象
		Statement st = null;			//声明语句对象
		ResultSet rs = null;			//声明结果集对象
		try{
			con = getConnection();		//获得连接
			st = con.createStatement();		//获得Statement对象
			String sql = "select user.u_no,user.u_name,user.u_email,user.u_state,user.h_id " +
					"from user ,friend " +
					" where user.u_no=friend.u_noy and friend.u_noz="+u_no;
			rs = st.executeQuery(sql);
			while(rs.next()){			//循环取出每行记录
				String no = rs.getInt(1)+"";
				String name = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String email = rs.getString(3);
				String state = new String(rs.getString(4).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String hid = rs.getInt(5)+"";
				User user = new User(no, name, email, state, hid);
				result.add(user);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(st != null){
					st.close();
					st = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：删除指定好友
	public static String deleteFriend(String u_no,String u_noToDelete){
		String result = null;
		Connection con = null;		//声明数据库连接
		PreparedStatement ps = null;	//声明语句对象
		try{
			con = getConnection();	//获取数据库连接
			ps = con.prepareStatement("delete from friend where u_noz=? and u_noy=?");	//创建语句
			ps.setInt(1, Integer.valueOf(u_no));
			ps.setInt(2, Integer.valueOf(u_noToDelete));
			int count = ps.executeUpdate();		//执行语句
			if(count == 1){	//删除成功
				result = DELETE_SUCCESS;
			}
			else{
				result = DELETE_FAIL;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：写入新的日志
	public static String writeNewDiary(String title,String content,String author){
		String result = null;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();			//获得连接
			ps = con.prepareStatement("insert into diary(r_id,r_title,r_content,u_no)" +
					" values(?,?,?,?)");
			int max = getMax(DIARY);		//获取当前最大编号
			ps.setInt(1, max);				//设置各个字段的值
			ps.setString(2, new String(title.getBytes(CHAR_ENCODING),"ISO-8859-1"));	
			ps.setString(3, new String(content.getBytes(CHAR_ENCODING),"ISO-8859-1"));
			int u_no = Integer.valueOf(author);	//转成字符串
			ps.setInt(4, u_no);
			int count = ps.executeUpdate();		//更新数据库
			if(count == 1){
				result = DIARY_SUCCESS;
			}
			else{
				result = DIARY_FAIL;
			}
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：根据权限获取相册信息
	public static ArrayList<String []> getAlbumListByAccess(String uno,String visitor){
		ArrayList<String []> result = new ArrayList<String []>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();
			if(checkIfMyFriend(uno, visitor)){//检查访问者和被访问者是否为好友
				ps = con.prepareStatement("select x_id,x_name from album where u_no=? and x_access<2");
			}
			else{
				ps = con.prepareStatement("select x_id,x_name from album where u_no=? and x_access=0");
			}
			ps.setInt(1, Integer.valueOf(uno));
			rs = ps.executeQuery();		//执行查询
			while(rs.next()){	//如果结果集中有数据
				String xid = rs.getInt(1)+"";
				String xname = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String [] sa = new String[]{xid,xname};
				result.add(sa);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：获取所有的相册
	public static ArrayList<String []> getAlbumList(String u_no){
		ArrayList<String []> result = new ArrayList<String []>();
		Connection con = null;		//声明数据库连接对象
		PreparedStatement ps = null;	//声明预编译语句
		ResultSet rs = null;			//声明ResultSet对象
		try{
			con = getConnection();		//获得数据库连接
			ps = con.prepareStatement("select x_id,x_name,x_access from album where u_no=?");
			ps.setInt(1, Integer.valueOf(u_no));	//设置参数
			rs = ps.executeQuery();		//执行查询
			while(rs.next()){			//遍历结果集
				String [] sa = new String[3];
				sa[0] = rs.getInt(1)+"";
				sa[1] = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				sa[2] = rs.getInt(3)+"";
				result.add(sa);				//加入到列表中
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法:创建一个相册
	public static int createAlbum(String name,String u_no){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("insert into album(x_id,x_name,u_no) values(?,?,?)");
			ps.setInt(1, getMax(ALBUM));
			ps.setString(2, new String(name.getBytes(CHAR_ENCODING),"ISO-8859-1"));
			ps.setInt(3, Integer.valueOf(u_no));		//设置预编译语句的参数
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：转换成ISO-8859-1
	public static String toISO(String s){
		String result = null;
		try {
			result = new String(s.getBytes(),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	//方法：更新用户心情
	public static String updateState(String u_no,String newState){
		String result = null;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("update user set u_state=? where u_no=?");
			ps.setString(1, new String(newState.getBytes(CHAR_ENCODING),"ISO-8859-1"));
			ps.setInt(2, Integer.valueOf(u_no));
			int count = ps.executeUpdate();
			if(count ==1){
				result = UPDATE_STATE_SUCCESS;
			}
			else{
				result = UPDATE_STATE_FAIL;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：查询用户的日记列表
	public static ArrayList<Diary> getUserDiary(String u_no,int currentPage, int span){
		ArrayList<Diary> result = new ArrayList<Diary>();
		Connection con = null;			//声明数据库连接对象
		Statement st = null;	//声明语句对象
		ResultSet rs = null;			//声明结果集对象
		int start = (currentPage-1)*span;		//计算起始位置
		String sql = "select diary.r_id,diary.r_title,diary.r_content,date_format(diary.r_date,'%Y-%c-%e %k:%i:%s'),diary.u_no,user.u_name" +//日记标题、日记内容、日记时间、日记所属者、日记所属者昵称
				" from diary,user where diary.u_no="+u_no+" and diary.u_no=user.u_no" +	
				" order by diary.r_date desc" + " limit "+start+","+span;			//构建语句对象
		try{
			con = getConnection();			//获得连接
			st = con.createStatement();		//创建语句
			rs = st.executeQuery(sql);		//执行查询 
			while(rs.next()){				//读取结果集生成日记对象
				String rid = rs.getInt(1)+"";
				String title = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String content = new String(rs.getString(3).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String date = rs.getString(4);
				String uno = rs.getInt(5)+"";
				String uname = new String(rs.getString(6).getBytes("ISO-8859-1"),CHAR_ENCODING);
				Diary d = new Diary(rid,title, content, uname, uno, date);
				result.add(d);
			}
			for(Diary d:result){		//为每个日记生成评论列表
				ArrayList<Comments> cmtList = getComments(d.rid);
				d.setCommentList(cmtList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(st != null){
					st.close();
					st = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：获得指定日记的评论列表
	public static ArrayList<Comments> getComments(String r_id){
		ArrayList<Comments> result = new ArrayList<Comments>();
		Connection con = null;			//声明Connection对象
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select date_format(comment.c_date,'%Y-%c-%e %k:%i:%s'),comment.c_content,user.u_name,comment.u_no" +
				" from comment,user where comment.r_id=? and user.u_no=comment.u_no order by comment.c_date desc";
		try{
			con = getConnection();		//获得连接
			ps = con.prepareStatement(sql);	//获得预编译语句
			ps.setInt(1, Integer.valueOf(r_id));	//设置参数
			rs = ps.executeQuery();			//执行查询
			while(rs.next()){
				String date = rs.getString(1);
				String content = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String uname = new String(rs.getString(3).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String uno = rs.getString(4)+"";
				Comments c = new Comments(date,content,uname,uno);
				result.add(c);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：向数据库中插入图片Blob数据
	public static int insertImage(File file,String pname,String pdes,String x_id){
		int result =-1;
		Connection con = null;
		java.sql.PreparedStatement ps = null;
		FileInputStream fis = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("insert into photo(p_id,p_name,p_des,p_data,x_id) values(?,?,?,?,?)");
			ps.setInt(1, getMax(PHOTO));
			ps.setString(2, pname);
			ps.setString(3, pdes);
			fis = new FileInputStream(file);
			ps.setBinaryStream(4, fis,(int)(file.length()));		//转换为int因为版本过低
//			ps.setBlob(4, fis, (int)file.length());
			ps.setInt(5, Integer.valueOf(x_id));
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：从数据库读取对应相册的图片的信息
	public static ArrayList<PhotoInfo> getPhotoInfoByAlbum(String xid,int pageNo,int span){
		ArrayList<PhotoInfo> result = new ArrayList<PhotoInfo>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int start = span*(pageNo-1);		//计算起始位置
		try{
			con = getConnection();
			ps = con.prepareStatement("select p_id,p_name,p_des,x_id from photo" +
					" where x_id=? order by p_id limit "+start+","+span);		//创建语句
			ps.setInt(1, Integer.valueOf(xid));		//设置参数
			rs = ps.executeQuery();
			while(rs.next()){		//遍历结果集
				String p_id = rs.getInt(1)+"";
				String p_name = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);	//相片名称
				String p_des = new String(rs.getString(3).getBytes("ISO-8859-1"),CHAR_ENCODING);	//相片描述
				String x_id = rs.getInt(4)+"";
				PhotoInfo p = new PhotoInfo(p_id, p_name, p_des, x_id);		//上传Photo对象
				result.add(p);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：从数据库中提取Blob数据
	public static Blob getPhotoBlob(String id){
		Blob result = null;
		Connection con = null;	//数据库对象
		PreparedStatement ps = null;	//预编译语句
		ResultSet rs = null;		//结果集
		try{
			con = getConnection();		//获得连接
			ps = con.prepareStatement("select p_data from photo where p_id=?");	//创建语句
			ps.setInt(1, Integer.valueOf(id));	//设置参数
			rs = ps.executeQuery();		//设置参数
			if(rs.next()){	//
				result = rs.getBlob(1);		//获得Blob对象
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
			try{
				ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：从数据库中取出制定相册的长度
	public static int getAlbumSize(String xid){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();		//获得连接
			ps = con.prepareStatement("select count(*) as count from photo where x_id=?");
			ps.setInt(1, Integer.valueOf(xid));		//设置参数
			rs = ps.executeQuery();		//执行查询
			if(rs.next()){
				result = rs.getInt(1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：获得头像列表
	public static ArrayList<HeadInfo> getHeadList(int pageNo,int span){
		ArrayList<HeadInfo> result = new ArrayList<HeadInfo>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int start = (pageNo-1)*span;	//计算开始位置
		try{
			con = getConnection();	//获得数据库连接
			st = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery("select h_id,h_des,u_no from head limit "+start+","+span);
			while(rs.next()){
				String hid = rs.getInt(1)+"";
				String hdes = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String uno = rs.getInt(3)+"";
				HeadInfo hi = new HeadInfo(hid, hdes, uno);	//创建HeadInfo对象
				result.add(hi);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(st != null){
					st.close();
					st = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：获得头像的Blob数据
	public static Blob getHeadBlob(String hid){
		Blob result = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();	//获得连接
			ps = con.prepareStatement("select h_data from head where h_id=?");
			ps.setInt(1, Integer.valueOf(hid));		//设置参数
			rs = ps.executeQuery();		//执行查询
			if(rs.next()){		//查找结果集
				result = rs.getBlob(1);		//赋值
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：向数据库中插入头像Blob
	public static int insertHeadFile(File file,String hdes,String uno){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		FileInputStream fis = null;
		try{
			con = getConnection();		//获得数据库连接
			ps = con.prepareStatement("insert into head(h_id,h_des,h_data,u_no) values(?,?,?,?)");//设置参数
			int max = getMax(HEAD);
			ps.setInt(1, max);
			ps.setString(2, hdes);
			fis = new FileInputStream(file);
			ps.setBinaryStream(3, fis,(int)file.length());
			ps.setInt(4, Integer.valueOf(uno));
			result = ps.executeUpdate();		//执行插入
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：更改用户的头像
	public static int changeUserHead(String uno,String hid){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("update user set h_id=? where u_no=?");
			ps.setInt(1, Integer.valueOf(hid));	//设置参数
			ps.setInt(2, Integer.valueOf(uno));	//设置参数
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：修改用户信息（不包括头像）
	public static int changeUserInfo(String uno,String uname,String uemail,String ustate){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();	//获得连接
			ps = con.prepareStatement("update user set u_name=?,u_email=?,u_state=? where u_no=?");	//创建语句
			ps.setString(1, new String(uname.getBytes("GBK"),"ISO-8859-1"));
			ps.setString(2, new String(uemail.getBytes("GBK"),"ISO-8859-1"));
			ps.setString(3, new String(ustate.getBytes("GBK"),"ISO-8859-1"));
			ps.setInt(4, Integer.valueOf(uno));	//
			result = ps.executeUpdate();		//执行更新
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：获得当前头像的总数
	public static int getHeadSize(){
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try{
			con = getConnection();		//获得连接
			st = con.createStatement();
			rs = st.executeQuery("select count(h_id) as count from head");	//执行查询
			if(rs.next()){		//查到数据
				result = rs.getInt(1);	//读取数据
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(st != null){
					st.close();
					st = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：通过微博号找到相应用户
	public static User getUser(String uno){
		User user = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("select u_name,u_email,u_state,h_id from user where u_no=?");
			ps.setString(1, uno);
			rs = ps.executeQuery();
			while(rs.next()){		//遍历结果集
				String uname = new String(rs.getString(1).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String uemail = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String ustate = new String(rs.getString(3).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String hid = rs.getString(4);
				user = new User(uno, uname, uemail, ustate, hid);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return user;
	}
	//方法：获取日记总数
	public static int getDiarySize(String u_no){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("select count(r_id) as count from diary where u_no=?");
			ps.setInt(1, Integer.valueOf(u_no));		//设置参数
			rs = ps.executeQuery();
			if(rs.next()){			//查看结果集
				result = rs.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：给指定日志添加一条评论
	public static int addComment(String c_comment,String r_id,String u_no){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();	//获得数据库连接
			ps = con.prepareStatement("insert into comment(c_id,c_content,u_no,r_id) values(?,?,?,?)");
			ps.setInt(1, getMax(COMMENT));		//设置自动编号的值
			ps.setString(2, new String(c_comment.getBytes(CHAR_ENCODING),"ISO-8859-1"));			//设置评论内容字段
			ps.setInt(3, Integer.valueOf(u_no));	//设置用户编号
			ps.setInt(4, Integer.valueOf(r_id));	//设置日记编号
			result = ps.executeUpdate();			//执行插入操作
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：检查某人是不是自己的好友
	public static boolean checkIfMyFriend(String me,String stranger){
		boolean result = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();	//获得数据库连接
			ps = con.prepareStatement("select f_id from friend where u_noz=? and u_noy=?");	//创建语句对象
			ps.setInt(1, Integer.valueOf(me));	//设置代表自己编号的参数
			ps.setInt(2, Integer.valueOf(stranger));	//设置代表对方编号的参数
			rs = ps.executeQuery();	//执行查询
			if(rs.next()){
				result = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：将某人添加到好友列表中去
	public static int makeFriend(String me,String stranger){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs  = null;
		try{
			con = getConnection();		//获得数据库连接
			ps = con.prepareStatement("insert into friend(f_id,u_noz,u_noy) values(?,?,?)");
			ps.setInt(1, getMax(FEIEND));			//设置id编号
			ps.setInt(2, Integer.valueOf(me));		//设置添加人的id
			ps.setInt(3, Integer.valueOf(stranger));	//设置被添加人的id
			result = ps.executeUpdate();		//执行插入
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：更新指定用户的最近访客
	public static int addVisitor(String host,String visitor){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int hostId = Integer.valueOf(host);			//主人的id
		int visitorId = Integer.valueOf(visitor);	//访问者的id
		try{
			con = getConnection();		//获得数据库连接
			//首先查看Visitor是否来过
			ps = con.prepareStatement("select v_no from visit where u_no=? and v_no=?");
			ps.setInt(1, hostId);	//设置主人id
			ps.setInt(2, visitorId);
			rs = ps.executeQuery();
			if(rs.next()){			//提取结果集数据
				ps = con.prepareStatement("update visit set v_date=now() where u_no=? and v_no=?");
				ps.setInt(1, hostId);		//设置主人id
				ps.setInt(2, visitorId);	//设置访客id
				result = ps.executeUpdate();	//执行更新
			}
			else{					//最新的那个访客和当前访客不相同
				ps = con.prepareStatement("insert into visit(v_id,u_no,v_no) values(?,?,?)");
				ps.setInt(1, getMax(VISIT));				//设置主键值
				ps.setInt(2, Integer.valueOf(host));		//设置主人id
				ps.setInt(3, Integer.valueOf(visitor));		//设置访客id
				result = ps.executeUpdate();				//执行查询				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：获得用户的访客列表
	public static ArrayList<Visitor> getVisitors(String uno){
		ArrayList<Visitor> result = new ArrayList<Visitor>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();		//获得数据库连接
			ps = con.prepareStatement("select user.u_no,user.u_name,user.h_id,date_format(visit.v_date,'%Y-%c-%e %k:%i:%s') from user,visit" +
					" where user.u_no=visit.v_no and visit.u_no=? order by visit.v_date desc");	//昵称、头像、时间
			ps.setInt(1, Integer.valueOf(uno));
			rs = ps.executeQuery();			//执行查询
			while(rs.next()){				//遍历结果集
				String v_no = rs.getInt(1)+"";
				String v_name = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String h_id = rs.getInt(3)+"";
				String v_date = rs.getString(4);
				Visitor v = new Visitor(v_no, v_name, h_id, v_date);
				result.add(v);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：删除指定日记
	public static int deleteDiary(String rid){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			deleteAllCommentByDiary(rid);			//先删除评论
			con = getConnection();
			ps = con.prepareStatement("delete from diary where r_id=?");
			ps.setInt(1, Integer.valueOf(rid));
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：删除指定日志的所有评论
	public static int deleteAllCommentByDiary(String rid){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("delete from comment where r_id=?");
			ps.setInt(1, Integer.valueOf(rid));
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：删除指定图片
	public static int deletePhoto(String pid){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			deleteAllCommentByPhoto(pid);
			con = getConnection();		//获得连接
			ps = con.prepareStatement("delete from photo where p_id=?");
			ps.setInt(1, Integer.valueOf(pid));		//设置删除的照片的id
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：删除指定图片的所有评论
	public static int deleteAllCommentByPhoto(String pid){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("delete from p_comment where p_id=?");
			ps.setInt(1, Integer.valueOf(pid));
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	//方法：搜索好友
	public static ArrayList<User> searchFriendByName(String u_name){
		ArrayList<User> result = new ArrayList<User>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();
			Statement st = con.createStatement();
			String sql = "select u_no,u_name,u_email,u_state,h_id from user where u_name like '%"+u_name+"%'";
			rs = st.executeQuery(sql);
			
			while(rs.next()){			//遍历结果集
				String uno = rs.getInt(1)+"";
				String uname = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String uemail = new String(rs.getString(3).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String ustate = new String(rs.getString(4).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String hid = new String(rs.getString(5).getBytes("ISO-8859-1"),CHAR_ENCODING);
				User u = new User(uno, uname, uemail, ustate, hid);		//创建User对象
				result.add(u);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：添加新的图片评论
	public static int addPhotoComment(String content,String p_id,String u_no){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("insert into p_comment(c_id,c_content,u_no,p_id) values(?,?,?,?)");
			ps.setInt(1, getMax(P_COMMENT));
			ps.setString(2, new String(content.getBytes(CHAR_ENCODING),"ISO-8859-1"));
			ps.setInt(3, Integer.valueOf(u_no));
			ps.setInt(4, Integer.valueOf(p_id));
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：获得指定图片的评论
	public static ArrayList<P_Comments> getPhotoComment(String p_id){
		ArrayList<P_Comments> result = new ArrayList<P_Comments>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("select p_comment.c_content,p_comment.u_no,user.u_name,date_format(p_comment.c_date,'%Y-%c-%e %k:%i:%s')" +
					" from p_comment,user where p_comment.u_no=user.u_no and p_id=? order by p_comment.c_date");
			ps.setInt(1, Integer.valueOf(p_id));
			rs = ps.executeQuery();
			while(rs.next()){
				String content = new String(rs.getString(1).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String u_no = rs.getString(2);
				String u_name = new String(rs.getString(3).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String date = rs.getString(4);
				P_Comments pc = new P_Comments(content, u_no, u_name, date);
				result.add(pc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：向数据库插入照片
	public static int insertPhotoFromAndroid(byte [] buf,String pname,String pdes,String x_id){
		int result =-1;
		Connection con = null;
		java.sql.PreparedStatement ps = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("insert into photo(p_id,p_name,p_des,p_data,x_id) values(?,?,?,?,?)");
			ps.setInt(1, getMax(PHOTO));
			ps.setString(2, pname);
			ps.setString(3, pdes);
			InputStream in = new ByteArrayInputStream(buf);
			ps.setBinaryStream(4, in,(int)(in.available()));		//转换为int因为版本过低
//			ps.setBlob(4, fis, (int)file.length());
			ps.setInt(5, Integer.valueOf(x_id));
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：修改相册的权限
	public static int changeAlbumAccess(String xid,String newAccess){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();		//获得数据库连接
			ps = con.prepareStatement("update album set x_access=? where x_id=?");
			ps.setInt(1, Integer.valueOf(newAccess));
			ps.setInt(2, Integer.valueOf(xid));		
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：修改指定日志
	public static int modifyDiary(String rid,String rtitle,String rcontent){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("update diary set r_title=?,r_content=?,r_date=now() where r_id=?");
			ps.setString(1, new String(rtitle.getBytes(CHAR_ENCODING),"ISO-8859-1"));
			ps.setString(2, new String(rcontent.getBytes(CHAR_ENCODING),"ISO-8859-1"));
			ps.setInt(3, Integer.valueOf(rid));
			result = ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(con != null){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	//方法：查询指定相册的权限
	public static int getAlbumAccess(String xid){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("select x_access from album where x_id=?");
			ps.setInt(1, Integer.valueOf(xid));
			rs = ps.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//main方法：用于测试
	public static void main(String args[]){
//		System.out.println(getConnection());
//		System.out.println("第一个p_comment:"+getMax(P_COMMENT));
//		System.out.println("第二个USER:"+getMax(USER));
//		System.out.println("第三个USER:"+getMax(USER));
//		System.out.println("注册第一个："+registerUser("东东", "123", "wpftool@126.com", "今天天气好冷","0"));
//		getFiendList("2008");
//		System.out.println(deleteFriend("2008", "2030"));
//		System.out.println(updateState("2008", "我好生气Lve"));
//		ArrayList<Diary> al = getUserDiary("2008", 1, 5);
//		for(Diary d:al){
//			System.out.println(d.rid+"\t"+d.title+"\t"+d.content+"\t"+d.time+"\t"+d.u_name+"\t"+d.u_no);
//			System.out.println("评论");
//			for(Comments c:d.commentList){
//				System.out.println(c.cmtNo+"\t"+c.cmtName+"\t"+c.content+"\t"+c.date);
//			}
//			System.out.println();
//		}
//		ArrayList<String []> al = getAlbumList("2008");
//		for(String [] sa:al){
//			System.out.println(sa[0]+"\t"+sa[1]);
//		}
//		System.out.println(createAlbum("我和大海", "2008"));
//		System.out.println(getAlbumSize("1"));
//		System.out.println(getHeadList(1,4).size());
//		System.out.println(getHeadBlob("3"));
//		System.out.println(changeUserHead("2008","3"));
//		System.out.println(changeUserInfo("2008","飞飞","wpf@wpf.com","今天好冷啊！"));
//		System.out.println(getHeadSize());
//		User user = getUser("2008");
//		System.out.println(user.u_no+"\t"+user.u_name+"\t"+user.u_email+"\t"+user.u_state+"\t"+user.h_id);
//		System.out.println(getDiarySize("2008"));
//		System.out.println(checkIfMyFriend("2010", "2008"));
//		System.out.println(makeFriend("2010", "2008"));
//		System.out.println(addVisitor("2008","2010"));		
//		System.out.println(getVisitors("2008").get(1).v_name);
//		System.out.println(deleteDiary("38"));
//		System.out.println(deleteDiary("39"));
//		System.out.println(deleteDiary("42"));
//		System.out.println(searchFriendByName("飞").size());
//		ArrayList<User> list = searchFriendByName("飞");
//		for(User u:list){
//			System.out.println(u.u_name);
//		}
////		System.out.println(getComments("7").size());
//		System.out.println(addPhotoComment("在哪里拍的？？", "8", "2009"));
//		System.out.println(getPhotoComment("8").size());
//		System.out.println(getAlbumList("2008").get(0)[2]);
//		System.out.println(changeAlbumAccess("9", "1"));
//		System.out.println(deleteAllCommentByDiary("46"));
//		System.out.println(modifyDiary("4","出游记","今天又看大海了。"));
//		System.out.println(deleteAllCommentByPhoto("8"));
//		System.out.println(deletePhoto("35"));
		System.out.println(getAlbumAccess("5"));
	}
}
