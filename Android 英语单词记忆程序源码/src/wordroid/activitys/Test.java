package wordroid.activitys;

import java.util.ArrayList;

import wordroid.database.DataAccess;
import wordroid.model.BookList;
import wordroid.model.R;
import wordroid.model.Word;
import wordroid.model.WordList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Test extends Activity {
	TextView spelling;
	RadioGroup meaning;
	RadioButton meaning0, meaning1, meaning2, meaning3;
	Button nextBtn, overBtn, attention;
	ArrayList<Word> list,allList;
	int allListLength;
	int listLength;
	int[] opt;
	int currentId;
	int rightAns;
	int score = 0;
	boolean isCheck;
	Toast toast;
	int[] rand;
	DataAccess dataAccess;
	WordList wordList;
	int wordListInt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.test);
		
		Intent intent = getIntent();
		String value = intent.getStringExtra("wordList");
		wordListInt = Integer.parseInt(value) + 1;
		this.setTitle("测试LIST-"+wordListInt);
		dataAccess = new DataAccess(this);
		allList = dataAccess.QueryWord(null, null);		
		list = dataAccess.QueryWord("LIST='" + wordListInt + "'", null);
		listLength = list.size();
		allListLength = allList.size();
		System.out.println("listLength2 " + listLength);
		rand = random();
		
		spelling = (TextView) findViewById(R.id.Spelling);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUI.TTF");
		spelling.setTypeface(tf);
		meaning = (RadioGroup) findViewById(R.id.Meanning);
		meaning0 = (RadioButton) findViewById(R.id.RadioButton0);
		meaning1 = (RadioButton) findViewById(R.id.RadioButton1);
		meaning2 = (RadioButton) findViewById(R.id.RadioButton2);
		meaning3 = (RadioButton) findViewById(R.id.RadioButton3);
		nextBtn = (Button) findViewById(R.id.NextButton);
		overBtn = (Button) findViewById(R.id.OverButton);
		overBtn.setVisibility(View.GONE);
		DisplayMetrics dm = new DisplayMetrics(); 
		   dm = getApplicationContext().getResources().getDisplayMetrics(); 
		   int screenWidth = dm.widthPixels; 
		   nextBtn.setWidth(screenWidth/2);
		   overBtn.setWidth(screenWidth/2);
		this.attention= (Button) this.findViewById(R.id.test_addintoattention);
		attention.setWidth(screenWidth/2);
		this.attention.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				DataAccess data = new DataAccess(Test.this);
				ArrayList<Word> attention = new ArrayList<Word>();
				attention=data.QueryAttention("SPELLING ='"+list.get(rand[currentId]).getSpelling()+"'", null);
				if (attention.size()==0){
					data.InsertIntoAttention(list.get(rand[currentId]));
					Toast.makeText(Test.this, "已加入生词本", Toast.LENGTH_SHORT).show();
				}
				else Toast.makeText(Test.this, "生词本中已包含这个单词！", Toast.LENGTH_SHORT).show();
			}
			
		});
		
		overBtn.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				ArrayList<WordList> wordLists = dataAccess.QueryList("BOOKID = '"+DataAccess.bookID+"'and LIST='" + wordListInt + "'",null);
				wordList = wordLists.get(0);
				String bestScore = wordList.getBestScore();
				
				System.out.println("bestScore" + bestScore);
						
				if (bestScore.equals("")) {
					wordList.setBestScore(""+(score*100/listLength));
				} else {
					int bestScoreInt = Integer.parseInt(bestScore);
					if (bestScoreInt < (score*100/listLength)) {
						wordList.setBestScore(""+(score*100/listLength));
					}
				} 
				dataAccess.UpdateList(wordList);
				
				Dialog dlg = new AlertDialog.Builder(Test.this)
				.setTitle("测试结果")
				.setMessage("共" + listLength + "题，做对" + score + "题， 正确率" + (score*100/listLength) + "%")
				.setPositiveButton("返回", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
//						Intent intent = new	Intent();
//						intent.putExtra("testIntent", "123");
//			    		intent.setClass(Test.this, Main.class);
//			    		Test.this.startActivity(intent);
						finish();
					}
				}).create();
				
				dlg.show();			
			}
		});
		
		productQues(rand[currentId]);
		
		meaning.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub	
				
