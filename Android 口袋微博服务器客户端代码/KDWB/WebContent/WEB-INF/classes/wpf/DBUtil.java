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
 * ����Ϊ���ݿ⹤���࣬�ṩһϵ�еľ�̬����ʵ�ֶ����ݿ�Ĳ���
 */
public class DBUtil {
	 /*
	  * date_format(v_date,'%Y��%c��%e�� %k:%i:%s')
		date_format(v_date,'%Y-%c-%e %k:%i:%s')
	  */
	//������ʹ������Դ���ӳط������ݿ�
	public static Connection getConnection(){
		Connection con = null;
		//ʹ������Դ���ӳ�
//		try{
//			Context initial = new InitialContext();    
//			//����mysqlΪ����Դjndi����      	
//			DataSource ds = (DataSource)initial.lookup("java:comp/env/jdbc/mysql");
//			con=ds.getConnection();
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
		//ʹ��JDBCֱ�ӷ������ݿ�
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost/kdwb","root","123");			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	//����:��ȡָ�����еĵ�ǰ����ţ��÷���Ϊͬ������
	public static synchronized int getMax(String table){
		int max = -1;
		Connection con = null;			//�������ݿ����Ӷ���
		Statement st = null;
		ResultSet rs = null;
		try{
			con = getConnection();		//��ȡ���ݿ�����
			st = con.createStatement();	//����һ��Statement����
			String sql = "update max set "+table+"="+table+"+1";
			st.executeUpdate(sql);					//���������
			rs = st.executeQuery("select "+table+" from max");				//��ѯ�����
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
	//����������û����������Ƿ���ȷ
	public static ArrayList<String> checkLogin(String u_no,String u_pwd){
		ArrayList<String> result = new ArrayList<String>();
		Connection con = null;		//������ȡ���ݿ�����
		PreparedStatement ps = null;					//����Statement����
		ResultSet rs = null;					//����ResultSet����
		try{
			con = getConnection();		//��ȡ���ݿ�����
			if(con == null){			//�ж����ݿ����Ӷ����Ƿ�
				result.add(CONNECTION_OUT);		//
				return result;
			}
			ps = con.prepareStatement("select u_no,u_name,u_email,u_state,h_id from user where u_no=? and u_pwd=?");
			ps.setString(1, u_no);				//����Ԥ�������Ĳ���
			ps.setString(2, u_pwd);				//����Ԥ�������Ĳ���
			rs = ps.executeQuery();
			if(rs.next()){				//�жϽ�����Ƿ�Ϊ��
				for(int i=1;i<=5;i++){
					result.add(rs.getString(i));	//������������ݴ�ŵ�ArrayList��
				}
			}
			else{						//������ݿ���޴���
				result.add(LOGIN_FAIL);	//���ص�¼������Ϣ
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
	//������ע���û�
	public static String registerUser(String u_name,String u_pwd,String u_email,String u_state,String h_id){
		String result=null;
		Connection con = null;		//�������ݿ����Ӷ���
		PreparedStatement ps = null;		//����������
		try{
			con = getConnection();
			if(con == null){			//�ж��Ƿ�ɹ���ȡ����
				result = CONNECTION_OUT;
				return result;		//���ط�����ִ��
			}
			ps = con.prepareStatement("insert into user(u_no,u_name,u_pwd,u_email,u_state,h_id)" +
					"values(?,?,?,?,?,?)");		//����SQL���
			String u_no = String.valueOf(getMax(USER));	//��÷�����û����ʺ�
			u_name = new String(u_name.getBytes(),"ISO-8859-1");		//ת��ISO-8859-1�Բ������ݿ�
			u_state = new String(u_state.getBytes(),"ISO-8859-1");		//ת��ISO-8859-1�Բ������ݿ�
			int no = Integer.valueOf(u_no);
			int hid = Integer.valueOf(h_id);
			ps.setInt(1, no);			//����PreparedStatement�Ĳ���
			ps.setString(2, u_name);
			ps.setString(3, u_pwd);
			ps.setString(4, u_email);
			ps.setString(5, u_state);
			ps.setInt(6,hid);
			int count = ps.executeUpdate();			//ִ�в���
			if(count == 1){		//�������ɹ�
				result = u_no;		//�����ҵ��ʺ�
			}
			else{						//���û�в�������
				result = REGISTER_FAIL;		//��ó�����Ϣ
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
	//��������ѯ�û����еĺ��ѣ������б�
	public static ArrayList<User> getFiendList(String u_no){
		ArrayList<User> result = new ArrayList<User>();
		Connection con = null;			//�������ݿ����Ӷ���
		Statement st = null;			//����������
		ResultSet rs = null;			//�������������
		try{
			con = getConnection();		//�������
			st = con.createStatement();		//���Statement����
			String sql = "select user.u_no,user.u_name,user.u_email,user.u_state,user.h_id " +
					"from user ,friend " +
					" where user.u_no=friend.u_noy and friend.u_noz="+u_no;
			rs = st.executeQuery(sql);
			while(rs.next()){			//ѭ��ȡ��ÿ�м�¼
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
	//������ɾ��ָ������
	public static String deleteFriend(String u_no,String u_noToDelete){
		String result = null;
		Connection con = null;		//�������ݿ�����
		PreparedStatement ps = null;	//����������
		try{
			con = getConnection();	//��ȡ���ݿ�����
			ps = con.prepareStatement("delete from friend where u_noz=? and u_noy=?");	//�������
			ps.setInt(1, Integer.valueOf(u_no));
			ps.setInt(2, Integer.valueOf(u_noToDelete));
			int count = ps.executeUpdate();		//ִ�����
			if(count == 1){	//ɾ���ɹ�
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
	//������д���µ���־
	public static String writeNewDiary(String title,String content,String author){
		String result = null;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();			//�������
			ps = con.prepareStatement("insert into diary(r_id,r_title,r_content,u_no)" +
					" values(?,?,?,?)");
			int max = getMax(DIARY);		//��ȡ��ǰ�����
			ps.setInt(1, max);				//���ø����ֶε�ֵ
			ps.setString(2, new String(title.getBytes(CHAR_ENCODING),"ISO-8859-1"));	
			ps.setString(3, new String(content.getBytes(CHAR_ENCODING),"ISO-8859-1"));
			int u_no = Integer.valueOf(author);	//ת���ַ���
			ps.setInt(4, u_no);
			int count = ps.executeUpdate();		//�������ݿ�
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
	//����������Ȩ�޻�ȡ�����Ϣ
	public static ArrayList<String []> getAlbumListByAccess(String uno,String visitor){
		ArrayList<String []> result = new ArrayList<String []>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();
			if(checkIfMyFriend(uno, visitor)){//�������ߺͱ��������Ƿ�Ϊ����
				ps = con.prepareStatement("select x_id,x_name from album where u_no=? and x_access<2");
			}
			else{
				ps = con.prepareStatement("select x_id,x_name from album where u_no=? and x_access=0");
			}
			ps.setInt(1, Integer.valueOf(uno));
			rs = ps.executeQuery();		//ִ�в�ѯ
			while(rs.next()){	//����������������
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
	//��������ȡ���е����
	public static ArrayList<String []> getAlbumList(String u_no){
		ArrayList<String []> result = new ArrayList<String []>();
		Connection con = null;		//�������ݿ����Ӷ���
		PreparedStatement ps = null;	//����Ԥ�������
		ResultSet rs = null;			//����ResultSet����
		try{
			con = getConnection();		//������ݿ�����
			ps = con.prepareStatement("select x_id,x_name,x_access from album where u_no=?");
			ps.setInt(1, Integer.valueOf(u_no));	//���ò���
			rs = ps.executeQuery();		//ִ�в�ѯ
			while(rs.next()){			//���������
				String [] sa = new String[3];
				sa[0] = rs.getInt(1)+"";
				sa[1] = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				sa[2] = rs.getInt(3)+"";
				result.add(sa);				//���뵽�б���
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
	//����:����һ�����
	public static int createAlbum(String name,String u_no){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("insert into album(x_id,x_name,u_no) values(?,?,?)");
			ps.setInt(1, getMax(ALBUM));
			ps.setString(2, new String(name.getBytes(CHAR_ENCODING),"ISO-8859-1"));
			ps.setInt(3, Integer.valueOf(u_no));		//����Ԥ�������Ĳ���
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
	//������ת����ISO-8859-1
	public static String toISO(String s){
		String result = null;
		try {
			result = new String(s.getBytes(),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	//�����������û�����
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
	//��������ѯ�û����ռ��б�
	public static ArrayList<Diary> getUserDiary(String u_no,int currentPage, int span){
		ArrayList<Diary> result = new ArrayList<Diary>();
		Connection con = null;			//�������ݿ����Ӷ���
		Statement st = null;	//����������
		ResultSet rs = null;			//�������������
		int start = (currentPage-1)*span;		//������ʼλ��
		String sql = "select diary.r_id,diary.r_title,diary.r_content,date_format(diary.r_date,'%Y-%c-%e %k:%i:%s'),diary.u_no,user.u_name" +//�ռǱ��⡢�ռ����ݡ��ռ�ʱ�䡢�ռ������ߡ��ռ��������ǳ�
				" from diary,user where diary.u_no="+u_no+" and diary.u_no=user.u_no" +	
				" order by diary.r_date desc" + " limit "+start+","+span;			//����������
		try{
			con = getConnection();			//�������
			st = con.createStatement();		//�������
			rs = st.executeQuery(sql);		//ִ�в�ѯ 
			while(rs.next()){				//��ȡ����������ռǶ���
				String rid = rs.getInt(1)+"";
				String title = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String content = new String(rs.getString(3).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String date = rs.getString(4);
				String uno = rs.getInt(5)+"";
				String uname = new String(rs.getString(6).getBytes("ISO-8859-1"),CHAR_ENCODING);
				Diary d = new Diary(rid,title, content, uname, uno, date);
				result.add(d);
			}
			for(Diary d:result){		//Ϊÿ���ռ����������б�
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
	//���������ָ���ռǵ������б�
	public static ArrayList<Comments> getComments(String r_id){
		ArrayList<Comments> result = new ArrayList<Comments>();
		Connection con = null;			//����Connection����
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select date_format(comment.c_date,'%Y-%c-%e %k:%i:%s'),comment.c_content,user.u_name,comment.u_no" +
				" from comment,user where comment.r_id=? and user.u_no=comment.u_no order by comment.c_date desc";
		try{
			con = getConnection();		//�������
			ps = con.prepareStatement(sql);	//���Ԥ�������
			ps.setInt(1, Integer.valueOf(r_id));	//���ò���
			rs = ps.executeQuery();			//ִ�в�ѯ
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
	//�����������ݿ��в���ͼƬBlob����
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
			ps.setBinaryStream(4, fis,(int)(file.length()));		//ת��Ϊint��Ϊ�汾����
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
	//�����������ݿ��ȡ��Ӧ����ͼƬ����Ϣ
	public static ArrayList<PhotoInfo> getPhotoInfoByAlbum(String xid,int pageNo,int span){
		ArrayList<PhotoInfo> result = new ArrayList<PhotoInfo>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int start = span*(pageNo-1);		//������ʼλ��
		try{
			con = getConnection();
			ps = con.prepareStatement("select p_id,p_name,p_des,x_id from photo" +
					" where x_id=? order by p_id limit "+start+","+span);		//�������
			ps.setInt(1, Integer.valueOf(xid));		//���ò���
			rs = ps.executeQuery();
			while(rs.next()){		//���������
				String p_id = rs.getInt(1)+"";
				String p_name = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);	//��Ƭ����
				String p_des = new String(rs.getString(3).getBytes("ISO-8859-1"),CHAR_ENCODING);	//��Ƭ����
				String x_id = rs.getInt(4)+"";
				PhotoInfo p = new PhotoInfo(p_id, p_name, p_des, x_id);		//�ϴ�Photo����
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
	//�����������ݿ�����ȡBlob����
	public static Blob getPhotoBlob(String id){
		Blob result = null;
		Connection con = null;	//���ݿ����
		PreparedStatement ps = null;	//Ԥ�������
		ResultSet rs = null;		//�����
		try{
			con = getConnection();		//�������
			ps = con.prepareStatement("select p_data from photo where p_id=?");	//�������
			ps.setInt(1, Integer.valueOf(id));	//���ò���
			rs = ps.executeQuery();		//���ò���
			if(rs.next()){	//
				result = rs.getBlob(1);		//���Blob����
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
	//�����������ݿ���ȡ���ƶ����ĳ���
	public static int getAlbumSize(String xid){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();		//�������
			ps = con.prepareStatement("select count(*) as count from photo where x_id=?");
			ps.setInt(1, Integer.valueOf(xid));		//���ò���
			rs = ps.executeQuery();		//ִ�в�ѯ
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
	//���������ͷ���б�
	public static ArrayList<HeadInfo> getHeadList(int pageNo,int span){
		ArrayList<HeadInfo> result = new ArrayList<HeadInfo>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int start = (pageNo-1)*span;	//���㿪ʼλ��
		try{
			con = getConnection();	//������ݿ�����
			st = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery("select h_id,h_des,u_no from head limit "+start+","+span);
			while(rs.next()){
				String hid = rs.getInt(1)+"";
				String hdes = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String uno = rs.getInt(3)+"";
				HeadInfo hi = new HeadInfo(hid, hdes, uno);	//����HeadInfo����
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
	//���������ͷ���Blob����
	public static Blob getHeadBlob(String hid){
		Blob result = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();	//�������
			ps = con.prepareStatement("select h_data from head where h_id=?");
			ps.setInt(1, Integer.valueOf(hid));		//���ò���
			rs = ps.executeQuery();		//ִ�в�ѯ
			if(rs.next()){		//���ҽ����
				result = rs.getBlob(1);		//��ֵ
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
	//�����������ݿ��в���ͷ��Blob
	public static int insertHeadFile(File file,String hdes,String uno){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		FileInputStream fis = null;
		try{
			con = getConnection();		//������ݿ�����
			ps = con.prepareStatement("insert into head(h_id,h_des,h_data,u_no) values(?,?,?,?)");//���ò���
			int max = getMax(HEAD);
			ps.setInt(1, max);
			ps.setString(2, hdes);
			fis = new FileInputStream(file);
			ps.setBinaryStream(3, fis,(int)file.length());
			ps.setInt(4, Integer.valueOf(uno));
			result = ps.executeUpdate();		//ִ�в���
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
	//�����������û���ͷ��
	public static int changeUserHead(String uno,String hid){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("update user set h_id=? where u_no=?");
			ps.setInt(1, Integer.valueOf(hid));	//���ò���
			ps.setInt(2, Integer.valueOf(uno));	//���ò���
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
	//�������޸��û���Ϣ��������ͷ��
	public static int changeUserInfo(String uno,String uname,String uemail,String ustate){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();	//�������
			ps = con.prepareStatement("update user set u_name=?,u_email=?,u_state=? where u_no=?");	//�������
			ps.setString(1, new String(uname.getBytes("GBK"),"ISO-8859-1"));
			ps.setString(2, new String(uemail.getBytes("GBK"),"ISO-8859-1"));
			ps.setString(3, new String(ustate.getBytes("GBK"),"ISO-8859-1"));
			ps.setInt(4, Integer.valueOf(uno));	//
			result = ps.executeUpdate();		//ִ�и���
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
	//��������õ�ǰͷ�������
	public static int getHeadSize(){
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try{
			con = getConnection();		//�������
			st = con.createStatement();
			rs = st.executeQuery("select count(h_id) as count from head");	//ִ�в�ѯ
			if(rs.next()){		//�鵽����
				result = rs.getInt(1);	//��ȡ����
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
	//������ͨ��΢�����ҵ���Ӧ�û�
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
			while(rs.next()){		//���������
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
	//��������ȡ�ռ�����
	public static int getDiarySize(String u_no){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();
			ps = con.prepareStatement("select count(r_id) as count from diary where u_no=?");
			ps.setInt(1, Integer.valueOf(u_no));		//���ò���
			rs = ps.executeQuery();
			if(rs.next()){			//�鿴�����
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
	//��������ָ����־���һ������
	public static int addComment(String c_comment,String r_id,String u_no){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();	//������ݿ�����
			ps = con.prepareStatement("insert into comment(c_id,c_content,u_no,r_id) values(?,?,?,?)");
			ps.setInt(1, getMax(COMMENT));		//�����Զ���ŵ�ֵ
			ps.setString(2, new String(c_comment.getBytes(CHAR_ENCODING),"ISO-8859-1"));			//�������������ֶ�
			ps.setInt(3, Integer.valueOf(u_no));	//�����û����
			ps.setInt(4, Integer.valueOf(r_id));	//�����ռǱ��
			result = ps.executeUpdate();			//ִ�в������
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
	//���������ĳ���ǲ����Լ��ĺ���
	public static boolean checkIfMyFriend(String me,String stranger){
		boolean result = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();	//������ݿ�����
			ps = con.prepareStatement("select f_id from friend where u_noz=? and u_noy=?");	//����������
			ps.setInt(1, Integer.valueOf(me));	//���ô����Լ���ŵĲ���
			ps.setInt(2, Integer.valueOf(stranger));	//���ô���Է���ŵĲ���
			rs = ps.executeQuery();	//ִ�в�ѯ
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
	//��������ĳ����ӵ������б���ȥ
	public static int makeFriend(String me,String stranger){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs  = null;
		try{
			con = getConnection();		//������ݿ�����
			ps = con.prepareStatement("insert into friend(f_id,u_noz,u_noy) values(?,?,?)");
			ps.setInt(1, getMax(FEIEND));			//����id���
			ps.setInt(2, Integer.valueOf(me));		//��������˵�id
			ps.setInt(3, Integer.valueOf(stranger));	//���ñ�����˵�id
			result = ps.executeUpdate();		//ִ�в���
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
	//����������ָ���û�������ÿ�
	public static int addVisitor(String host,String visitor){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int hostId = Integer.valueOf(host);			//���˵�id
		int visitorId = Integer.valueOf(visitor);	//�����ߵ�id
		try{
			con = getConnection();		//������ݿ�����
			//���Ȳ鿴Visitor�Ƿ�����
			ps = con.prepareStatement("select v_no from visit where u_no=? and v_no=?");
			ps.setInt(1, hostId);	//��������id
			ps.setInt(2, visitorId);
			rs = ps.executeQuery();
			if(rs.next()){			//��ȡ���������
				ps = con.prepareStatement("update visit set v_date=now() where u_no=? and v_no=?");
				ps.setInt(1, hostId);		//��������id
				ps.setInt(2, visitorId);	//���÷ÿ�id
				result = ps.executeUpdate();	//ִ�и���
			}
			else{					//���µ��Ǹ��ÿͺ͵�ǰ�ÿͲ���ͬ
				ps = con.prepareStatement("insert into visit(v_id,u_no,v_no) values(?,?,?)");
				ps.setInt(1, getMax(VISIT));				//��������ֵ
				ps.setInt(2, Integer.valueOf(host));		//��������id
				ps.setInt(3, Integer.valueOf(visitor));		//���÷ÿ�id
				result = ps.executeUpdate();				//ִ�в�ѯ				
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
	//����������û��ķÿ��б�
	public static ArrayList<Visitor> getVisitors(String uno){
		ArrayList<Visitor> result = new ArrayList<Visitor>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = getConnection();		//������ݿ�����
			ps = con.prepareStatement("select user.u_no,user.u_name,user.h_id,date_format(visit.v_date,'%Y-%c-%e %k:%i:%s') from user,visit" +
					" where user.u_no=visit.v_no and visit.u_no=? order by visit.v_date desc");	//�ǳơ�ͷ��ʱ��
			ps.setInt(1, Integer.valueOf(uno));
			rs = ps.executeQuery();			//ִ�в�ѯ
			while(rs.next()){				//���������
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
	//������ɾ��ָ���ռ�
	public static int deleteDiary(String rid){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			deleteAllCommentByDiary(rid);			//��ɾ������
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
	//������ɾ��ָ����־����������
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
	//������ɾ��ָ��ͼƬ
	public static int deletePhoto(String pid){
		int result = -1;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			deleteAllCommentByPhoto(pid);
			con = getConnection();		//�������
			ps = con.prepareStatement("delete from photo where p_id=?");
			ps.setInt(1, Integer.valueOf(pid));		//����ɾ������Ƭ��id
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
	//������ɾ��ָ��ͼƬ����������
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
	//��������������
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
			
			while(rs.next()){			//���������
				String uno = rs.getInt(1)+"";
				String uname = new String(rs.getString(2).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String uemail = new String(rs.getString(3).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String ustate = new String(rs.getString(4).getBytes("ISO-8859-1"),CHAR_ENCODING);
				String hid = new String(rs.getString(5).getBytes("ISO-8859-1"),CHAR_ENCODING);
				User u = new User(uno, uname, uemail, ustate, hid);		//����User����
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
	//����������µ�ͼƬ����
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
	//���������ָ��ͼƬ������
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
	//�����������ݿ������Ƭ
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
			ps.setBinaryStream(4, in,(int)(in.available()));		//ת��Ϊint��Ϊ�汾����
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
	//�������޸�����Ȩ��
	public static int changeAlbumAccess(String xid,String newAccess){
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = getConnection();		//������ݿ�����
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
	//�������޸�ָ����־
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
	//��������ѯָ������Ȩ��
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
	
	//main���������ڲ���
	public static void main(String args[]){
//		System.out.println(getConnection());
//		System.out.println("��һ��p_comment:"+getMax(P_COMMENT));
//		System.out.println("�ڶ���USER:"+getMax(USER));
//		System.out.println("������USER:"+getMax(USER));
//		System.out.println("ע���һ����"+registerUser("����", "123", "wpftool@126.com", "������������","0"));
//		getFiendList("2008");
//		System.out.println(deleteFriend("2008", "2030"));
//		System.out.println(updateState("2008", "�Һ�����Lve"));
//		ArrayList<Diary> al = getUserDiary("2008", 1, 5);
//		for(Diary d:al){
//			System.out.println(d.rid+"\t"+d.title+"\t"+d.content+"\t"+d.time+"\t"+d.u_name+"\t"+d.u_no);
//			System.out.println("����");
//			for(Comments c:d.commentList){
//				System.out.println(c.cmtNo+"\t"+c.cmtName+"\t"+c.content+"\t"+c.date);
//			}
//			System.out.println();
//		}
//		ArrayList<String []> al = getAlbumList("2008");
//		for(String [] sa:al){
//			System.out.println(sa[0]+"\t"+sa[1]);
//		}
//		System.out.println(createAlbum("�Һʹ�", "2008"));
//		System.out.println(getAlbumSize("1"));
//		System.out.println(getHeadList(1,4).size());
//		System.out.println(getHeadBlob("3"));
//		System.out.println(changeUserHead("2008","3"));
//		System.out.println(changeUserInfo("2008","�ɷ�","wpf@wpf.com","������䰡��"));
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
//		System.out.println(searchFriendByName("��").size());
//		ArrayList<User> list = searchFriendByName("��");
//		for(User u:list){
//			System.out.println(u.u_name);
//		}
////		System.out.println(getComments("7").size());
//		System.out.println(addPhotoComment("�������ĵģ���", "8", "2009"));
//		System.out.println(getPhotoComment("8").size());
//		System.out.println(getAlbumList("2008").get(0)[2]);
//		System.out.println(changeAlbumAccess("9", "1"));
//		System.out.println(deleteAllCommentByDiary("46"));
//		System.out.println(modifyDiary("4","���μ�","�����ֿ����ˡ�"));
//		System.out.println(deleteAllCommentByPhoto("8"));
//		System.out.println(deletePhoto("35"));
		System.out.println(getAlbumAccess("5"));
	}
}
