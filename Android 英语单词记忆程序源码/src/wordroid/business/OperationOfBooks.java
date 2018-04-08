package wordroid.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import wordroid.database.DataAccess;
import wordroid.model.Word;
import wordroid.model.WordList;

public class OperationOfBooks {
	public static boolean ifContinue;
	/*
	 * 初始化词库
	 * 参数
	 * FileName: 文本文件名
	 * NumOfEachList:用户选择的每个LIST的容量
	 */
	public boolean ImportBook(Context context,String FileName,String NewName,int NumOfEachList,String order) throws Exception{
		ifContinue=true;
		ArrayList<Word> list = new ArrayList<Word>();
		
		File f = new File("/sdcard/"+FileName);
		Log.i("1",FileName);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			int ID=1;
			int listnum=1;
			while((line=br.readLine())!=null){
				if (ifContinue){
					if ((double)ID/NumOfEachList>listnum)
						listnum++;
					StringTokenizer st = new StringTokenizer(line, "[,]");
					Word word = new Word();
					word.setID(String.valueOf(ID));
					word.setList(String.valueOf(listnum));
					word.setSpelling(st.nextToken());
					word.setPhonetic_alphabet("["+st.nextToken()+"]");
					word.setMeanning(st.nextToken());
					list.add(word);
					Log.i("Add word into list",word.getSpelling());
				ID++;
				}
				else return false;
				
			} 
			ArrayList<Word> mylist = initWordList(list,order);
			
//			for (int i=0;i<list.size();i++){
//				Log.i("Check list 1",list.get(i).getSpelling()+list.get(i).getMeanning());
//			}
			DataAccess data = new DataAccess(context);
			boolean success;
			try{
					success=data.initBook(FileName, mylist,String.valueOf(listnum),NewName);
			}catch(Exception e){
				throw new Exception();
			}
			
	//		Log.i("bookID",bookID);
	//		Log.i("listnum",""+listnum);
		return success; 
	
		
	}
	
	private ArrayList<Word> initWordList(ArrayList<Word> list, String order) {
		// TODO Auto-generated method stub
		String listnum;
		if (order.equals("luanxu")){
			int num=0;
		    for (int i=0;i<list.size();i++){
		    	if (ifContinue){
		    		num=(int) (Math.random()*list.size());
					  listnum=list.get(num).getList();
					  list.get(num).setList(list.get(i).getList());
					  list.get(i).setList(listnum);
					  Log.i("luan", list.get(i).getList());
		    	}
		    	else return list;
			  
		}
		}
		return list;
	}

