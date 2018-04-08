package wpf;


public class PhotoInfo {
	public String p_id;		//相片编号
	public String p_name;	//相片名称
	public String p_des;	//相片描述
	public String x_id;		//相册名称
	public PhotoInfo(String pId, String pName, String pDes, String xId) {
		super();
		p_id = pId;
		p_name = pName;
		p_des = pDes;
		x_id = xId;
	}
}
