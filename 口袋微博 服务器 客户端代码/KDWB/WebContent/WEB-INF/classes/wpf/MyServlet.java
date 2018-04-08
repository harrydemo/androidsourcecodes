package wpf;
import static wpf.ConstantUtil.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
						throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
						throws ServletException, IOException {
		request.setCharacterEncoding(CHAR_ENCODING);			//设置编码
		String action = (String)request.getParameter("action");	//获取action
		System.out.println("MyServlet===== action:"+action);
		if(action.equals("login")){		//action为登录信息
			String u_no = (String)request.getParameter("u_no");			//读取密码参数
			String u_pwd = (String)request.getParameter("u_pwd");		//读取密码参数
			ArrayList<String> result = DBUtil.checkLogin(u_no, u_pwd);	//查询数据库
			if(result.size()>1){					//如果列表长度大于1，表示登录成功
				HttpSession session = request.getSession();
				String no = result.get(0);			//获得用户的号码
				String name = new String(result.get(1).getBytes("ISO-8859-1"),CHAR_ENCODING);		//获得用户的昵称
				String email = result.get(2);		//获取用户电子邮件
				String state = new String(result.get(3).getBytes("ISO-8859-1"),CHAR_ENCODING);	//获取用户状态
				String hid = result.get(4);			//获取用户头像
				User user = new User(no, name, email, state, hid);
				session.setAttribute("user", user);	//将用户id加入Session
			}
			else{
				request.setAttribute("loginResult", result.get(0));
			}
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
		else if(action.equals("register")){		//action为注册信息
			String u_name = (String)request.getParameter("u_name");
			String u_pwd = (String)request.getParameter("u_pwd");
			String u_email = (String)request.getParameter("u_email");
			String u_state = (String)request.getParameter("u_state");
			
			String result = DBUtil.registerUser(u_name, u_pwd, u_email, u_state,"0");
			if(!result.equals(REGISTER_FAIL)){		//注册成功
				User user = new User(result, u_name, u_email, u_state, "0");
				HttpSession session = request.getSession();
				session.setAttribute("user", user);				
			}
			request.setAttribute("result", result);
			request.getRequestDispatcher("register.jsp").forward(request,response);
		}
		else if(action.equals("logout")){		//action为注销登陆
			HttpSession session = request.getSession();
			session.setAttribute("user", null);
			request.getRequestDispatcher("login.jsp").forward(request,response);
		}
		else if(action.equals("new_diary")){	//action为写新日记
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			if(user == null){			//用户没有登录
				request.setAttribute("result", DIARY_FAIL);
				request.getRequestDispatcher("write.jsp").forward(request, response);
				return;
			}
			String u_no = user.u_no;			//获得用户的id
			String title = (String)request.getParameter("title");
			String content = (String)request.getParameter("content");
			String result = DBUtil.writeNewDiary(title, content, u_no);
			request.setAttribute("result", result);			//将结果设置到request的属性中
			request.getRequestDispatcher("write.jsp").forward(request, response);//返回
		}
		else if(action.equals("gotoregister")){		//action为注册
			System.out.println("hahahahhh");
			request.getRequestDispatcher("register.jsp").forward(request, response);
		}
		else if(action.equals("delete")){					//action为删除好友
			String u_no = (String)request.getParameter("u_no");
			String u_noToDelete = (String)request.getParameter("u_noToDelete");
			DBUtil.deleteFriend(u_no, u_noToDelete);
			request.getRequestDispatcher("friend.jsp").forward(request, response);
		}
		else if(action.equals("new_state")){				//action为更新心情
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			if(user == null){			//用户没有登录
				request.setAttribute("result", UPDATE_STATE_FAIL);
				request.getRequestDispatcher("write.jsp").forward(request, response);
				return;
			}
			String u_no = user.u_no;			//获得用户的id
			String content = (String)request.getParameter("content");
			String result = DBUtil.updateState(u_no, content);		//更新心情
			user.u_state=content;
			session.setAttribute("user", user);			//更新Session的内容
			request.setAttribute("result", result);			//将结果设置到request的属性中
			request.getRequestDispatcher("write.jsp").forward(request, response);//返回
		}
		else if(action.equals("createAlbum")){				//action为创建相册
			String albumName = request.getParameter("albumName");
			String u_no = request.getParameter("u_no");		//获取相册所属者id
			if(DBUtil.createAlbum(albumName, u_no) == 1){		//创建成功
				request.setAttribute("result", CREATE_ALBUM_SUCESS);
			}
			else{
				request.setAttribute("result", CREATE_ALBUM_FAIL);
			}
			request.getRequestDispatcher("uploadImage.jsp").forward(request, response);
		}
		else if(action.equals("modify")){				//action为转到修改资料页面
			request.getRequestDispatcher("personalInfo.jsp").forward(request,response);
			
		}
		else if(action.equals("changeHead")){		//action为修改头像
			String hid = request.getParameter("hid");	//读取参数
			String uno = request.getParameter("uno");	//读取参数
			if(DBUtil.changeUserHead(uno, hid) == 1){		//修改成功
				HttpSession session = request.getSession();
				User u = (User)session.getAttribute("user");
				u.h_id=hid;
				session.setAttribute("user", u);
			}
			request.getRequestDispatcher("personalInfo.jsp").forward(request, response);
		}
		else if(action.equals("changeInfo")){			//action为修改用户信息
			String uname = request.getParameter("uname");	//获得昵称
			String uemail = request.getParameter("uemail");	//获得邮箱地址
			String ustate = request.getParameter("ustate");	//获得心情
			HttpSession session = request.getSession();		//获得Session
			User user = (User)session.getAttribute("user");	//提取出Session对象
			String uno = user.u_no;
			int result = DBUtil.changeUserInfo(uno, uname, uemail, ustate);
			if(result == 1){		//更改成功
				user.u_name = uname;
				user.u_email = uemail;
				user.u_state = ustate;
				session.setAttribute("user", user);
			}
			request.setAttribute("changeInfoResult", result);
			request.getRequestDispatcher("personalInfo.jsp").forward(request, response);//跳转
		}
		else if(action.equals("seeDiary")){		//action为查看日记
			HttpSession session = request.getSession();		//获取Session
			User user = (User)session.getAttribute("user");	//获得User对象
			if(user != null){
				request.setAttribute("u_no", user.u_no);
			}
			request.getRequestDispatcher("diary.jsp").forward(request, response);
		}
		else if(action.equals("seeAlbum")){			//action为查看相册
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			if(user!=null){
				request.setAttribute("u_no", user.u_no);	//设置返回属性
			}
			request.getRequestDispatcher("album.jsp").forward(request, response);
		}
		else if(action.equals("makeComment")){		//action为发表评论
			String c_content = request.getParameter("comment");
			String r_id = request.getParameter("r_id");
			String visitor = request.getParameter("visitor");
			int result = DBUtil.addComment(c_content, r_id, visitor);
			if(result == 1){		//添加评论成功
				request.getRequestDispatcher("diary.jsp").forward(request, response);
			}
		}
		else if(action.equals("toFriendPage")){					//action为去好友的微博
			HttpSession session = request.getSession();		//获得Session
			User user = (User)session.getAttribute("user");	//获得登录用户
			String visitor = user.u_no;						//访问者的id
			String u_no = (String)request.getParameter("uno");
			DBUtil.addVisitor(u_no, visitor);		//添加最近访客
			request.getRequestDispatcher("friendPage.jsp").forward(request, response);
		}
		else if(action.equals("deleteDiary")){			//action为删除指定日志
			String rid=request.getParameter("r_id");
			int result = DBUtil.deleteDiary(rid);
			request.getRequestDispatcher("diary.jsp").forward(request, response);
			
		}
		else if(action.equals("deletePhoto")){				//action为删除指定图片
			String pid = request.getParameter("p_id");
			int result = DBUtil.deletePhoto(pid);
			request.getRequestDispatcher("album.jsp").forward(request, response);
		}
		else if(action.equals("searchFriend")){						//action为搜索好友
			request.getRequestDispatcher("searchList.jsp").forward(request, response);
		}
		else if(action.equals("makeFriend")){						//action为添加好友
			String me = request.getParameter("me");				//自己的id
			String stranger = request.getParameter("stranger");	//陌生人的id
			int result = DBUtil.makeFriend(me, stranger);			//交朋友
			request.getRequestDispatcher("searchList.jsp").forward(request, response);
		}
		else if(action.equals("addPhotoComment")){				//action为addPhotoComment
			String content = request.getParameter("content");
			String uno = request.getParameter("u_no");
			String pid = request.getParameter("p_id");
			int result = DBUtil.addPhotoComment(content, pid, uno);
			if(result == 1){
				request.getRequestDispatcher("album.jsp").forward(request, response);
			}
		}
		else if(action.equals("change_album_access")){			//action为change_album_access
			String xid = request.getParameter("xid");
			String access = request.getParameter("album_access");
			System.out.println("xid  -- "+xid+" access:"+access);
			DBUtil.changeAlbumAccess(xid, access);
			request.getRequestDispatcher("album.jsp").forward(request, response);
		}
		else if(action.equals("toModifyDiary")){			//action为去修改日志的页面
			request.getRequestDispatcher("modifyDiary.jsp").forward(request, response);
		}
		else if(action.equals("modifyDiary")){				//action为修改日志
			String rid = request.getParameter("r_id");
			String rtitle = request.getParameter("r_title");
			String rcontent = request.getParameter("r_content");
			int result = DBUtil.modifyDiary(rid, rtitle, rcontent);
			request.setAttribute("result", result);
			request.getRequestDispatcher("modifyDiary.jsp").forward(request, response);
		}
	}

}