//				System.out.println("currentId " + currentId);
//				System.out.println("listLength " + listLength);
				if (isCheck) {
					isCheck = false;
					switch (rightAns) {
						case 0:
							if ( checkedId == meaning0.getId() ) {
								score++;
								DisplayToast("正确");
							} else {
								DisplayToast("错误,正确答案是 " + allList.get(opt[0]).getMeanning());
							}
							break;
						case 1:
							if ( checkedId == meaning1.getId() ) {
								score++;
								DisplayToast("正确");
							} else {
								DisplayToast("错误,正确答案是 " + allList.get(opt[1]).getMeanning() );
							}
							break;
						case 2:
							if ( checkedId == meaning2.getId() ) {
								score++;
								DisplayToast("正确");
							} else {
								DisplayToast("错误,正确答案是 " + allList.get(opt[2]).getMeanning() );
							}
							break;
						case 3:
							if ( checkedId == meaning3.getId() ) {
								score++;
								DisplayToast("正确");
							} else {
								DisplayToast("错误,正确答案是 " + allList.get(opt[3]).getMeanning() );
							}
							break;
					}
					
					meaning0.setEnabled(false);
					meaning1.setEnabled(false);
					meaning2.setEnabled(false);
					meaning3.setEnabled(false);
					
					if ( currentId == (listLength-1) ) {
						nextBtn.setEnabled(false);
						overBtn.setVisibility(View.VISIBLE);	
						attention.setVisibility(View.VISIBLE);
					} else {
						nextBtn.setVisibility(View.VISIBLE);
						attention.setVisibility(View.VISIBLE);
					}
				}
				
			}
		});
		
		nextBtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				currentId++;
				productQues(rand[currentId]);

			}
			
		});
		
	}
	
	public void productQues(int id){
		Word word = list.get(id);
		opt = options(word.getID());
		spelling.setText(word.getSpelling()+" "+word.getPhonetic_alphabet());
		meaning0.setText(allList.get(opt[0]).getMeanning());
		meaning1.setText(allList.get(opt[1]).getMeanning());
		meaning2.setText(allList.get(opt[2]).getMeanning());
		meaning3.setText(allList.get(opt[3]).getMeanning());
		meaning.clearCheck();
		meaning0.setEnabled(true);
		meaning1.setEnabled(true);
		meaning2.setEnabled(true);
		meaning3.setEnabled(true);
		nextBtn.setVisibility(View.GONE);
		attention.setVisibility(View.GONE);
		isCheck = true;
	}
	
	public int[] options (String wordId) {
		int id = Integer.parseInt(wordId);
		int optionNum[] = new int[4]; 
		
		for (int i = 0; i < 4; i++) {
			optionNum[i] = (int)(Math.random()*allList.size());
			if ( i > 0){
				for (int j = 0; j < i; j++) {
					if ( optionNum[j] == optionNum[i]) {
						i--;
						break;
					}
				}
			}
		}
		
		boolean isExist = true;
		for (int k = 0; k < 4; k++) {
			if ( optionNum[k] == (id-1) ) {
				isExist = false;
				break;
			}
		}
		
		if (isExist) {
			rightAns = (int)(Math.random()*4);
			optionNum[rightAns] = id-1;
		}
		
		
		return optionNum;
	}
	
	public int[] random() {
		int temp;
		int temp1;
		int rand[];
		rand = new int[listLength];
		for (int i = 0 ; i < listLength; i++) {
			rand[i] = i;
		}
		
		for (int i = 0 ; i < listLength; i++) {
			temp = rand[i];
			temp1 = (int)(Math.random()*listLength);
			rand[i] = rand[temp1];
			rand[temp1] = temp;
		}
		
		for (int i = 0 ; i < listLength; i++) {
			System.out.println(rand[i]);
		}
		return rand;
	}
	
	public void DisplayToast(String str) {
		toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		toast.show();
	}
	

}
