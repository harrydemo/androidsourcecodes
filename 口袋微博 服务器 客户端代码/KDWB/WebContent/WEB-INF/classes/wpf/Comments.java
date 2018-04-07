package wpf;

public class Comments {
	public String date;		//评论时间
	public String content;		//平路内容
	public String cmtName;		//评论者名称
	public String cmtNo;		//评论者id
	
	public Comments(String date,String content,String cmtName,String cmtNo){
		this.date = date;
		this.content = content;
		this.cmtName = cmtName;
		this.cmtNo = cmtNo;
	}
}
