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
		request.setCharacterEncoding(CHAR_ENCODING);			//���ñ���
		String action = (String)request.getParameter("action");	//��ȡaction
		System.out.println("MyServlet===== action:"+action);
		if(action.equals("login")){		//actionΪ��¼��Ϣ
			String u_no = (String)request.getParameter("u_no");			//��ȡ�������
			String u_pwd = (String)request.getParameter("u_pwd");		//��ȡ�������
			ArrayList<String> result = DBUtil.checkLogin(u_no, u_pwd);	//��ѯ���ݿ�
			if(result.size()>1){					//����б��ȴ���1����ʾ��¼�ɹ�
				HttpSession session = request.getSession();
				String no = result.get(0);			//����û��ĺ���
				String name = new String(result.get(1).getBytes("ISO-8859-1"),CHAR_ENCODING);		//����û����ǳ�
				String email = result.get(2);		//��ȡ�û������ʼ�
				String state = new String(result.get(3).getBytes("ISO-8859-1"),CHAR_ENCODING);	//��ȡ�û�״̬
				String hid = result.get(4);			//��ȡ�û�ͷ��
				User user = new User(no, name, email, state, hid);
				session.setAttribute("user", user);	//���û�id����Session
			}
			else{
				request.setAttribute("loginResult", result.get(0));
			}
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
		else if(action.equals("register")){		//actionΪע����Ϣ
			String u_name = (String)request.getParameter("u_name");
			String u_pwd = (String)request.getParameter("u_pwd");
			String u_email = (String)request.getParameter("u_email");
			String u_state = (String)request.getParameter("u_state");
			
			String result = DBUtil.registerUser(u_name, u_pwd, u_email, u_state,"0");
			if(!result.equals(REGISTER_FAIL)){		//ע��ɹ�
				User user = new User(result, u_name, u_email, u_state, "0");
				HttpSession session = request.getSession();
				session.setAttribute("user", user);				
			}
			request.setAttribute("result", result);
			request.getRequestDispatcher("register.jsp").forward(request,response);
		}
		else if(action.equals("logout")){		//actionΪע����½
			HttpSession session = request.getSession();
			session.setAttribute("user", null);
			request.getRequestDispatcher("login.jsp").forward(request,response);
		}
		else if(action.equals("new_diary")){	//actionΪд���ռ�
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			if(user == null){			//�û�û�е�¼
				request.setAttribute("result", DIARY_FAIL);
				request.getRequestDispatcher("write.jsp").forward(request, response);
				return;
			}
			String u_no = user.u_no;			//����û���id
			String title = (String)request.getParameter("title");
			String content = (String)request.getParameter("content");
			String result = DBUtil.writeNewDiary(title, content, u_no);
			request.setAttribute("result", result);			//��������õ�request��������
			request.getRequestDispatcher("write.jsp").forward(request, response);//����
		}
		else if(action.equals("gotoregister")){		//actionΪע��
			System.out.println("hahahahhh");
			request.getRequestDispatcher("register.jsp").forward(request, response);
		}
		else if(action.equals("delete")){					//actionΪɾ������
			String u_no = (String)request.getParameter("u_no");
			String u_noToDelete = (String)request.getParameter("u_noToDelete");
			DBUtil.deleteFriend(u_no, u_noToDelete);
			request.getRequestDispatcher("friend.jsp").forward(request, response);
		}
		else if(action.equals("new_state")){				//actionΪ��������
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			if(user == null){			//�û�û�е�¼
				request.setAttribute("result", UPDATE_STATE_FAIL);
				request.getRequestDispatcher("write.jsp").forward(request, response);
				return;
			}
			String u_no = user.u_no;			//����û���id
			String content = (String)request.getParameter("content");
			String result = DBUtil.updateState(u_no, content);		//��������
			user.u_state=content;
			session.setAttribute("user", user);			//����Session������
			request.setAttribute("result", result);			//��������õ�request��������
			request.getRequestDispatcher("write.jsp").forward(request, response);//����
		}
		else if(action.equals("createAlbum")){				//actionΪ�������
			String albumName = request.getParameter("albumName");
			String u_no = request.getParameter("u_no");		//��ȡ���������id
			if(DBUtil.createAlbum(albumName, u_no) == 1){		//�����ɹ�
				request.setAttribute("result", CREATE_ALBUM_SUCESS);
			}
			else{
				request.setAttribute("result", CREATE_ALBUM_FAIL);
			}
			request.getRequestDispatcher("uploadImage.jsp").forward(request, response);
		}
		else if(action.equals("modify")){				//actionΪת���޸�����ҳ��
			request.getRequestDispatcher("personalInfo.jsp").forward(request,response);
			
		}
		else if(action.equals("changeHead")){		//actionΪ�޸�ͷ��
			String hid = request.getParameter("hid");	//��ȡ����
			String uno = request.getParameter("uno");	//��ȡ����
			if(DBUtil.changeUserHead(uno, hid) == 1){		//�޸ĳɹ�
				HttpSession session = request.getSession();
				User u = (User)session.getAttribute("user");
				u.h_id=hid;
				session.setAttribute("user", u);
			}
			request.getRequestDispatcher("personalInfo.jsp").forward(request, response);
		}
		else if(action.equals("changeInfo")){			//actionΪ�޸��û���Ϣ
			String uname = request.getParameter("uname");	//����ǳ�
			String uemail = request.getParameter("uemail");	//��������ַ
			String ustate = request.getParameter("ustate");	//�������
			HttpSession session = request.getSession();		//���Session
			User user = (User)session.getAttribute("user");	//��ȡ��Session����
			String uno = user.u_no;
			int result = DBUtil.changeUserInfo(uno, uname, uemail, ustate);
			if(result == 1){		//���ĳɹ�
				user.u_name = uname;
				user.u_email = uemail;
				user.u_state = ustate;
				session.setAttribute("user", user);
			}
			request.setAttribute("changeInfoResult", result);
			request.getRequestDispatcher("personalInfo.jsp").forward(request, response);//��ת
		}
		else if(action.equals("seeDiary")){		//actionΪ�鿴�ռ�
			HttpSession session = request.getSession();		//��ȡSession
			User user = (User)session.getAttribute("user");	//���User����
			if(user != null){
				request.setAttribute("u_no", user.u_no);
			}
			request.getRequestDispatcher("diary.jsp").forward(request, response);
		}
		else if(action.equals("seeAlbum")){			//actionΪ�鿴���
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			if(user!=null){
				request.setAttribute("u_no", user.u_no);	//���÷�������
			}
			request.getRequestDispatcher("album.jsp").forward(request, response);
		}
		else if(action.equals("makeComment")){		//actionΪ��������
			String c_content = request.getParameter("comment");
			String r_id = request.getParameter("r_id");
			String visitor = request.getParameter("visitor");
			int result = DBUtil.addComment(c_content, r_id, visitor);
			if(result == 1){		//������۳ɹ�
				request.getRequestDispatcher("diary.jsp").forward(request, response);
			}
		}
		else if(action.equals("toFriendPage")){					//actionΪȥ���ѵ�΢��
			HttpSession session = request.getSession();		//���Session
			User user = (User)session.getAttribute("user");	//��õ�¼�û�
			String visitor = user.u_no;						//�����ߵ�id
			String u_no = (String)request.getParameter("uno");
			DBUtil.addVisitor(u_no, visitor);		//�������ÿ�
			request.getRequestDispatcher("friendPage.jsp").forward(request, response);
		}
		else if(action.equals("deleteDiary")){			//actionΪɾ��ָ����־
			String rid=request.getParameter("r_id");
			int result = DBUtil.deleteDiary(rid);
			request.getRequestDispatcher("diary.jsp").forward(request, response);
			
		}
		else if(action.equals("deletePhoto")){				//actionΪɾ��ָ��ͼƬ
			String pid = request.getParameter("p_id");
			int result = DBUtil.deletePhoto(pid);
			request.getRequestDispatcher("album.jsp").forward(request, response);
		}
		else if(action.equals("searchFriend")){						//actionΪ��������
			request.getRequestDispatcher("searchList.jsp").forward(request, response);
		}
		else if(action.equals("makeFriend")){						//actionΪ��Ӻ���
			String me = request.getParameter("me");				//�Լ���id
			String stranger = request.getParameter("stranger");	//İ���˵�id
			int result = DBUtil.makeFriend(me, stranger);			//������
			request.getRequestDispatcher("searchList.jsp").forward(request, response);
		}
		else if(action.equals("addPhotoComment")){				//actionΪaddPhotoComment
			String content = request.getParameter("content");
			String uno = request.getParameter("u_no");
			String pid = request.getParameter("p_id");
			int result = DBUtil.addPhotoComment(content, pid, uno);
			if(result == 1){
				request.getRequestDispatcher("album.jsp").forward(request, response);
			}
		}
		else if(action.equals("change_album_access")){			//actionΪchange_album_access
			String xid = request.getParameter("xid");
			String access = request.getParameter("album_access");
			System.out.println("xid  -- "+xid+" access:"+access);
			DBUtil.changeAlbumAccess(xid, access);
			request.getRequestDispatcher("album.jsp").forward(request, response);
		}
		else if(action.equals("toModifyDiary")){			//actionΪȥ�޸���־��ҳ��
			request.getRequestDispatcher("modifyDiary.jsp").forward(request, response);
		}
		else if(action.equals("modifyDiary")){				//actionΪ�޸���־
			String rid = request.getParameter("r_id");
			String rtitle = request.getParameter("r_title");
			String rcontent = request.getParameter("r_content");
			int result = DBUtil.modifyDiary(rid, rtitle, rcontent);
			request.setAttribute("result", result);
			request.getRequestDispatcher("modifyDiary.jsp").forward(request, response);
		}
	}

}
