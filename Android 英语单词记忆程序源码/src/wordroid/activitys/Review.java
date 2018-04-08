package wordroid.activitys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import wordroid.database.DataAccess;
import wordroid.model.Word;
import wordroid.model.WordList;

import wordroid.model.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
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

public class Review extends Activity implements OnClickListener{

	private String listnum;
	private int currentnum;
	private ArrayList<Word> list = new ArrayList<Word>();
    private Button remember;
    private Button notremember;
    private Button add;
    private Button right;
    private Button wrong;
    private Button nextone;
    private TextView spelling;
    private TextView info;
    private TextToSpeech tts;
    private ImageButton speak;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.reviewlayout);
		currentnum=0;
		Bundle bundle = new Bundle();
		bundle= this.getIntent().getExtras();
		listnum=bundle.getString("list");
		this.setTitle("��ϰLIST-"+listnum);
		DataAccess data = new DataAccess(this);
		list=data.QueryWord("LIST = '"+listnum+"'", null);
       // list=initWordList(data.QueryWord("LIST = '"+listnum+"'", null),bundle.getString("order"));
		initWidgets();
		UpdateView();
	}
	private void UpdateView() {
		// TODO Auto-generated method stub
		if (currentnum<list.size()){
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
		spelling.setText(list.get(currentnum).getID()+"."+list.get(currentnum).getSpelling());
        info.setText(list.get(currentnum).getPhonetic_alphabet()+"\n �뾡�����뵥��������˼...");
        remember.setVisibility(View.VISIBLE);
        notremember.setVisibility(View.VISIBLE);
        right.setVisibility(View.GONE);
        wrong.setVisibility(View.GONE);
        nextone.setVisibility(View.GONE);}
		else {
			DataAccess data = new DataAccess(this);
			WordList wordlist=data.QueryList("BOOKID ='"+DataAccess.bookID+"'AND LIST = '"+listnum+"'", null).get(0);
			wordlist.setShouldReview("0");
			wordlist.setReview_times(String.valueOf((Integer.parseInt(wordlist.getReview_times())+1)));
			Calendar cal = Calendar.getInstance();
		    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		    String date=f.format(cal.getTime());
			wordlist.setReviewTime(date);
			data.UpdateList(wordlist);
			
			Dialog dialog = new AlertDialog.Builder(this)
            .setIcon(R.drawable.dialog_icon)
            .setTitle("��ϰ�����")
            .setMessage("�������ڸ�ϰ�ƻ��в鿴�´θ�ϰʱ�䣡")
            .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked OK so do some stuff */
                	tts.shutdown();
                	finish();
                	Intent intent = new Intent();
            		intent.setClass(Review.this, ReviewMain.class);
            		startActivity(intent);
                }
            }).create();
			dialog.show();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
			 Dialog dialog = new AlertDialog.Builder(this)
	            .setIcon(R.drawable.dialog_icon)
	            .setTitle("��ϰδ���")
	            .setMessage("��ȷ�����ڽ�����ϰ���⽫���±��θ�ϰ��Ч��")
	            .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked OK so do some stuff */
	                	tts.shutdown();
	                	finish();
	                	Intent intent = new Intent();
	            		intent.setClass(Review.this, ReviewMain.class);
	            		startActivity(intent);
	                }
	            })
	            .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked OK so do some stuff */
	                }
	            }).create();
				dialog.show();
		 }
			 
		return true;
	}
	
	private void initWidgets() {
		// TODO Auto-generated method stub
		this.add=(Button) this.findViewById(R.id.add);
		add.setOnClickListener(this);
		this.info=(TextView) this.findViewById(R.id.info);
		this.nextone=(Button) this.findViewById(R.id.nextone);
		nextone.setOnClickListener(this);
		this.notremember=(Button) this.findViewById(R.id.notremember);
		notremember.setOnClickListener(this);
		this.remember=(Button) this.findViewById(R.id.remember);
		remember.setOnClickListener(this);
		this.right=(Button) this.findViewById(R.id.right);
		right.setOnClickListener(this);
		this.spelling=(TextView) this.findViewById(R.id.spelling);
		this.wrong=(Button) this.findViewById(R.id.wrong);
		wrong.setOnClickListener(this);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUI.TTF");
		info.setTypeface(tf);
		this.speak=(ImageButton) this.findViewById(R.id.ImageButton02);
		speak.setOnClickListener(this);
		tts = new TextToSpeech(this, ttsInitListener);
		DisplayMetrics dm = new DisplayMetrics(); 
		   dm = getApplicationContext().getResources().getDisplayMetrics(); 
		   int screenWidth = dm.widthPixels; 
		   add.setWidth(screenWidth/3);
		   nextone.setWidth(screenWidth/3*2);
		   notremember.setWidth(screenWidth/3);
		   remember.setWidth(screenWidth/3);
		   right.setWidth(screenWidth/3);
		   wrong.setWidth(screenWidth/3);
		   
		   
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
	      /* ����Ƿ�֧�������ʱ�� */
	      if (tts.isLanguageAvailable(loc) == TextToSpeech.LANG_AVAILABLE)
	      {
	        /* �趨���� */
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	    if (v==speak){
			tts.speak(list.get(currentnum).getSpelling(),TextToSpeech.QUEUE_ADD,
		              null);
		}
		if (v==add){
		}
		if (v==remember){
			wrong.setVisibility(View.VISIBLE);
			right.setVisibility(View.VISIBLE);
			 remember.setVisibility(View.GONE);
		        notremember.setVisibility(View.GONE);
		        info.setText(list.get(currentnum).getPhonetic_alphabet()+"\n"+list.get(currentnum).getMeanning());
		}
		if (v==notremember){
			nextone.setVisibility(View.VISIBLE);
			 remember.setVisibility(View.GONE);
		        notremember.setVisibility(View.GONE);
		        info.setText(list.get(currentnum).getPhonetic_alphabet()+"\n"+list.get(currentnum).getMeanning());
		}
		if (v==right){
			currentnum++;
			this.UpdateView();
		}
		if (v==wrong||v==nextone){
			Word word = new Word();
			word=list.get(currentnum);
			list.add(word);
			currentnum++;
			this.UpdateView();
		}
		if (v==add){
			DataAccess data = new DataAccess(Review.this);
			ArrayList<Word> attention = new ArrayList<Word>();
			attention=data.QueryAttention("SPELLING ='"+list.get(currentnum).getSpelling()+"'", null);
			if (attention.size()==0){
				data.InsertIntoAttention(list.get(currentnum));
				Toast.makeText(Review.this, "�Ѽ������ʱ�", Toast.LENGTH_SHORT).show();
			}
			else Toast.makeText(Review.this, "���ʱ����Ѱ���������ʣ�", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	

}
