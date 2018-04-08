package trainApp.http;

public class PostDataProvider {
	private static String LX = "lx";// 普通查询00;学生团体查询0X；农民工团体查询0M
	public static String LX_VALUE_00 = "00";
	public static String LX_VALUE_0X = "0X";
	public static String LX_VALUE_0M = "0M";

	// 查询日期
	private static String NMONTH3 = "nmonth3";
	private static String NMONTH3_NEW_VALUE = "nmonth3_new_value";
	private static String NDAY3 = "nday3";
	private static String NDAY3_NEW_VALUE = "nday3_new_value";

	// 发站与到站
	private static String STARTSTATION_TICKETLEFT = "startStation_ticketLeft";
	private static String STARTSTATION_TICKETLEFT_NEW_VALUE = "startStation_ticketLeft_new_value";
	private static String ARRIVESTATION_TICKETLEFT = "arriveStation_ticketLeft";
	private static String ARRIVESTATION_TICKETLEFT_NEW_VALUE = "arriveStation_ticketLeft_new_value";
	// 车次
	private static String TRAINCODE = "trainCode";
	private static String TRAINCODE_NEW_VALUE = "trainCode_new_value";

	// 全部1；始发3；终到4；始发终到5；过路6；
	private static String RFLAG = "rFlag";
	/**
	 * 筛选查询条件，全部的车辆
	 */
	public static String RFLAG_ALL_VALUE = "1";
	/**
	 * 筛选查询条件，始发的车辆
	 */
	public static String RFLAG_START_VALUE = "3";
	/**
	 * 筛选查询条件，终到的车辆
	 */
	public static String RFLAG_ARRIVE_VALUE = "4";
	/**
	 * 筛选查询条件，始发终到的车辆
	 */
	public static String RFLAG_START_ARRIVE_VALUE = "5";
	/**
	 * 筛选查询条件，路过的车辆
	 */
	public static String RFLAG_PASS_VALUE = "6";
	

	public static String NAME_CKBALL = "name_ckball";// 全部车辆值value_ckball，如果没有全部post信息不填加此选项
	public static String NAME_CKBALL_VALUE = "value_ckball";

	// 下面分别为动车/直特/特快/快速/普快/普客/临客，如没有选不添加post数据中
	public static String TFLAGDC = "tFlagDC";
	public static String TFLAGZ = "tFlagZ";
	public static String TFLAGT = "tFlagT";
	public static String TFLAGK = "tFlagK";
	public static String TFLAGPK = "tFlagPK";
	public static String TFLAGPKE = "tFlagPKE";
	public static String TFLAGLK = "tFlagLK";

	/**
	 * 私有的post数据字段
	 */
	private String postData;


	/**
	 * 获取PostDataProvider的一个实例
	 * @return
	 */
	public static PostDataProvider getInstance() {
		return new PostDataProvider();
	}

	private PostDataProvider() {
	}
	
	/**
	 * 获取Post的数据
	 * 
	 * @return post数据
	 */
	public String getPostData() {
		return this.postData;
	}

	/**
	 * 设置Post的数据
	 * 
	 * @param nmonth3 月份
	 * @param nday3 日
	 * @param startStation_ticketLeft 发站
	 * @param arriveStation_ticketLeft 到站
	 * @param trainCode 车次
	 * @param rFlag 筛选，全部1；始发3；终到4；始发终到5；过路6；
	 * @return 返回postData串
	 */
	public String setPostData(String nmonth3, String nday3,
			String startStation_ticketLeft, String arriveStation_ticketLeft,
			String trainCode, String rFlag) {
		postData = this.NMONTH3 + "=" + nmonth3 + "&" + this.NMONTH3_NEW_VALUE
				+ "=" + "true" + "&" + this.NDAY3 + "=" + nday3 + "&"
				+ this.NDAY3_NEW_VALUE + "=" + "false" + "&"
				+ this.STARTSTATION_TICKETLEFT + "=" + startStation_ticketLeft
				+ "&" + this.STARTSTATION_TICKETLEFT_NEW_VALUE + "=" + "true"
				+ "&" + this.ARRIVESTATION_TICKETLEFT + "="
				+ arriveStation_ticketLeft + "&"
				+ this.ARRIVESTATION_TICKETLEFT_NEW_VALUE + "=" + "true" + "&"
				+ this.TRAINCODE + "=" + trainCode + "&"
				+ this.TRAINCODE_NEW_VALUE + "=" + "true" + "&" + this.RFLAG
				+ "=" + rFlag;
		return postData;
	}

	/**
	 * 后续添加post数据的键值信息
	 * 
	 * @param key
	 *            post数据键
	 * @param value
	 *            post的数据值
	 */
	public void addPostData(String key, String value) {
		if (postData != "") {
			postData = postData + "&" + key + "=" + value;
		} else {
			postData = key + "=" + value;
		}
	}

	/**
	 * 默认参数设置，所有火车的等级全选时
	 */
	public void addPostData() {
		addPostData(PostDataProvider.NAME_CKBALL,
				PostDataProvider.NAME_CKBALL_VALUE);
		addPostData(PostDataProvider.TFLAGDC, "DC");
		addPostData(PostDataProvider.TFLAGZ, "Z");
		addPostData(PostDataProvider.TFLAGT, "T");
		addPostData(PostDataProvider.TFLAGK, "K");
		addPostData(PostDataProvider.TFLAGPK, "PK");
		addPostData(PostDataProvider.TFLAGPKE, "PKE");
		addPostData(PostDataProvider.TFLAGLK, "LK");
	}

	/**
	 * 面向的对象的post数据
	 * @param value
	 *            lx的值：普通查询00;学生团体查询0X；农民工团体查询0M
	 * @return 添加成功后返回true
	 */
	public boolean addLXPostData(String value) {
		boolean flag = false;
		if (this.postData != "") {
			postData = "lx" + "=" + value + "&" + postData;
			flag = true;
		}
		return flag;
	}
}
