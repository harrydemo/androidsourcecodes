package wpf;
import static wpf.ConstantUtil.DIARY_SUCCESS;
import static wpf.ConstantUtil.REGISTER_FAIL;
import static wpf.ConstantUtil.UPDATE_STATE_SUCCESS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
public class ServerAgent extends Thread{
	public Socket socket;
	public DataInputStream din;
	public DataOutputStream dout;
	boolean flag = false;
	
	public ServerAgent(Socket socket){
		this.socket = socket;
		try {
			this.din = new DataInputStream(socket.getInputStream());
			this.dout = new DataOutputStream(socket.getOutputStream());
			flag =true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//方法：线程执行方法
	public void run(){
		while(flag){
			try {
				String msg = din.readUTF();			//接收客户端发来的消息
//				System.out.println("收到的消息是："+msg);	
				if(msg.startsWith("<#LOGIN#>")){				//消息为登录
					String content = msg.substring(9);			//获得消息内容
					String [] sa = content.split("\\|");
					ArrayList<String> result = DBUtil.checkLogin(sa[0], sa[1]);
					if(result.size()>1){			//登录成功
						StringBuilder sb = new StringBuilder();
						sb.append("<#LOGIN_SUCCESS#>");
						for(String s:result){
							sb.append(s);
							sb.append("|");
						}
						String loginInfo = sb.substring(0,sb.length()-1);
						dout.writeUTF(loginInfo);			//返回用户的基本信息			
					}
					else{				//登录失败
						String loginInfo = "<#LOGIN_FAIL#>"+result.get(0);
						dout.writeUTF(loginInfo);
					}
				}
				else if(msg.startsWith("<#USER_LOGOUT#>")){			//消息为用户登出
					this.din.close();
					this.dout.close();
					this.flag = false;
					this.socket.close();
					this.socket = null;
				}
				else if(msg.startsWith("<#REGISTER#>")){			//消息为用户注册
					msg = msg.substring(12);	//获得字符串值
					String [] sa = msg.split("\\|");	//切割字符串
					String regResult = DBUtil.registerUser(sa[0], sa[1], sa[2], sa[3], "1");
					if(regResult.equals(REGISTER_FAIL)){		//注册失败
						dout.writeUTF("<#REG_FAIL#>");
					}
					else{
						dout.writeUTF("<#REG_SUCCESS#>"+regResult);
					}
				}
				else if(msg.startsWith("<#NEW_DIARY#>")){					//消息为发布新日记
					msg = msg.substring(13);				//获得消息内容
					String [] sa = msg.split("\\|");		//获得字符串数组
					String result = DBUtil.writeNewDiary(sa[0], sa[1], sa[2]);
					String reply = "";
					if(result.equals(DIARY_SUCCESS)){			//日志发布成功
						reply = "<#DIARY_SUCCESS#>";
					}
					else {
						reply = "<#DIARY_FAIL#>";
					}
					dout.writeUTF(reply);					//发出回复消息
				}
				else if(msg.startsWith("<#NEW_STATUS#>")){				//消息为更新心情
					msg = msg.substring(14);			//提取内容
					String [] sa = msg.split("\\|");	//切割字符串
					String result = DBUtil.updateState(sa[1], sa[0]);	//
					if(result.equals(UPDATE_STATE_SUCCESS)){		//如果更新心情成功
						dout.writeUTF("<#STATUS_SUCCESS#>");
					}
					else{
						dout.writeUTF("<#STATUS_FAIL#>");			//发出心情更新失败消息
					}
				}
				else if(msg.startsWith("<#FRIEND_LIST#>")){			//消息为获得好友列表
					msg = msg.substring(15);
					ArrayList<User> list = DBUtil.getFiendList(msg);	//获得好友的列表
					dout.writeInt(list.size());		//告知客户端好友列表的长度
					for(int i=0;i<list.size();i++){
						User u = list.get(i);			//获得该处的User对象
						StringBuilder sb = new StringBuilder();	//创建StringBuilder
						sb.append(u.u_no);
						sb.append("|");
						sb.append(u.u_name);
						sb.append("|");
						sb.append(u.u_email);
						sb.append("|");
						sb.append(u.u_state);
						sb.append("|");
						sb.append(u.h_id);
						dout.writeUTF(sb.toString());			//发出好友信息
						Blob blob = DBUtil.getHeadBlob(u.h_id);	//获得指定用户的头像Blob
						byte [] buf = blob.getBytes(1l, (int)blob.length());	//获得字节数组
						dout.writeInt(buf.length);
						dout.write(buf);
						dout.flush();				//不知道加上这个有永不
					}
				}else if(msg.startsWith("<#VISITOR_LIST#>")){					//消息为显示最近访客请求
					msg = msg.substring(16);			//提取消息内容
					ArrayList<Visitor> visitorList = DBUtil.getVisitors(msg);		//获取访客列表
					int size = visitorList.size();
					dout.writeInt(size);			//告知客户端数据的长度
					for(int i=0;i<size;i++){		//遍历访客列表
						StringBuilder sb = new StringBuilder();
						Visitor v = visitorList.get(i);
						sb.append(v.v_no);
						sb.append("|");
						sb.append(v.v_name);
						sb.append("|");
						sb.append(v.v_date);
						sb.append("|");
						sb.append(v.h_id);
						dout.writeUTF(sb.toString());
						Blob blob = DBUtil.getHeadBlob(v.h_id);	//获得头像Blob数据
						byte [] buf = blob.getBytes(1l, (int)blob.length());	//获取字节数组
						dout.writeInt(buf.length);
						dout.write(buf);		//将字节数组发出
						dout.flush();
					}
				}
				else if(msg.startsWith("<#GET_DIARY#>")){			//消息为获得日记列表
					msg = msg.substring(13);					//提取消息内容
					String [] sa = msg.split("\\|");			//分隔字符串
					ArrayList<Diary> diaryList = DBUtil.getUserDiary(sa[0], Integer.valueOf(sa[1]), 5);
					int size = diaryList.size();
					dout.writeInt(size);						//向客户端发送日记长度
					for(int i=0;i<size;i++){
						StringBuilder sb = new StringBuilder();
						Diary d= diaryList.get(i);
						sb.append(d.rid);			//日志id
						sb.append("|");
						sb.append(d.title);		//日志标题
						sb.append("|");
						sb.append(d.content);		//日志内容
						sb.append("|");
						sb.append(d.time);			//日志发表时间
						dout.writeUTF(sb.toString());		//发出日记列表
					}
				}
				else if(msg.startsWith("<#GET_ALBUM_LIST#>")){			//消息为获取相册列表
					msg = msg.substring(18);			//提取内容
					ArrayList<String []> albumList = DBUtil.getAlbumList(msg);
					if(albumList.size() == 0){//如果用户无相册
						dout.writeUTF("<#NO_ALBUM#>");
					}
					else{			//用户相册列表不为空
						StringBuilder sb = new StringBuilder();
						for(String [] sa:albumList){
							sb.append(sa[0]);
							sb.append("|");
							sb.append(sa[1]);
							sb.append("|");
							sb.append(sa[2]);
							sb.append("$");
						}
						dout.writeUTF(sb.substring(0, sb.length()-1));						
					}
				}
				else if(msg.startsWith("<#GET_ALBUM#>")){				//消息为获得指定相册
					msg = msg.substring(13);		//提取内容
					int albumSize = DBUtil.getAlbumSize(msg);		//获取相册长度
					dout.writeInt(albumSize);					//把相册长度发给客户端
					List<PhotoInfo> photoList = DBUtil.getPhotoInfoByAlbum(msg, 1, albumSize);//获取图片信息
					for(int i=0;i<albumSize;i++){				//循环获取图片数据
						PhotoInfo pi = photoList.get(i);		//获得图片信息
						StringBuilder sb = new StringBuilder();
						sb.append(pi.p_id);
						sb.append("|");
						sb.append(pi.p_name);
						sb.append("|");
						sb.append(pi.p_des);
						sb.append("|");
						sb.append(pi.x_id);
						dout.writeUTF(sb.toString());
						Blob blob = DBUtil.getPhotoBlob(pi.p_id);		//获得图片Blob
						byte [] buf = blob.getBytes(1l, (int)blob.length());		//获得字节数组
						dout.writeInt(buf.length);				//告知客户端数组长度
						dout.write(buf);
						dout.flush();
					}						
				}
				else if(msg.startsWith("<#NEW_ALBUM#>")){				//消息为创建新相册
					msg = msg.substring(13);				//提取内容
					String [] sa = msg.split("\\|");		//分割字符串
					int result = DBUtil.createAlbum(sa[0], sa[1]);		//创建新相册
					if(result == 1){		//创建成功
						dout.writeUTF("<#NEW_ALBUM_SUCCESS#>");		//发回成功消息
					}
					else{
						dout.writeUTF("<#NEW_ALBUM_FAIL#>");			//发回创建失败消息
					}
				}
				else if(msg.startsWith("<#NEW_PHOTO#>")){			//消息为上传照片
					msg = msg.substring(13);			//提取内容
					String [] sa = msg.split("\\|");		//分隔字符串
					int size = din.readInt();			//读取图片大小
					byte [] buf = new byte[size];		//创建字节数组
					for(int i=0;i<size;i++){
						buf[i] = din.readByte();
					}
					int result = DBUtil.insertPhotoFromAndroid(buf, sa[0], sa[1], sa[2]);	//插入图片
					if(result == 1){
						dout.writeUTF("<#NEW_PHOTO_SUCCESS#>");		//返回上传成功消息
					}
					else{
						dout.writeUTF("<#NEW_PHOTO_FAIL#>");		//返回上传失败的消息
					}
				}
				else if(msg.startsWith("<#GET_COMMENT#>")){			//消息为获取指定日志的评论列表
					msg = msg.substring(15);						//提取内容
					ArrayList<Comments> cmtList = DBUtil.getComments(msg);	//获得评论列表
					int size = cmtList.size();			//评论的个数
					dout.writeInt(size);				//返回评论的个数
					String reply = din.readUTF();		//等待客户端反馈
					if(reply.equals("<#READY_TO_READ_COMMENT#>")){	//如果客户端已经准备好
						for(Comments c:cmtList){
							StringBuilder sb = new StringBuilder();
							sb.append(c.cmtNo);
							sb.append("|");
							sb.append(c.cmtName);
							sb.append("|");
							sb.append(c.content);
							sb.append("|");
							sb.append(c.date);
							dout.writeUTF(sb.toString());		//发送消息到客户端
						}
					}
				}
				else if(msg.startsWith("<#NEW_COMMENT#>")){		//消息为添加评论
					msg = msg.substring(15);
					String [] sa = msg.split("\\|");
					int result = DBUtil.addComment(sa[0], sa[1], sa[2]);
					if(result == 1){
						dout.writeUTF("<#NEW_COMMENT_SUCESS#>");
					}
					else{
						dout.writeUTF("<#NEW_COMMENT_FAIL#>");
					}
				}
				else if(msg.startsWith("<#SEARCH_CONTACT#>")){					//消息为搜索联系人
					msg = msg.substring(18);			//提取内容
					ArrayList<User> result = DBUtil.searchFriendByName(msg);
					int size = result.size();
					dout.writeInt(size);				//告知客户端搜索结果数
					if(size >0){			//如果搜索结果不为空
							for(int i=0;i<size;i++){
								StringBuffer sb = new StringBuffer();
								User u = result.get(i);
								sb.append(u.u_no);
								sb.append("|");
								sb.append(u.u_name);
								sb.append("|");
								sb.append(u.u_email);
								sb.append("|");
								sb.append(u.u_state);
								sb.append("|");
								sb.append(u.h_id);
								dout.writeUTF(sb.toString());			//发出微博用户信息
								Blob blob = DBUtil.getHeadBlob(u.h_id);		//获得头像Blob
								byte [] buf = blob.getBytes(1l, (int)blob.length());	//字节数组
								dout.writeInt(buf.length);			//发出字节数组长度
								dout.write(buf);
							}
//						}
					}
				}
				else if(msg.startsWith("<#CHANGE_ALBUM_ACCESS#>")){		//消息为修改相册权限
					msg = msg.substring(23);
					String [] sa = msg.split("\\|");		//分割字符串
					int result = DBUtil.changeAlbumAccess(sa[0], sa[1]);	//修改权限
					if(result == 1){		//修改成功
						dout.writeUTF("<#ALBUM_ACCESS_SUCCESS#>");
					}
					else{
						dout.writeUTF("<#ALBUM_ACCESS_FAIL#>");
					}
				}
				else if(msg.startsWith("<#DELETE_DIARY#>")){		//消息为删除日志
					msg = msg.substring(16);		//提取内容
					int result = DBUtil.deleteDiary(msg);
					if(result == 1){		//删除成功
						dout.writeUTF("<#DELETE_DIARY_SUCCESS#>");
					}
					else{
						dout.writeUTF("<#DELETE_DIARY_FAIL#>");
					}
				}
				else if(msg.startsWith("<#MODIFY_DIARY#>")){		//消息为修改日志
					msg = msg.substring(16);		//提取内容
					String [] sa = msg.split("\\|");	//分隔字符串
					int result = DBUtil.modifyDiary(sa[0], sa[1], sa[2]);
					if(result == 1){		//修改成功
						dout.writeUTF("<#MODIFY_DIARY_SUCCESS#>");
					}
					else{
						dout.writeUTF("<#MODIFY_DIARY_FAIL#>");
					}
				}
				else if(msg.startsWith("<#DELETE_PHOTO#>")){			//消息为删除照片
					msg = msg.substring(16);
					int result = DBUtil.deletePhoto(msg);
					if(result == 1){
						dout.writeUTF("<#DELETE_PHOTO_SUCCESS#>");
					}
					else{
						dout.writeUTF("<#DELETE_PHOTO_FAIL#>");
					}
				}
				else if(msg.startsWith("<#GET_ALBUM_LIST_BY_ACCESS#>")){	//消息为按照权限获得相册列表
					msg = msg.substring(28);	//提取内容
					String [] sa = msg.split("\\|");
					ArrayList<String []> albumList = DBUtil.getAlbumListByAccess(sa[0], sa[1]);
					if(albumList.size() == 0){
						dout.writeUTF("<#NO_ALBUM#>");
					}
					else{
						StringBuilder sb = new StringBuilder();
						for(String [] albumInfo:albumList){
							sb.append(albumInfo[0]);
							sb.append("|");
							sb.append(albumInfo[1]);
							sb.append("$");
						}
						dout.writeUTF(sb.toString());						
					}
				}
			}
			catch(SocketException se){
				try {
					dout.close();
					din.close();
					socket.close();
					socket = null;
					flag = false;
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
			catch(EOFException eof){
				try {
					dout.close();
					din.close();
					socket.close();
					socket = null;
					flag = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