	public void UpdateListInfo(Context context){
		int diff=0;
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	    String date=f.format(cal.getTime());
		ArrayList<WordList> list = new ArrayList<WordList>();
		DataAccess data = new DataAccess(context);
		list=data.QueryList(null, null);
		java.util.Date olddate;
		java.util.Date nowdate;
		try {
			nowdate = f.parse(date);
	loop:	for (int i=0;i<list.size();i++){
			if (list.get(i).getShouldReview().equals("1"))
				continue;
			if (list.get(i).getLearned().equals("0"))
				continue;
			if (list.get(i).getReviewTime().equals(""))
				olddate=f.parse(list.get(i).getLearnedTime());
			else olddate=f.parse(list.get(i).getReviewTime());
			switch (Integer.parseInt(list.get(i).getReview_times())){
			case 0:{
				diff=1;
				break;
			}
			case 1:{
				diff=1;
				break;
			}
			case 2:{
				diff=2;
				break;
			}
			case 3:{
				diff=3;
				break;
			}
			case 4:{
				diff=8;
				break;
			}
			default:{
				continue loop;
			}
			}
		    
		    long  day=(nowdate.getTime()-olddate.getTime())/(24*60*60*1000);
		    Log.i("Comparing "+nowdate.getTime()+" "+olddate.getTime(), String.valueOf(day));
		    if (day>=diff){
		    	WordList newlist =list.get(i);
		    	newlist.setShouldReview("1");
		    	data.UpdateList(newlist);
		    }
		}} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<ArrayList<String>> GetPlan(int week,Context context){
		if (week<0)return null;
		DataAccess data = new DataAccess(context);
		ArrayList<WordList> source = data.QueryList("BOOKID = '"+DataAccess.bookID+"'", null);
		ArrayList<ArrayList<String>> origin = new ArrayList<ArrayList<String>>();
	    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		for (int i=0;i<source.size();i++){
			if (source.get(i).getLearned().equals("0")){
				ArrayList<String> inOrigin = new ArrayList<String>();
				origin.add(inOrigin);
				continue;
			}
			if (Integer.parseInt(source.get(i).getReview_times())>=5){
				ArrayList<String> inOrigin = new ArrayList<String>();
				origin.add(inOrigin);
				continue;
			}
			if (source.get(i).getShouldReview().equals("1")){
				Calendar cal = Calendar.getInstance();
				switch(Integer.parseInt(source.get(i).getReview_times())){
				case 0:{
					ArrayList<String> inOrigin = new ArrayList<String>();
			        inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 1);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 2);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 3);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 8);
					inOrigin.add(f.format(cal.getTime()));
					origin.add(inOrigin);
					break;
				}
				case 1:{
					ArrayList<String> inOrigin = new ArrayList<String>();
			        inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 2);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 3);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 8);
					inOrigin.add(f.format(cal.getTime()));
					origin.add(inOrigin);
					break;
				}
				case 2:{
					ArrayList<String> inOrigin = new ArrayList<String>();
			        inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 3);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 8);
					inOrigin.add(f.format(cal.getTime()));
					origin.add(inOrigin);
					break;
				}
				case 3:{
					ArrayList<String> inOrigin = new ArrayList<String>();
			        inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 8);
					inOrigin.add(f.format(cal.getTime()));
					origin.add(inOrigin);
					break;
				}
				case 4:{
					ArrayList<String> inOrigin = new ArrayList<String>();
			        inOrigin.add(f.format(cal.getTime()));
					origin.add(inOrigin);
					break;
				}
				
				}
			}
			else if (source.get(i).getShouldReview().equals("0")){
				Calendar cal = Calendar.getInstance();
				String from;
				if (source.get(i).getReview_times().equals("0"))
				from = source.get(i).getLearnedTime();
				else from = source.get(i).getReviewTime();
			     Date date1 = null;
			     try {
					 date1 = f.parse(from);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cal.setTime(date1);
				
				switch(Integer.parseInt(source.get(i).getReview_times())){
				case 0:{
					ArrayList<String> inOrigin = new ArrayList<String>();
					cal.add(Calendar.DATE, 1);
			        inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 1);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 2);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 3);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 8);
					inOrigin.add(f.format(cal.getTime()));
					origin.add(inOrigin);
					break;
				}
				case 1:{
					ArrayList<String> inOrigin = new ArrayList<String>();
					cal.add(Calendar.DATE, 1);
			        inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 2);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 3);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 8);
					inOrigin.add(f.format(cal.getTime()));
					origin.add(inOrigin);
					break;
				}
				case 2:{
					ArrayList<String> inOrigin = new ArrayList<String>();
					cal.add(Calendar.DATE, 2);
			        inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 3);
					inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 8);
					inOrigin.add(f.format(cal.getTime()));
					origin.add(inOrigin);
					break;
				}
				case 3:{
					ArrayList<String> inOrigin = new ArrayList<String>();
					cal.add(Calendar.DATE, 3);
			        inOrigin.add(f.format(cal.getTime()));
					cal.add(Calendar.DATE, 8);
					inOrigin.add(f.format(cal.getTime()));
					origin.add(inOrigin);
					break;
				}
				case 4:{
					ArrayList<String> inOrigin = new ArrayList<String>();
					cal.add(Calendar.DATE, 8);
			        inOrigin.add(f.format(cal.getTime()));
					origin.add(inOrigin);
					break;
				}
				
				}
				
				
				}
				}
		
		for (int i1=0;i1<origin.size();i1++){
			Log.i("List"+(i1+1), "have");
			for (int j=0;j<origin.get(i1).size();j++){
				Log.i(""+j, "on"+origin.get(i1).get(j));
			}
		}
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		Calendar cale = Calendar.getInstance();
		String[] fromto = new String[7];
		cale.add(Calendar.DATE, week*7);
		for(int i=0;i<7;i++){
			fromto[i]=f.format(cale.getTime());
			cale.add(Calendar.DATE, 1);
		}
		for(int i=0;i<7;i++){
			ArrayList <String> inResult = new ArrayList<String>();
			inResult.add(fromto[i]);
			result.add(inResult);
		}
		for (int listnum=0;listnum<origin.size();listnum++){
			for (int j=0;j<origin.get(listnum).size();j++){
				for (int date=0;date<7;date++){
					if (origin.get(listnum).get(j).equals(fromto[date]))
						result.get(date).add("List-"+(listnum+1));
				}
			}
		}
		
		
		return result;
	}
	public void setNotify(String time,Context context){
		StringTokenizer st = new StringTokenizer(time, ":, ",false);
		int hour = Integer.parseInt(st.nextToken());
		int minute = Integer.parseInt(st.nextToken());
		Log.i("hour", ""+hour);
		Log.i("minute", ""+minute);
		Calendar calendar=Calendar.getInstance();
		if(calendar.get(Calendar.HOUR_OF_DAY)>hour||(calendar.get(Calendar.HOUR_OF_DAY)==hour&&calendar.get(Calendar.MINUTE)>=minute))
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Log.i("", f.format(calendar.getTime()));
		long starttime=calendar.getTimeInMillis();
		long repeattime = 24*60*60*1000;
		AlarmManager am = (AlarmManager) context.getSystemService("alarm");
		Intent intent = new Intent(context,makeNotify.class);
		intent.setAction("shownotify");
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.cancel(sender);
		Intent intent2 = new Intent(context,makeNotify.class);
		intent2.setAction("shownotify");
		PendingIntent sender2 = PendingIntent.getBroadcast(context, 0, intent2, 0);
		am.setRepeating(AlarmManager.RTC, starttime, repeattime, sender2);
		Log.i("it's on", "on");
	}
}
