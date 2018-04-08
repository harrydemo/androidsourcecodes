package trainApp.common;

public class TrainInfo
{

	public TrainInfo(){
		
	}
	public TrainInfo(String trainCode, String startStation, 
					 String arriveStation, String startTime, 
					 String arriveTime, String usedTime, 
					 String hardSeatCount, String softSeatCount,
					 String hardCouchetteCount, String softCouchetteCount,
					 String firstClassSeatCount, String secondClassSeatCount,
					 String premiumCouchetteCount, boolean haveSeat){
		String[] codes = trainCode.replace("(", "|").replace("（", "|").split("|");
		this.trainCode = codes[0];
		this.startStation = startStation;
		this.arriveStation = arriveStation;
		this.startTime = startTime;
		this.arriveTime = arriveTime;
		this.usedTime = usedTime;
		this.hardSeatCount = hardSeatCount;
		this.softSeatCount = softSeatCount;
		this.hardCouchetteCount = hardCouchetteCount;
		this.softCouchetteCount = softCouchetteCount;
		this.firstClassSeatCount = firstClassSeatCount;
		this.secondClassSeatCount = secondClassSeatCount;
		this.premiumCouchetteCount = premiumCouchetteCount;
		this.haveSeat = haveSeat;
	}
	/**
	 * 火车车次
	 */
	private String trainCode;
	/**
	 * 发站
	 */
	private String startStation;
	/**
	 * 到站
	 */
	private String arriveStation;
	/**
	 * 开车时间
	 */
	private String startTime;
	/**
	 * 到车时间
	 */
	private String arriveTime;
	/**
	 * 历时
	 */
	private String usedTime;
	/**
	 * 硬座数量
	 */
	private String hardSeatCount;
	/**
	 * 软座数量
	 */
	private String softSeatCount;
	/**
	 * 硬卧数量
	 */
	private String hardCouchetteCount;
	/**
	 * 软卧数量
	 */
	private String softCouchetteCount;
	/**
	 * 一等座
	 */
	private String firstClassSeatCount;
	/**
	 * 二等座
	 */
	private String secondClassSeatCount;
	/**
	 * 高级软卧
	 */
	private String premiumCouchetteCount;
	
	private boolean haveSeat;
	/*******Set 方法*******/
	public void setTrainCode(String trainCode){
		this.trainCode = trainCode;
	}
	public void setStartStation(String startStation){
		this.startStation = startStation;
	}
	public void setArriveStation(String arriveStation){
		this.arriveStation = arriveStation;
	}
	public void setStartTime(String startTime){
		this.startTime = startTime;
	}
	public void setArriveTime(String arriveTime){
		this.arriveTime = arriveTime;
	}
	public void setUsedTime(String usedTime){
		this.usedTime = usedTime;
	}
	public void setHardSeatCount(String hardSeatCount){
		this.hardSeatCount = hardSeatCount;
	}
	public void setSoftSeatCount(String softSeatCount){
		this.softSeatCount = softSeatCount;
	}
	public void setHardCouchetteCount(String hardCouchetteCount){
		this.hardCouchetteCount = hardCouchetteCount;
	}
	public void setSoftCouchetteCount(String softCouchetteCount){
		this.softCouchetteCount = softCouchetteCount;
	}
	public void setFirstClassSeatCount(String firstClassSeatCount){
		this.firstClassSeatCount = firstClassSeatCount;
	}
	public void setSecondClassSeatCount(String secondClassSeatCount){
		this.secondClassSeatCount = secondClassSeatCount;
	}
	public void setPremiumCouchetteCount(String premiumCouchetteCount){
		this.premiumCouchetteCount = premiumCouchetteCount;
	}
	
	public void setHaveSeat(boolean haveSeat){
		this.haveSeat = haveSeat;
	}
	
	/*******Get 方法*******/
	public String getTrainCode(){
		return this.trainCode;
	}
	public String getStartStation(){
		return this.startStation;
	}
	public String getArriveStation(){
		return this.arriveStation;
	}
	public String getStartTime(){
		return this.startTime;
	}
	public String getArrtiveTime(){
		return this.arriveTime;
	}
	public String getUsedTime(){
		return this.usedTime;
	}
	public String getHardSeatCount(){
		return this.hardSeatCount;
	}
	public String getSoftSeatCount(){
		return this.softSeatCount;
	}
	
	public String getHartCouchetteCount(){
		return this.hardCouchetteCount;
	}
	public String getSoftCouchetteCount(){
		return this.softCouchetteCount;
	}
	public String getFirstClassSeatCount(){
		return this.firstClassSeatCount;
	}
	public String getSecondClassSeatCount(){
		return this.secondClassSeatCount;
	}
	public String getPremiumCouchetteCount(){
		return this.premiumCouchetteCount;
	}
	
	public boolean isHaveSeat(){
		return this.haveSeat;
	}
}
