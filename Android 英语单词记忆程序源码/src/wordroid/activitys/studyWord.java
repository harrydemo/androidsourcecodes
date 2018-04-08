package wordroid.activitys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import wordroid.model.R;
import wordroid.model.Word;
import wordroid.business.OperationOfBooks;
import wordroid.business.TTS;
import wordroid.database.DataAccess;
import wordroid.model.WordList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class studyWord extends Activity implements OnClickListener{
	private TextView tv;
	private TextView tv2;
	private String listnum;
	private int currentnum;
	private int numoflist;
	private ArrayList<Word> list = new ArrayList<Word>();
    private Button add;
    private Button nextone;
    private Button beforeone;
    private TextView spelling;
    private TextView info;
    private ImageButton speak;
    private TextToSpeech tts;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studyword);
		currentnum=0;
		Intent intent = getIntent();
		
		Bundle b = intent.getExtras();
		String name = b.getString("list");
		listnum=name;
		this.setTitle("学习LIST-"+name);
		DataAccess data = new DataAccess(this);
		list=data.QueryWord("LIST = '"+name+"'", null);
		numoflist = list.size();
		initWidgets();
		UpdateView();
	}

	private void initWidgets() {
		// TODO Auto-generated method stub
		this.add=(Button) this.findViewById(R.id.add);
		add.setOnClickListener(this);
		this.info=(TextView) this.findViewById(R.id.info);
		this.beforeone=(Button) this.findViewById(R.id.beforeone);
		beforeone.setOnClickListener(this);
		this.nextone=(Button) this.findViewById(R.id.nextone);
		nextone.setOnClickListener(this);
		this.spelling=(TextView) this.findViewById(R.id.spelling);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUI.TTF");
		info.setTypeface(tf);
		this.speak=(ImageButton)this.findViewById(R.id.ImageButton01);
		tts = new TextToSpeech(this, ttsInitListener);
		speak.setOnClickListener(this);
		DisplayMetrics dm = new DisplayMetrics(); 
		   dm = getApplicationContext().getResources().getDisplayMetrics(); 
		   int screenWidth = dm.widthPixels; 
		   add.setWidth(screenWidth/3);
		   beforeone.setWidth(screenWidth/3);
		   nextone.setWidth(screenWidth/3);
	
	}

	private TextToSpeech.OnInitListener ttsInitListener = new TextToSpeech.OnInitListener()
	  {

	    @Override
	    public void onInit(int status)
	    {
	      // TODO Auto-generated method stub
	    	Locale loc= new Locale("uk");
			SharedPreferences setting = getSharedPreferences("wordroid.model_preferences", MODE_PRIVATE);
			if(setting.getString("category", "1").equals("2"))
				loc = new Locale("us");
			
	      /* 检查是否支持输入的时区 */
	      if (tts.isLanguageAvailable(loc) == TextToSpeech.LANG_AVAILABLE)
	      {
	        /* 设定语言 */
	        tts.setLanguage(loc);
	      }
	      tts.setOnUtteranceCompletedListener(ttsUtteranceCompletedListener);
	    }

	  };
	  private TextToSpeech.OnUtteranceCompletedListener ttsUtteranceCompletedListener = new TextToSpeech.OnUtteranceCompletedListener()
	  {
	    @Override
	    public void onUtteranceCompleted(String utteranceId)
	    {
	    }
	  };
	public void onClick(View v) {
		 Log.i("3", "3");
	    this.UpdateView();
	    Log.i("3", "3");
		if (v==nextone){
			if(currentnum<numoflist){
			    currentnum++;
			    this.UpdateView();
			}
			
		}
		else if (v==add){
			DataAccess data = new DataAccess(studyWord.this);
			ArrayList<Word> attention = new ArrayList<Word>();
			attention=data.QueryAttention("SPELLING ='"+list.get(currentnum).getSpelling()+"'", null);
			if (attention.size()==0){
				data.InsertIntoAttention(list.get(currentnum));
				Toast.makeText(studyWord.this, "已加入生词本", Toast.LENGTH_SHORT).show();
			}
			else Toast.makeText(studyWord.this, "生词本中已包含这个单词！", Toast.LENGTH_SHORT).show();
		
		}
		else if(v==beforeone){
			currentnum--;
			this.UpdateView();
		}
		Log.i("3", "3");
		if (v==speak){
			tts.speak(list.get(currentnum).getSpelling(),TextToSpeech.QUEUE_ADD,
		              null);
		}
		Log.i("3", "3");
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
			 Dialog dialog = new AlertDialog.Builder(this)
	            .setIcon(R.drawable.dialog_icon)
	            .setTitle("学习未完成")
	            .setMessage("你确定现在结束学习吗？这将导致本次学习无效！")
	            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked OK so do some stuff */
	                	tts.shutdown();
	                	finish();
	                	Intent intent = new Intent();
	            		intent.setClass(studyWord.this, study.class);
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
			 
		return true;
	}

	private void UpdateView() {
		if(currentnum==0){
			beforeone.setEnabled(false);
		}
		else if(currentnum>0){
			beforeone.setEnabled(true);
		}
		SharedPreferences setting = getSharedPreferences("wordroid.model_preferences", MODE_PRIVATE);
		if(setting.getBoolean("iftts", false)){
			Thread thread =new Thread(new Runnable(){
	              public void run(){
					try {
						Thread.sleep(500);
						tts.speak(list.get(currentnum).getSpelling(),TextToSpeech.QUEUE_FLUSH,
					              null);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
				thread.start();
		}
		
		
		// TODO Auto-generated method stub
		if (currentnum<numoflist){
			spelling.setText(list.get(currentnum).getID()+"."+list.get(currentnum).getSpelling());
			info.setText(list.get(currentnum).getPhonetic_alphabet()+"\n"+list.get(currentnum).getMeanning());
	        }
		else if(currentnum>=numoflist){
				DataAccess data = new DataAccess(this);
				WordList wordlist=data.QueryList("BOOKID ='"+DataAccess.bookID+"'AND LIST = '"+listnum+"'", null).get(0);
				wordlist.setLearned("1");
				wordlist.setReview_times("0");
				wordlist.setReviewTime("");
				Calendar cal = Calendar.getInstance();
			    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			    String date=f.format(cal.getTime());
				wordlist.setLearnedTime(date);
				data.UpdateList(wordlist);
				
				currentnum--;
				
				Dialog dialog = new AlertDialog.Builder(this)
	            .setIcon(R.drawable.dialog_icon)
	            .setTitle("学习已完成")
	            .setMessage("您可以依照复习计划进行本单元的复习")
	            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked OK so do some stuff */
	                	tts.shutdown();
	                	finish();
	                	Intent intent = new Intent();
	            		intent.setClass(studyWord.this, study.class);
	            		startActivity(intent);
	            		
	                }
	            }).create();
				dialog.show();
			}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
