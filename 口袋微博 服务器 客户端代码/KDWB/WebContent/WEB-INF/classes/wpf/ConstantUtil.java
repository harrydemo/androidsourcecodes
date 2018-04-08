package wpf;

public class ConstantUtil {
	public static final String FEIEND = "friend_max";	//用于获取friend表当前的最大编号
	public static final String DIARY = "diary_max";		//用于获取diary表当前的最大编号
	public static final String ALBUM = "album_max";		//用于获取album表当前的最大编号
	public static final String PHOTO = "photo_max";		//用于获取photo表的最大编号
	public static final String COMMENT = "comment_max";	//用于获取comment表当前的最大编号
	public static final String VISIT = "comment_max";	//用于获取visit表当前的最大编号
	public static final String USER = "user_max";		//用于获取user表当前的最大编号
	public static final String HEAD = "head_max";		//用于获取user表当前的最大编号
	public static final String P_COMMENT = "p_comment_max";		//用于获取user表当前的最大编号
	
	public static final String CHAR_ENCODING = "GBK";	//字符串编码格式
	public static final String CONNECTION_OUT = "访问数据库出错，请稍后重试。";
	public static final String LOGIN_FAIL = "用户名或密码无效，请重新输入。";
	public static final String REGISTER_FAIL = "注册失败，请重试。"; 
	public static final String DIARY_FAIL = "日志更新失败，请重试。";
	public static final String DIARY_SUCCESS = "日志更新成功！";
	public static final String DELETE_FAIL = "删除失败，请重试。";
	public static final String DELETE_SUCCESS = "删除成功！";
	public static final String UPDATE_STATE_SUCCESS = "心情更新成功！";
	public static final String UPDATE_STATE_FAIL = "心情更新失败！";
	public static final String CREATE_ALBUM_SUCESS = "相册创建成功！";
	public static final String CREATE_ALBUM_FAIL = "相册创建失败！";
	public static final String UPLOAD_SIZE_EXCEED = "文件大小超过限制！请重新选择。（3M以内）";
	public static final String UPLOAD_SUCCESS = "文件上传成功。";
	public static final String UPLOAD_FAIL = "文件上传失败。";
	public static final int DIARY_ABBR = 8;//显示的日志字数，超过则省略
	
	public static void pp(byte [] buf){
		System.out.println("================================");
		int start = buf.length-100;
		for(int i=start;i<buf.length;i++){
			System.out.print(buf[i]);
		}
		System.out.println("\n================================");
	}
}
