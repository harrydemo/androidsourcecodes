package cn.edu.xtu.tilepuzzle;

import android.graphics.Bitmap;

public class ClassPeopleInfo {
	public String name="";	
	public String flag="3x4";// ��ʾ �� 3��4�� 4x5 


	public long time=0;
	public String strTime="";
	
	public Bitmap showImage;

	public Bitmap getShowImage() {
		return showImage;
	}

	public void setShowImage(Bitmap showImage) {
		this.showImage = showImage;
	}

	public ClassPeopleInfo(){
		
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
		//System.out.println("�������ͣ�"+flag);
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {		
		this.name = name;
		//System.out.println("�������֣�"+name);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {		
		//System.out.println("����ʱ�䣺"+time);
		this.time = time;
		String space="";
		if(time>=1000&&time<10000)space="";
		else if(time>=100)space=" ";
                else if(time>=10)space="  ";
		else {
			space="   ";
		}
		this.strTime=String.valueOf(time)+space+"��";//BoardModel.getTimeString(time);		
	}

	public String getStrTime() {
		return strTime;
	}
}
