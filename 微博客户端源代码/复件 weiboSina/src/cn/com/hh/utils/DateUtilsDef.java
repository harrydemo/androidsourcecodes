package cn.com.hh.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtilsDef {
  /**
   * �����ָ�������ڵĽ������߹�ȥ��ָ��������
   * @param  Date date ����Ļ�׼����
   * @param  int  dayNum ָ��������
   * @param  Boolean  flag  ��flag��true��Ҫ���㽫�����ڣ������ǹ�ȥ����
   * 
   * @return  String  compDate ����������
   */
	public static String compDate(Date date,int dayNum,Boolean flag){
		if(!date.equals(null)){
			//��ʽ������Date
			 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		     String strDate = df.format(date);
			//��ȡ�������ڵ��꣬�£���
		     String strYear =  strDate.substring(0, 4);
		     int intYear = Integer.valueOf(strYear);
		     String strMonth =  strDate.substring(5, 7);
		     int intMonth = Integer.valueOf(strMonth);
		     String strDay =  strDate.substring(8, 10);
		     int intDay = Integer.valueOf(strDay);
		     int compDay ;
		     //�����flag������Ӧ�Ľ������ڼ���
		     if(flag){
		    	 //�ֳ��·�������
		    	   compDay = intDay + dayNum;
					switch (intMonth){
					case 1:
					case 3:
					case 5:
					case 7:
					case 8:
					case 10:
					     //���������������31ʱ��������1���·ݼ�1
					     if(compDay > 31 ){
					    	 compDay = compDay -31;
					    	 intMonth+=1;
					     }
					     else if(compDay <= 31){
					    	 intDay = compDay;
					     }
					     break;
					case 12:
				     	 if(compDay > 31 ){
				     		compDay = compDay -31;
				   			intMonth = 1;
				   			intYear += 1;
				   		 }
				     	else if(compDay <= 31){
					    	 intDay = compDay;
					     }
				     	 break;
					case 2:
						//����Ķ���
						if((intYear%4==0 && intYear%100!=0)||(intYear%400==0)){
							if(compDay > 29 ){
					     		compDay = compDay -29;
					     		intMonth += 1;
					   		 }
							else if(compDay <= 29){
						    	 intDay = compDay;
						     }
						}else{
							if(compDay > 28 ){
					     		compDay = compDay -28;
					     		intMonth += 1;
					   		 }
							else if(compDay <= 28){
						    	 intDay = compDay;
						     }
						}
						break;
					case 4:
					case 6:
					case 9:
					case 11:
						//���������������31ʱ��������1���·ݼ�1
					     if(compDay > 30 ){
					    	 compDay = compDay - 30;
					    	 intMonth += 1;
					     }
					     else if(compDay <= 30){
					    	 intDay = compDay;
					     }
					     break;
					}
					
				}
		   //�����flag������Ӧ��ȥ�����ڼ���
		     if(!flag){
		    	 //�ֳ��·�������
		    	   compDay = intDay - dayNum;
					switch (intMonth){
					case 1:
						if(compDay <0){
							compDay = (intDay + 31) - dayNum;
							intMonth = 12;
							intYear -=1;
						}
						else if(compDay > 0){
							intDay = compDay;
						}else if(compDay == 0){
							intDay = 31;
							intMonth = 12;
							intYear -=1;
						}
						break;
					case 3:
						//����Ķ���
						if(compDay <0){
							if((intYear%4==0 && intYear%100!=0)||(intYear%400==0)){
								compDay = (intDay + 29) - dayNum;
								
							}else{
								compDay = (intDay + 28) - dayNum;
							}
							intMonth = 2;
						}
						else if(compDay > 0){
							intDay = compDay;
						}
						else if(compDay == 0){
							if((intYear%4==0 && intYear%100!=0)||(intYear%400==0)){
								intDay = 29;
								
							}else{
								intDay = 28;
							}
							intMonth = 2;
						}
						break;
					case 5:
					case 7:
					case 8:
					case 10:
					case 12:
						//�����������С��0ʱ�����������ϸ��µ��������ڼ��������·ݼ�1
						if(compDay < 0){
							compDay = (intDay + 30) - dayNum;
							intMonth -= 1;
						}
						else if(compDay == 0){
							intDay = 30;
							intMonth -= 1;
						}
						else if(compDay > 0){
							intDay = compDay;
						}
					     break;
					case 2:
					case 4:
					case 6:
					case 9:
					case 11:
						 //�����������С��0ʱ�����������ϸ��µ��������ڼ��������·ݼ�1
						if(compDay < 0){
							compDay = (intDay + 31) - dayNum;
							intMonth -= 1;
						}
						else if(compDay == 0){
							intDay = 31;
							intMonth -= 1;
						}
						else if(compDay > 0){
							intDay = compDay;
						}
					     break;
					}
					
				}
		     //����������������ƴ�ӳ��ַ���
		     strYear = String.valueOf(intYear);
		     strMonth = String.valueOf(intMonth);
		     if(intMonth / 10 == 0){
		    	 strMonth = "0" + strMonth;
		     }
		     strDay = String.valueOf(intDay);
		     if(intDay / 10 == 0){
		    	 strDay = "0" + strDay;
		     }
		     strDate = strYear+"-" + strMonth + "-" + strDay;
			 return strDate;
		}
		return null;
	}
	
	/**    
     * ��δָ����ʽ�������ַ���ת����java.util.Date�������ڶ��� <br>    
     *    
     * @param date,��ת���������ַ���    
     * @return    
     * @throws ParseException    
     */     
    public Date parseStringToDate(String date) throws ParseException{      
        Date result=null;      
        String parse=date;      
        parse=parse.replaceFirst("^[0-9]{4}([^0-9]?)", "yyyy$1");      
        parse=parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");      
        parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");      
        parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");      
        parse=parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");      
        parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");      
        parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");      
              
        SimpleDateFormat format=new SimpleDateFormat(parse);      
     
        result=format.parse(date);      
              
        return result;      
    }      
   /**
    * �������������͵�ʱ��������ʱ��
    * @param startDate  ��ʼ����
    * @param endDate    ��������
      * @return
    */
    public  String twoDateDistance(Date startDate,Date endDate){
    	
    	if(startDate == null ||endDate == null){
    		return null;
    	}
      	long timeLong = endDate.getTime() - startDate.getTime(); 
    	long year = timeLong /(24*60*60*1000*365);
    	String yearStr = String.valueOf(year);
    	long month = timeLong %(24*60*60*1000*365) /(24*60*60*1000*30);
    	String monthStr = String.valueOf(month);
    	long day = timeLong %(24*60*60*1000*365)%(24*60*60*1000*30)/(24*60*60*1000);
    	String dayStr = String.valueOf(day);
    	long hour = timeLong %(24*60*60*1000*365) % (24*60*60*1000*30) %(24*60*60*1000)/(60*60*1000);
    	String hourStr = String.valueOf(hour);
    	long minute = timeLong %(24*60*60*1000*365)% (24*60*60*1000*30) % (24*60*60*1000)%(60*60*1000)/(60*1000);
    	String minuteStr = String.valueOf(minute);
    	String returnStr = null;
    	if(year != 0){
    		returnStr = yearStr +"��";
    	}else if(month != 0){
    		returnStr = monthStr +"��";
    	}else if(day != 0){
    		returnStr = dayStr +"��";
    	}else if(hour != 0){
    		returnStr = hourStr +"Сʱ";
    	}else if(minute != 0){
    		returnStr = minuteStr +"����";
    	}
    	return returnStr;
    }
}
