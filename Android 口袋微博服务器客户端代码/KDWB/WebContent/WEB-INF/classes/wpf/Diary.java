package wpf;

import java.util.ArrayList;

public class Diary {
	public String rid;	//»’º«±‡∫≈
	public String title;
	public String content;
	public String u_name;
	public String time;
	public String u_no;
	public ArrayList<Comments> commentList;
	
	public void setCommentList(ArrayList<Comments> commentList) {
		this.commentList = commentList;
	}

	public Diary(String no,String title,String content,String u_name,String u_no,
			String time,ArrayList<Comments> cmtList){
		this.rid = no;
		this.title = title;
		this.content = content;
		this.u_name = u_name;
		this.u_no = u_no;
		this.time = time;
		this.commentList = cmtList;
	}
	public Diary(String no,String title,String content,String u_name,String u_no,String time){
		this.rid = no;
		this.title = title;
		this.content = content;
		this.u_name = u_name;
		this.u_no = u_no;
		this.time = time;
	}
}
