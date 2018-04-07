package cn.com.hh.domian;

public class WeiBoInfo {
    //文章id
    private String id;
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }
    //发布人id
    private String userId;
    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId=userId;
    }
    
    //发布人名字
    private String userName;
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    
    //发布人头像
    private String userIcon;
    public String getUserIcon(){
        return userIcon;
    }
    public void setUserIcon(String userIcon){
        this.userIcon=userIcon;
    }
    
    //发布时间
    private String time;
    public String getTime(){
        return time;
    }
    public void setTime(String time)
    {
        this.time=time;
    }
    
    //是否有图片
    private Boolean haveImage=false;
    public Boolean getHaveImage(){
        return haveImage;
    }
    public void setHaveImage(Boolean haveImage){
        this.haveImage=haveImage;
    }
    
    //文章内容
    private String text;
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text=text;
    }
    
}