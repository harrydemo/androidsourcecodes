package wordroid.activitys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import wordroid.model.BookList;
import wordroid.model.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wordroid.business.OperationOfBooks;
import wordroid.database.DataAccess;
import wordroid.model.WordList;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;


public class ReviewMain extends TabActivity implements TabHost.TabContentFactory{
	private int week=0;
	public ArrayList<WordList> wordlist;
	private ArrayList<String> listShould ;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		TabHost th = getTabHost();
		DataAccess data = new DataAccess(this);
		wordlist=data.QueryList("BOOKID ='"+DataAccess.bookID+"'", null);
		BookList book =data.QueryBook("ID ='"+DataAccess.bookID+"'", null).get(0);
		listShould = new ArrayList<String>(wordlist.size());
		for(int i=0;i<wordlist.size();i++){
			if (wordlist.get(i).getShouldReview().equals("1")){
				listShould.add(wordlist.get(i).getList());
			}
		}
		this.setTitle("复习-"+book.getName());
		th.addTab(th.newTabSpec("shouldreview").setIndicator("该复习的LIST",ReviewMain.this.getResources().getDrawable(R.drawable.should)).setContent(this));
		th.addTab(th.newTabSpec("alllists").setIndicator("所有LIST",ReviewMain.this.getResources().getDrawable(R.drawable.all)).setContent(this));
		th.addTab(th.newTabSpec("plan").setIndicator("我的复习计划",ReviewMain.this.getResources().getDrawable(R.drawable.plan)).setContent(this));
	}
	@Override
	public View createTabContent(final String tag) {
		// TODO Auto-generated method stub
		final TextView textview = new TextView(this);
		textview.setTextColor(Color.BLACK);
		textview.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.main_budget_lv_header));
	    final ListView lv = new ListView(this);
	    lv.setCacheColorHint(0); 
		LinearLayout ll= new LinearLayout(this);
		ll.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.main_back));
		ll.setOrientation(LinearLayout.VERTICAL);
		if (tag.equals("shouldreview")){
			textview.setText("   需要进行复习的LIST");
			ll.addView(textview);
			
			lv.setAdapter(new SimpleAdapter(this, getData(tag), R.layout.list1, new String[]{"label","times","lasttime","image"}, new int[]{R.id.label,R.id.times,R.id.lasttime,R.id.list1_image}));
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int arg2, long arg3) {
					// TODO Auto-generated method stub
					startReview(arg2,1);
					
				}				
			});
			lv.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int arg2, long arg3) {
					// TODO Auto-generated method stub
					Dialog dialog = new AlertDialog.Builder(ReviewMain.this)
		            .setIcon(R.drawable.dialog_icon)
		            .setTitle("操作")
		            .setItems(new String[]{"标记为已复习"}, new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (which==0){
								DataAccess data = new DataAccess(ReviewMain.this);
								WordList labelList = wordlist.get(Integer.parseInt(listShould.get(arg2))-1);
								labelList.setShouldReview("0");
								labelList.setReview_times(String.valueOf((Integer.parseInt(labelList.getReview_times())+1)));
								Calendar cal = Calendar.getInstance();
							    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
							    String date=f.format(cal.getTime());
							    labelList.setReviewTime(date);
								data.UpdateList(labelList);
								Intent intent = new Intent();
								intent.setClass(ReviewMain.this, ReviewMain.class);
								finish();
								startActivity(intent);
							}
						}
		            	
		            })
		            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		            .create();
					dialog.show();
					return false;
				}
				
			});
			ll.addView(lv);
			
		}
		if (tag.equals("alllists")){
			textview.setText("   所有的LIST");
			ll.addView(textview);
			lv.setAdapter(new SimpleAdapter(this, getData(tag), R.layout.list2, new String[]{"label","state","image"}, new int[]{R.id.label,R.id.state,R.id.list2_image}));
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int arg2, long arg3) {
					// TODO Auto-generated method stub
					if(wordlist.get(arg2).getLearned().equals("0")){
						Dialog dialog = new AlertDialog.Builder(ReviewMain.this)
			            .setIcon(R.drawable.dialog_icon)
			            .setTitle("提醒")
			            .setMessage("这个单元(LIST-"+(arg2+1)+")还没有学习过，要现在学习吗？")
			            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
			                    /* User clicked OK so do some stuff */
			                	Intent intent = new Intent();
			    				Bundle bundle = new Bundle();
			                	bundle.putString("list", String.valueOf(arg2+1));
			    				intent.setClass(ReviewMain.this, studyWord.class);
			    				intent.putExtras(bundle);	
			    				finish();
			    				startActivity(intent);
			                	}
			            })
			            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
			                    /* User clicked OK so do some stuff */
			                	}
			            }).create();
						dialog.show();
					}
					else if(wordlist.get(arg2).getShouldReview().equals("1")){
						startReview(arg2+1,2);
					}
					else if(wordlist.get(arg2).getShouldReview().equals("0")){
						Dialog dialog = new AlertDialog.Builder(ReviewMain.this)
			            .setIcon(R.drawable.dialog_icon)
			            .setTitle("提醒")
			            .setMessage("这个单元(LIST-"+(arg2+1)+")暂时不需要进行复习，确定要现在复习吗？")
			            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
			                    /* User clicked OK so do some stuff */
								startReview(arg2+1,2);
			                	}
			            })
			            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
			                    /* User clicked OK so do some stuff */
			                	}
			            }).create();
						dialog.show();
					}
				
				}
				
			});
			ll.addView(lv);
		}
		if (tag.equals("plan")){
			textview.setText("   我的复习计划");
			lv.setAdapter(new SimpleAdapter(ReviewMain.this, getData(tag), R.layout.list3, new String[]{"date","lists","day","image"}, new int[]{R.id.date,R.id.lists,R.id.day,R.id.list3_image}));
		    Button addbutton= new Button(this);
		    DisplayMetrics dm = new DisplayMetrics(); 
			   dm = getApplicationContext().getResources().getDisplayMetrics(); 
			   int screenWidth = dm.widthPixels; 
			   addbutton.setWidth(screenWidth/2);
		    final Button minusbutton= new Button(this);
		    addbutton.setText("下一周");
		    addbutton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.white_btns));
		    addbutton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					minusbutton.setEnabled(true);
					week++;
					lv.setAdapter(new SimpleAdapter(ReviewMain.this, getData(tag), R.layout.list3, new String[]{"date","lists","day","image"}, new int[]{R.id.date,R.id.lists,R.id.day,R.id.list5_image}));
				}
		    	
		    });
		    minusbutton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.white_btns));
		    minusbutton.setText("上一周");
		    
		    minusbutton.setWidth(screenWidth/2);
		    minusbutton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					week--;
					if (week==0)
						minusbutton.setEnabled(false);
					lv.setAdapter(new SimpleAdapter(ReviewMain.this, getData(tag), R.layout.list3, new String[]{"date","lists","day","image"}, new int[]{R.id.date,R.id.lists,R.id.day,R.id.list5_image}));
				}
		    	
		    });
		    LinearLayout newll = new LinearLayout(ReviewMain.this);
		    newll.setOrientation(LinearLayout.HORIZONTAL);
		    newll.addView(minusbutton);
		    newll.addView(addbutton);
		    minusbutton.setEnabled(false);
			ll.addView(textview);
			ll.addView(newll);
			ll.addView(lv);
		}
		
		return ll;
	}
	
	
	private List<Map<String, Object>> getData(String tag) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		if (tag.equals("shouldreview")){
			
			for (int i=0;i<wordlist.size();i++){
				if (wordlist.get(i).getShouldReview().equals("1")){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("label"," LIST-"+wordlist.get(i).getList());
					map.put("times", "已复习次数："+wordlist.get(i).getReview_times());
					map.put("lasttime", "上次复习时间："+wordlist.get(i).getReviewTime());
					map.put("image", android.R.drawable.btn_star_big_on);
					list.add(map);
					
				}
			}
		}
		else if(tag.equals("alllists")){
			for (int i=0;i<wordlist.size();i++){
			    Map<String,Object> map = new HashMap<String,Object>();
			    map.put("label", "LIST-"+wordlist.get(i).getList());
			    if (wordlist.get(i).getLearned().equals("0")){
			    	map.put("state", "状态：未学习  ");
			    	map.put("image", android.R.drawable.btn_star_big_off);
			    }
			    else if (wordlist.get(i).getShouldReview().equals("1")){
			    	map.put("state", "状态：该复习了！ ");
			    	map.put("image", android.R.drawable.btn_star_big_on);
			    }
			    else if (Integer.parseInt(wordlist.get(i).getReview_times())>=5){
			    	map.put("state", "状态：复习已完成！ ");
			    	map.put("image", android.R.drawable.btn_star_big_off);
			    }
			    	
			    else{
			    	map.put("state", "状态：暂不需复习  ");
			    	map.put("image", android.R.drawable.btn_star_big_off);
			    }
			    list.add(map);
			}
		}
		else if (tag.equals("plan")){
			ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
			OperationOfBooks OOB = new OperationOfBooks();
			result=OOB.GetPlan(week, this);
			for (int i=0;i<7;i++){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("date", result.get(i).get(0));
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd"); 
			    Date date = null;
				try {
					date = f.parse(result.get(i).get(0));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    Calendar calendar=Calendar.getInstance();
			    calendar.setTime(date);
			    String lists="复习内容：";
			    String day="星期";
			    switch(calendar.get(Calendar.DAY_OF_WEEK)){
			    case 1:
			    	day+="一  ";
			    	break;
			    case 2:
			    	day+="二  ";
			    	break;
			    case 3:
			    	day+="三  ";
			    	break;
			    case 4:
			    	day+="四  ";
			    	break;
			    case 5:
			    	day+="五  ";
			    	break;
			    case 6:
			    	day+="六  ";
			    	break;
			    case 7:
			    	day+="日  ";
			    	break;
			    }
				map.put("day",day );
			    for (int j=1;j<result.get(i).size();j++){
			    	lists+=result.get(i).get(j)+" ";
			    }
			    if(result.get(i).size()==1)map.put("image", R.drawable.plan_off);
			    else map.put("image", R.drawable.plan_on);
			    map.put("lists", lists);
			    	list.add(map);
			}
		}
		return list;
	}

	private void startReview(final int arg2,final int tag) {
		String tag1="";
		if (tag==1) tag1=listShould.get(arg2);
		else tag1=String.valueOf(arg2);
		Dialog dialog = new AlertDialog.Builder(ReviewMain.this)
        .setIcon(R.drawable.dialog_icon)
        .setTitle("开始复习：")
        .setMessage("LIST-"+tag1)
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                /* User clicked OK so do some stuff */
            	Intent intent = new Intent();
				Bundle bundle = new Bundle();
            	if (tag==1)
            	bundle.putString("list", listShould.get(arg2));
            	else bundle.putString("list", String.valueOf(arg2));
				intent.setClass(ReviewMain.this, Review.class);
				intent.putExtras(bundle);	
				finish();
				startActivity(intent);
            	}
        })
        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                /* User clicked OK so do some stuff */
            	}
        }).create();
		dialog.show();
		
	}
}


